package com.example.modugarden.main.profile.follow

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.modugarden.api.AuthCallBack
import com.example.modugarden.api.dto.FollowListDtoRes
import com.example.modugarden.api.dto.FollowListDtoResContent
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.data.User
import com.example.modugarden.main.profile.*
import com.example.modugarden.ui.theme.*
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
class ProfileFollowActivity : ComponentActivity() {

    var newFollowerList = mutableListOf<FollowListDtoResContent>()
    var newFollowingList = mutableListOf<FollowListDtoResContent>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            val scope = rememberCoroutineScope()
            val scaffoldState = rememberScaffoldState()
            val id = intent.extras?.getInt("userId")
            Log.d("userId", id.toString())

            if (id != null) {
                RetrofitBuilder.followAPI.otherFollowerList(id)
                    .enqueue(object : Callback<FollowListDtoRes> {
                        override fun onResponse(
                            call: Call<FollowListDtoRes>,
                            response: Response<FollowListDtoRes>
                        ) {
                            Log.d("onResponse", response.code().toString() +
                                    "\n" + (response.body()?.content))
                            response.body()?.let { newFollowerList.addAll(it.content) }
                        }

                        override fun onFailure(call: Call<FollowListDtoRes>, t: Throwable) {
                            Log.d("onFailure", "안됨")
                        }
                    })
                RetrofitBuilder.followAPI.otherFollowingList(id)
                    .enqueue(object : AuthCallBack<FollowListDtoRes>(this, "성공") {
                        override fun onResponse(
                            call: Call<FollowListDtoRes>,
                            response: Response<FollowListDtoRes>
                        ) {
                            super.onResponse(call, response)
                            Log.d("onResponse", response.code().toString() +
                                     "\n" + (response.body()?.content)
                            )
                            response.body()?.let { newFollowingList.addAll(it.content) }
                        }

                        override fun onFailure(call: Call<FollowListDtoRes>, t: Throwable) {
                            Log.d("onFailure", "안됨")
                        }
                    })
            }
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
                                    items(newFollowerList) { follower ->
                                        ProfileCard(follower) { isFollowing ->
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
                                    items(newFollowingList) { following ->
                                        ProfileCard(following) { isFollowing ->
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
    }
}