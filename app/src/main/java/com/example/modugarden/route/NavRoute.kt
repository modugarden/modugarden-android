package com.example.modugarden.route

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.modugarden.ApplicationClass.Companion.clientId
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import com.google.accompanist.navigation.animation.composable
import com.example.modugarden.R
import com.example.modugarden.main.discover.DiscoverScreen
import com.example.modugarden.main.follow.FollowScreen
import com.example.modugarden.main.notification.NotificationScreen
import com.example.modugarden.main.profile.follow.ProfileApp
import com.example.modugarden.main.upload.UploadScreen
import com.example.modugarden.viewmodel.UserViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import kotlinx.coroutines.CoroutineScope

enum class NAV_ROUTE_BNB(val routeName: String, val description: String, val icon: Int) { //main 패키지 루트.
    FOLLOW("FOLLOW", "팔로우", R.drawable.ic_home),
    DISCOVER("DISCOVER", "탐색", R.drawable.ic_search_small),
    UPLOAD("UPLOAD", "업로드", R.drawable.ic_plus_solid),
    NOTIFICATION("NOTIFICATION", "알림", R.drawable.ic_notification),
    MYPROFILE("MYPROFILE", "내 프로필", R.drawable.ic_user),

}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraphBNB(
    navController: NavHostController,
    navFollowController:NavHostController,
    userViewModel: UserViewModel = viewModel(),
    scope: CoroutineScope,
    lazyScroll:LazyListState
) {

    val fadeInDuration = 500
    val fadeOutDuration = 50
    val slideInDuration = 200
    val slideOutDuration = 200

    AnimatedNavHost(navController, startDestination = NAV_ROUTE_BNB.FOLLOW.routeName,
        modifier = Modifier.fillMaxSize()) {
        composable(NAV_ROUTE_FOLLOW.USERPROFILE.routeName){
            ProfileApp(userViewModel.getUserId(), false, navController)
        }
        composable(
            NAV_ROUTE_BNB.FOLLOW.routeName,
            enterTransition = {
                fadeIn(animationSpec = tween(fadeInDuration)) +
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(slideInDuration))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(fadeOutDuration)) +
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(slideOutDuration))
            }
        ) {
            FollowScreen(navController, UVforMain = userViewModel, lazyScroll = lazyScroll, navFollowController = navFollowController) }

        composable(
            NAV_ROUTE_BNB.DISCOVER.routeName,
            enterTransition = {
                when(initialState.destination.route) {
                    NAV_ROUTE_BNB.FOLLOW.routeName ->
                        fadeIn(animationSpec = tween(fadeInDuration)) +
                                slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(slideInDuration))
                    else ->
                        fadeIn(animationSpec = tween(fadeInDuration)) +
                                slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(slideInDuration))
                }
            }
        ) { DiscoverScreen(navController) }
        composable( //업로드
            NAV_ROUTE_BNB.UPLOAD.routeName,
            enterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE_BNB.FOLLOW.routeName ->
                        fadeIn(animationSpec = tween(fadeInDuration)) +
                                slideIntoContainer(
                                    AnimatedContentScope.SlideDirection.Left,
                                    animationSpec = tween(slideInDuration)
                                )
                    NAV_ROUTE_BNB.DISCOVER.routeName ->
                        fadeIn(animationSpec = tween(fadeInDuration)) +
                                slideIntoContainer(
                                    AnimatedContentScope.SlideDirection.Left,
                                    animationSpec = tween(slideInDuration)
                                )
                    else ->
                        fadeIn(animationSpec = tween(fadeInDuration)) +
                                slideIntoContainer(
                                    AnimatedContentScope.SlideDirection.Right,
                                    animationSpec = tween(slideInDuration)
                                )
                }
            },
            exitTransition = {
                when(targetState.destination.route) {
                    NAV_ROUTE_BNB.FOLLOW.routeName ->
                        fadeOut(animationSpec = tween(fadeOutDuration)) +
                                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(slideOutDuration))
                    NAV_ROUTE_BNB.DISCOVER.routeName ->
                        fadeOut(animationSpec = tween(fadeOutDuration)) +
                                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(slideOutDuration))
                    else ->
                        fadeOut(animationSpec = tween(fadeOutDuration)) +
                                slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(slideOutDuration))
                }
            }
        ) { UploadScreen(navController) }
        composable(
            NAV_ROUTE_BNB.NOTIFICATION.routeName,
            enterTransition = {
                when(initialState.destination.route) {
                    NAV_ROUTE_BNB.FOLLOW.routeName ->
                        fadeIn(animationSpec = tween(fadeInDuration)) +
                                slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(slideInDuration))
                    NAV_ROUTE_BNB.DISCOVER.routeName ->
                        fadeIn(animationSpec = tween(fadeInDuration)) +
                                slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(slideInDuration))
                    NAV_ROUTE_BNB.UPLOAD.routeName ->
                        fadeIn(animationSpec = tween(fadeInDuration)) +
                                slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(slideInDuration))
                    NAV_ROUTE_BNB.MYPROFILE.routeName ->
                        fadeIn(animationSpec = tween(fadeInDuration)) +
                                slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(slideInDuration))
                    else -> fadeIn(animationSpec = tween(fadeInDuration))
                }
            },
            exitTransition = {
                when(targetState.destination.route) {
                    NAV_ROUTE_BNB.FOLLOW.routeName ->
                        fadeOut(animationSpec = tween(fadeInDuration)) +
                                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(slideInDuration))
                    NAV_ROUTE_BNB.DISCOVER.routeName ->
                        fadeOut(animationSpec = tween(fadeInDuration)) +
                                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(slideInDuration))
                    NAV_ROUTE_BNB.UPLOAD.routeName ->
                        fadeOut(animationSpec = tween(fadeInDuration)) +
                                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(slideInDuration))
                    NAV_ROUTE_BNB.MYPROFILE.routeName ->
                        fadeOut(animationSpec = tween(fadeInDuration)) +
                                slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(slideInDuration))
                    else -> fadeOut(animationSpec = tween(fadeInDuration))
                }
            }
        ) { NotificationScreen(navController) }
        composable(
            NAV_ROUTE_BNB.MYPROFILE.routeName,
            enterTransition = {
                fadeIn(animationSpec = tween(fadeInDuration)) +
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(slideInDuration))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(fadeOutDuration)) +
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(slideOutDuration))
            }
        ) {
            ProfileApp(sharedPreferences.getInt(clientId,0), true, navController)
        }
    }
}



