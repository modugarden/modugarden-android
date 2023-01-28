package com.example.modugarden.main.profile.follow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.modugarden.R
import com.example.modugarden.data.User
import com.example.modugarden.main.profile.*
import com.example.modugarden.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

val pages = listOf("팔로워", "팔로잉")

val followerList = listOf(
    User("https://blog.kakaocdn.net/dn/dTQvL4/btrusOKyP2u/TZBNHQSAHpJU5k8vmYVSvK/img.png".toUri(),
        "Mara", categoryResponse, 100, 50,
    true, postResponse, curationResponse
    )
)

val followingList = listOf(
    User("https://blog.kakaocdn.net/dn/dTQvL4/btrusOKyP2u/TZBNHQSAHpJU5k8vmYVSvK/img.png".toUri(),
        "Mara", categoryResponse, 100, 50,
    true, postResponse, curationResponse
    )
)

@OptIn(ExperimentalPagerApi::class)
class ProfileFollowActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            val scope = rememberCoroutineScope()
            val scaffoldState = rememberScaffoldState()
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White),
                scaffoldState = scaffoldState,
                snackbarHost = {
                    ScaffoldSnackBar (
                        snackbarHostState = it
                    )
                }
            ) {
                val pagerState = rememberPagerState()
                val coroutineScope = rememberCoroutineScope()
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    TopBar(
                        title = "팔로우",
                        titleIcon = R.drawable.ic_arrow_left_bold,
                        titleIconOnClick = { finish() },
                        bottomLine = false
                    )
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        backgroundColor = Color.White,
                        contentColor = moduBlack,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                                color = Color.Black
                            )
                        }
                    ) {
                        pages.forEachIndexed { index, title ->
                            Tab(
                                text = {
                                    Text(
                                        text = title,
                                        fontSize = 16.sp,
                                        color =
                                        if(pagerState.currentPage == index) moduBlack
                                        else moduGray_strong
                                    )
                                },
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                            )
                        }
                    }

                    HorizontalPager(
                        count = pages.size,
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                    ) { page ->
                        when (page) {
                            0 -> {
                                LazyColumn(
                                    modifier = Modifier
                                        .padding(18.dp)
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(18.dp)
                                ) {
                                    items(followerList) { follower ->
                                        ProfileCard(follower) { isFollowing ->
                                            scope.launch {
                                                if(isFollowing)
                                                    scaffoldState.snackbarHostState.showSnackbar("${follower.name} 님을 언팔로우 했어요.")
                                                else
                                                    scaffoldState.snackbarHostState.showSnackbar("${follower.name} 님을 팔로우 했어요.")
                                            }
                                        }
                                    }
                                }
                            }
                            1 -> {
                                LazyColumn(
                                    modifier = Modifier
                                        .padding(18.dp)
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(18.dp)
                                ) {
                                    items(followingList) { following ->
                                        ProfileCard(following) { isFollowing ->
                                            scope.launch {
                                                if(isFollowing)
                                                    scaffoldState.snackbarHostState.showSnackbar("${following.name} 님을 언팔로우 했어요.")
                                                else
                                                    scaffoldState.snackbarHostState.showSnackbar("${following.name} 님을 팔로우 했어요.")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}