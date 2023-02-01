package com.example.modugarden.route

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.modugarden.data.followPosts
import com.example.modugarden.main.follow.FollowMainScreen
import com.example.modugarden.main.profile.MyProfileScreen

enum class NAV_ROUTE_FOLLOW(val routeName: String, val description: String){
    FOLLOW("FOLLOW", "팔로우"),
    USERPROFILE("USERPROFILE", "남의 프로필")
}
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SuspiciousIndentation")
@Composable
fun NavigationGraphFollow(
    navController: NavHostController,
    navFollowController: NavHostController
) {

    NavHost(navFollowController, startDestination = NAV_ROUTE_FOLLOW.FOLLOW.routeName,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(
            NAV_ROUTE_FOLLOW.FOLLOW.routeName
        ) {
            FollowMainScreen(navController,navFollowController, followPosts)
        }
        composable(
            NAV_ROUTE_FOLLOW.USERPROFILE.routeName
        ) { backStackEntry ->
            MyProfileScreen(1)
        }
    }
}