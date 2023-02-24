package com.example.modugarden.main.profile.follow

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modugarden.R
import com.example.modugarden.api.dto.FollowListDtoResContent
import com.example.modugarden.ui.theme.ScaffoldSnackBar
import com.example.modugarden.ui.theme.TopBar
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_strong
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

val pages = listOf("팔로워", "팔로잉")

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProfileFollowMainScreen(
    id: Int,
    navController: NavController,
    followerList: SnapshotStateList<FollowListDtoResContent>,
    followingList: SnapshotStateList<FollowListDtoResContent>,
    getFollowerList: KFunction1<Int, Job>,
    getFollowingList: KFunction1<Int, Job>,
    onUserClick: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    Log.d("userId", id.toString())

    val newFollowerList = remember { followerList }
    val newFollowingList = remember { followingList }

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
                titleIconOnClick = { navController.navigateUp() },
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
                        getFollowerList(id)
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(18.dp),
                            contentPadding = PaddingValues(18.dp)
                        ) {
                            items(
                                items = newFollowerList,
                                key = { user ->
                                    user.userId.toString() + user.follow
                                }) { follower ->
                                ProfileCard(follower, onUserClick) { isFollowing ->
                                    scope.launch{
                                        val snackBar = scope.launch {
                                            if (isFollowing)
                                                scaffoldState.snackbarHostState.showSnackbar(
                                                    "${follower.nickname} 님을 언팔로우 했어요."
                                                )
                                            else
                                                scaffoldState.snackbarHostState.showSnackbar(
                                                    "${follower.nickname} 님을 팔로우 했어요."
                                                )
                                        }
                                            delay(900)
                                        snackBar.cancel()
                                    }
                                }
                            }
                        }
                    }
                    1 -> {
                        getFollowingList(id)
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(18.dp),
                            contentPadding = PaddingValues(18.dp)
                        ) {
                            items(
                                items = newFollowingList,
                                key = { user -> user.userId }) {following ->
                                ProfileCard(following, onUserClick) { isFollowing ->
                                    scope.launch{
                                        val snackBar = scope.launch {
                                            if (isFollowing)
                                                scaffoldState.snackbarHostState.showSnackbar(
                                                    "${following.nickname} 님을 언팔로우 했어요."
                                                )
                                            else
                                                scaffoldState.snackbarHostState.showSnackbar(
                                                    "${following.nickname} 님을 팔로우 했어요."
                                                )
                                        }
                                        delay(900)
                                        snackBar.cancel()
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