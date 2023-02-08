package com.example.modugarden.main.profile.follow

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modugarden.R
import com.example.modugarden.api.AuthCallBack
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.FollowListDtoRes
import com.example.modugarden.api.dto.FollowListDtoResContent
import com.example.modugarden.ui.theme.ScaffoldSnackBar
import com.example.modugarden.ui.theme.TopBar
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_strong
import com.example.modugarden.viewmodel.UserViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val pages = listOf("팔로워", "팔로잉")

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProfileFollowMainScreen(
    id: Int,
    navController: NavController,
    viewModel: UserViewModel,
    onUserClick: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    Log.d("userId", id.toString())

    val newFollowerList = remember { mutableStateOf(
        listOf(FollowListDtoResContent())
    ) }
    val newFollowingList = remember { mutableStateOf(
        listOf(FollowListDtoResContent())
    ) }

    val context = LocalContext.current

    RetrofitBuilder.followAPI.otherFollowerList(id)
        .enqueue(object : Callback<FollowListDtoRes> {
            override fun onResponse(
                call: Call<FollowListDtoRes>,
                response: Response<FollowListDtoRes>
            ) {
                Log.d("onResponse", response.code().toString() +
                        "\n" + (response.body()?.content))
                newFollowerList.value = response.body()?.content!!
            }

            override fun onFailure(call: Call<FollowListDtoRes>, t: Throwable) {
                Log.d("onFailure", "안됨")
            }
        })
    RetrofitBuilder.followAPI.otherFollowingList(id)
        .enqueue(object : AuthCallBack<FollowListDtoRes>(context, "성공") {
            override fun onResponse(
                call: Call<FollowListDtoRes>,
                response: Response<FollowListDtoRes>
            ) {
                super.onResponse(call, response)
                Log.d(
                    "onResponse", response.code().toString() +
                            "\n" + (response.body()?.content)
                )
                newFollowingList.value = response.body()?.content!!
            }

            override fun onFailure(call: Call<FollowListDtoRes>, t: Throwable) {
                Log.d("onFailure", "안됨")
            }
        })

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
                        LazyColumn(
                            modifier = Modifier
                                .padding(18.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            items(
                                items = newFollowerList.value,
                                key = { user -> user.userId }) { follower ->
                                ProfileCard(follower, onUserClick) { isFollowing ->
                                    scope.launch {
                                        if (isFollowing)
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                "${follower.nickname} 님을 언팔로우 했어요."
                                            )
                                        else
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                "${follower.nickname} 님을 팔로우 했어요."
                                            )
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
                            items(
                                items = newFollowingList.value,
                                key = { user -> user.userId }) {following ->
                                ProfileCard(following, onUserClick) { isFollowing ->
                                    scope.launch {
                                        if (isFollowing)
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                "${following.nickname} 님을 언팔로우 했어요."
                                            )
                                        else
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                "${following.nickname} 님을 팔로우 했어요."
                                            )
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