package com.example.modugarden.route

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.modugarden.R
import com.example.modugarden.login.MainLoginScreen
import com.example.modugarden.main.content.PostContentCommentScreen
import com.example.modugarden.main.content.PostContentScreen
import com.example.modugarden.main.discover.DiscoverScreen
import com.example.modugarden.main.follow.FollowScreen
import com.example.modugarden.main.follow.NoFollowingScreen
import com.example.modugarden.main.notification.NotificationScreen
import com.example.modugarden.main.profile.MyProfileScreen
import com.example.modugarden.main.upload.UploadScreen
import com.example.modugarden.signup.*

enum class NAV_ROUTE_BNB(val routeName: String, val description: String, val icon: Int) { //main 패키지 루트.

    FOLLOW("FOLLOW", "팔로우", R.drawable.ic_home),
    DISCOVER("DISCOVER", "탐색", R.drawable.ic_search),
    UPLOAD("UPLOAD", "업로드", R.drawable.ic_plus_solid),
    NOTIFICATION("NOTIFICATION", "알림", R.drawable.ic_notification),
    MYPROFILE("MYPROFILE", "내 프로필", R.drawable.ic_user),
    COMMENT("COMMENT","댓글",R.drawable.ic_user)
}
@Composable
fun NavigationGraphBNB(navController: NavHostController) {
    NavHost(navController, startDestination = NAV_ROUTE_BNB.FOLLOW.routeName,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(NAV_ROUTE_BNB.FOLLOW.routeName) { FollowScreen(navController) }
        composable(NAV_ROUTE_BNB.DISCOVER.routeName) { DiscoverScreen(navController) }
        composable(NAV_ROUTE_BNB.UPLOAD.routeName) { UploadScreen(navController) }
        composable(NAV_ROUTE_BNB.NOTIFICATION.routeName) { NotificationScreen(navController) }
        composable(NAV_ROUTE_BNB.MYPROFILE.routeName) { MyProfileScreen() }
    }
}



