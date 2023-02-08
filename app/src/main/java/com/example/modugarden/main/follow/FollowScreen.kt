package com.example.modugarden.main.follow

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.R
import com.example.modugarden.api.dto.FollowRecommendationRes
import com.example.modugarden.api.dto.GetFollowFeedCuration
import com.example.modugarden.api.dto.GetFollowFeedCurationContent
import com.example.modugarden.api.dto.PostDTO
import com.example.modugarden.data.Report
import com.example.modugarden.main.content.ReportCategoryItem
import com.example.modugarden.main.content.modalReportPost
import com.example.modugarden.route.NavigationGraphFollow
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_normal
import com.example.modugarden.viewmodel.RefreshViewModel
import com.example.modugarden.viewmodel.UserViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.skydoves.landscapist.glide.GlideImage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FollowScreen(navController: NavHostController){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val navFollowController = rememberNavController()
        NavigationGraphFollow(navController=navController,navFollowController = navFollowController)
    }
}
@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FollowMainScreen(navController: NavHostController,
                     navFollowController: NavHostController,
                    userViewModel: UserViewModel = viewModel(),
                     refreshViewModel :RefreshViewModel = viewModel()
)
{
    val context = LocalContext.current.applicationContext
    val mode = remember { mutableStateOf(true) }
    val isRefreshing by refreshViewModel.isRefreshing.collectAsState()

    //팔로우 피드 게시물
    val postres
            = remember { mutableStateOf(PostDTO.GetFollowFeedPost()) }
    refreshViewModel.getPosts(postres,context)
    val posts = mutableStateOf(postres.value.content)

    //팔로우 추천
    val recommendRes
        = remember { mutableStateOf(FollowRecommendationRes()) }
    refreshViewModel.getRecommend(recommendRes)
    val recommendList = mutableStateOf(recommendRes.value.content)

    //포스트, 큐레이션 수
    mode.value = posts.value.isNotEmpty()
    Log.i("모드",mode.toString())
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = {
            refreshViewModel.refresh()
            refreshViewModel.getPosts(postres,context)
            posts.value = postres.value.content
            })
    {
        // 포스트 수 + 큐레이션 수 1개 이상일 경우
        Box(){
            if (mode.value) {
                FollowingScreen(
                    posts.value,
                    null,
                    navController = navController,
                    navFollowController = navFollowController,
                    userViewModel = userViewModel
                )
            } else {

                NoFollowingScreen(
                    recommendList = recommendList.value,
                    navController=navFollowController,
                    userViewModel=userViewModel,
                    refreshViewModel = refreshViewModel
                )
            }
        }
    }

}






