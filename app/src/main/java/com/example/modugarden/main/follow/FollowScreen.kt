package com.example.modugarden.main.follow

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.api.dto.GetFollowFeedCuration
import com.example.modugarden.api.dto.PostDTO
import com.example.modugarden.route.NavigationGraphFollow
import com.example.modugarden.viewmodel.RefreshViewModel
import com.example.modugarden.viewmodel.UserViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FollowScreen(navController: NavHostController, userViewModel: UserViewModel= viewModel()){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val navFollowController = rememberNavController()
        NavigationGraphFollow(navController, navFollowController, userViewModel)
    }
}
@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FollowMainScreen(navController: NavHostController,
                     navFollowController: NavHostController,
                    userViewModel: UserViewModel = viewModel(),
                     refreshViewModel :RefreshViewModel = viewModel()
) {
    val context = LocalContext.current.applicationContext
    val mode = remember { mutableStateOf(true) }
    val isRefreshing by refreshViewModel.isRefreshing.collectAsState()

    //팔로우 피드 게시물
    val postres
            = remember { mutableStateOf(PostDTO.GetFollowFeedPost()) }
    refreshViewModel.getPosts(postres,context)
    val posts = mutableStateOf(postres.value.content)
    val curationres
            = remember { mutableStateOf(GetFollowFeedCuration()) }
    refreshViewModel.getCurations(curationres,context)

    val curations = mutableStateOf(curationres.value.content)
    Log.i("모드1",mode.value.toString())

    //포스트, 큐레이션 수
    mode.value = (posts.value.isNotEmpty() || curations.value.isNotEmpty())
    Log.i("모드2",mode.value.toString())
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = {
            refreshViewModel.refresh()
            refreshViewModel.getPosts(postres,context)
            refreshViewModel.getCurations(curationres,context)
            posts.value = postres.value.content
            curations.value=curationres.value.content
            })
    {
        // 포스트 수 + 큐레이션 수 1개 이상일 경우
        Box(){
            if (mode.value) {
                FollowingScreen(
                    posts.value,
                    curations.value,
                    navController = navController,
                    navFollowController = navFollowController,
                    userViewModel = userViewModel
                )
            } else {
                Log.i("시점","else")
                NoFollowingScreen(

                    navController =navFollowController,
                    userViewModel =userViewModel,
                    refreshViewModel = refreshViewModel
                )
            }
        }
    }

}






