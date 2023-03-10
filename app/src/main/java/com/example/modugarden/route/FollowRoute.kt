package com.example.modugarden.route

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.modugarden.main.follow.FollowMainScreen
import com.example.modugarden.main.profile.follow.ProfileApp
import com.example.modugarden.viewmodel.UserViewModel

enum class NAV_ROUTE_FOLLOW(val routeName: String, val description: String){
    FOLLOW("FOLLOW", "팔로우"),
    USERPROFILE("USERPROFILE", "남의 프로필")
}
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SuspiciousIndentation", "UnrememberedMutableState")
@Composable
fun NavigationGraphFollow(
    navController: NavHostController,
    navFollowController: NavHostController,
    UVforFollow: UserViewModel,
    lazyScroll: LazyListState
) {
    NavHost(
        navController= navFollowController,
        startDestination = NAV_ROUTE_FOLLOW.FOLLOW.routeName,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(
            NAV_ROUTE_FOLLOW.FOLLOW.routeName
        ) {
            FollowMainScreen(
                userViewModel= UVforFollow,
                navController = navController,
                navFollowController =navFollowController,
               lazyScroll =  lazyScroll
            )

        }
        composable(
            NAV_ROUTE_FOLLOW.USERPROFILE.routeName
        ) { backStackEntry ->
            ProfileApp(UVforFollow.getUserId(), false, navFollowController)
        }
    }
}