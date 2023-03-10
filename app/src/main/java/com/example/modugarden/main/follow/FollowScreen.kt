package com.example.modugarden.main.follow

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.modugarden.api.dto.GetFollowFeedCuration
import com.example.modugarden.api.dto.PostDTO
import com.example.modugarden.route.NavigationGraphFollow
import com.example.modugarden.ui.theme.ShowProgressBarV2
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.viewmodel.RefreshViewModel
import com.example.modugarden.viewmodel.UserViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FollowScreen(navController: NavHostController,
                 navFollowController:NavHostController,
                 UVforFollow: UserViewModel= viewModel(),
                 lazyScroll: LazyListState
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        NavigationGraphFollow(navController = navController, navFollowController = navFollowController, UVforFollow=UVforFollow, lazyScroll)
    }
}
@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FollowMainScreen(
                    navController: NavHostController,
                     navFollowController: NavHostController,
                    userViewModel: UserViewModel ,
                     refreshViewModel :RefreshViewModel = viewModel(),
                    lazyScroll:LazyListState
) {
    //????????? ?????? ?????????
    val context = LocalContext.current.applicationContext
    val isLoading = remember { mutableStateOf(false) }
    val postres
            = remember { mutableStateOf(PostDTO.GetFollowFeedPost(null)) }
    refreshViewModel.getPosts(postres,context)

    val curationres
            = remember { mutableStateOf(GetFollowFeedCuration(null)) }
    refreshViewModel.getCurations(curationres,context)
    val postLauncher
    = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()) {
        isLoading.value=true
        refreshViewModel.getPosts(postres,context)
    }
    val curationLauncher
    = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()) {
        isLoading.value=true
        refreshViewModel.getCurations(curationres,context)
    }

    val isRefreshing by refreshViewModel.isRefreshing.collectAsState()
    val posts = mutableStateOf(postres.value.content)
    val curations = mutableStateOf(curationres.value.content)
    val mode = remember { mutableStateOf(0) }
    val following = 1
    val nofollowing = 2

    //?????????, ???????????? ???
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
        // ????????? ??? + ???????????? ??? 1??? ????????? ??????
        Box(){
            if (mode.value==1) {
                FollowingScreen(
                    posts.value!!,
                    curations.value!!,
                    navController = navController,
                    navFollowController = navFollowController,
                    userViewModel = userViewModel,
                    postLauncher = postLauncher,
                    curationLauncher = curationLauncher,
                    lazyScroll = lazyScroll,
                    refreshViewModel = refreshViewModel,
                    postRes = postres,
                    context = context
                )
            } else if(mode.value==2) {
                NoFollowingScreen(
                    navFollowController =navFollowController,
                    userViewModel =userViewModel,
                    refreshViewModel = refreshViewModel
                )
            }
            else {
                val scrollState = rememberScrollState()
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(moduBackground)
                    .verticalScroll(scrollState)) {
                    if (isLoading.value){
                        ShowProgressBarV2()
                    }
                }
            }
        }
    }

}






