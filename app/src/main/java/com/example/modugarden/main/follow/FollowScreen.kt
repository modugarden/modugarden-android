package com.example.modugarden.main.follow

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.viewmodel.RefreshViewModel
import com.example.modugarden.viewmodel.UserViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FollowScreen(navController: NavHostController, UVforMain:UserViewModel,UVforFollow: UserViewModel= viewModel()){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val navFollowController = rememberNavController()
        NavigationGraphFollow(navController, navFollowController, UVforFollow=UVforFollow)
    }
}
@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FollowMainScreen(postres:MutableState<PostDTO.GetFollowFeedPost>,
                     curationres:MutableState<GetFollowFeedCuration>,
                    navController: NavHostController,
                     navFollowController: NavHostController,
                    userViewModel: UserViewModel ,
                     refreshViewModel :RefreshViewModel = viewModel()
) {

    val isRefreshing by refreshViewModel.isRefreshing.collectAsState()
    val context = LocalContext.current.applicationContext
    val posts = mutableStateOf(postres.value.content)
    val curations = mutableStateOf(curationres.value.content)
    val mode = remember { mutableStateOf(0) }
    val following = 1
    val nofollowing = 2
    //포스트, 큐레이션 수
    if (posts.value==null|| curations.value==null) mode.value = 0
    else if (posts.value!!.isEmpty() && curations.value!!.isEmpty()) mode.value=nofollowing
    else mode.value=following


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
            Log.i("모드",mode.value.toString())
            if (mode.value==1) {
                FollowingScreen(
                    posts.value!!,
                    curations.value!!,
                    navController = navController,
                    navFollowController = navFollowController,
                    userViewModel = userViewModel
                )
            } else if(mode.value==2) {
                Log.i("시점","else")
                NoFollowingScreen(

                    navController =navFollowController,
                    userViewModel =userViewModel,
                    refreshViewModel = refreshViewModel
                )
            }
            else {
                val scrollState = rememberScrollState()
                Box(modifier = Modifier.fillMaxSize().background(moduBackground).verticalScroll(scrollState)) {
                    
                }
            }
        }
    }

}






