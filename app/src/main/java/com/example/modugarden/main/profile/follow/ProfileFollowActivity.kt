package com.example.modugarden.main.profile.follow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.R
import com.example.modugarden.main.profile.User
import com.example.modugarden.main.profile.categoryResponse
import com.example.modugarden.main.profile.curationResponse
import com.example.modugarden.main.profile.postResponse
import com.example.modugarden.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

val pages = listOf("팔로워", "팔로잉")

val followerList = listOf(
    User(R.drawable.cog_8_tooth, "Mara", categoryResponse, 100, 50,
    true, postResponse, curationResponse
    )
)

val followingList = listOf(
    User(R.drawable.cog_8_tooth, "Mara", categoryResponse, 100, 50,
    true, postResponse, curationResponse
    )
)

@OptIn(ExperimentalPagerApi::class)
class ProfileFollowActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 20.dp)
                            .clickable(onClick = {
                                finish()
                            })
                    )
                    Text(
                        text = "팔로우",
                        style = TextStyle(
                            color = moduBlack,
                            fontSize = 16.sp,
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 15.dp)
                    )
                }

                val pagerState = rememberPagerState()
                val coroutineScope = rememberCoroutineScope()

                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.pagerTabIndicatorOffset(pagerState,tabPositions),
                            color = Color.Black
                        )
                    }
                ) {
                    pages.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.scrollToPage(index)
                                }
                            },
                            modifier = Modifier
                                .height(50.dp)
                                .background(Color.White)
                        ) {
                            Text(
                                text = title,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontWeight =
                                    if(pagerState.currentPage == index)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Normal
                                )
                            )
                        }
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
                                    ProfileFollowCard()
                                }
                            }
                        }
                        1 -> {


                        }
                    }
                }
            }
        }
    }
}