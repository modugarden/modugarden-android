package com.example.modugarden.route

import androidx.compose.animation.*
import androidx.compose.animation.core.EaseOutCirc
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.example.modugarden.main.upload.curation.UploadCurationInfoScreen
import com.example.modugarden.main.upload.curation.UploadCurationWebScreen
import com.example.modugarden.main.upload.post.*
import com.example.modugarden.viewmodel.UploadCurationViewModel
import com.example.modugarden.viewmodel.UploadPostViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost

enum class NAV_ROUTE_UPLOAD_POST(val routeName: String, val description: String) { //upload 패키지 루트.
    IMAGELIST("UPLOAD_POST_IMAGELIST", "포스트 업로드 사진 리스트 창"),
    IMAGEEDIT("UPLOAD_POST_IMAGEEDIT", "포스트 업로드 사진 편집창"),
    INFO("UPLOAD_POST_INFO", "포스트 업로드 정보 입력창"),
    IMAGEDETAIL("UPLOAD_POST_IMAGEDETAIL", "포스트 업로드 사진 보기 창"),
    TAGLOCATION("UPLOAD_POST_TAGLOCATION", "포스트 업로드 위치 태그 추가 창"),
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraphUploadPost(
    navController: NavHostController,
    uploadPostViewModel: UploadPostViewModel = viewModel()
) {
    val data = uploadPostViewModel.getAllData()
    AnimatedNavHost(navController, startDestination = NAV_ROUTE_UPLOAD_POST.INFO.routeName,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(
            NAV_ROUTE_UPLOAD_POST.IMAGELIST.routeName,
            enterTransition = {
                if(initialState.destination.route?.contains(NAV_ROUTE_UPLOAD_POST.IMAGEDETAIL.routeName) == true) {
                    fadeIn(tween(500))
                }
                else {
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500, easing = EaseOutExpo)) +
                            fadeIn(tween(500))
                }
            },
            exitTransition =  {
                if(targetState.destination.route?.contains(NAV_ROUTE_UPLOAD_POST.IMAGEDETAIL.routeName) == true) {
                    fadeOut(tween(500))
                }
                else {
                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500, easing = EaseOutExpo)) +
                            fadeOut(tween(500))
                }
            },
            popEnterTransition = {
                if(initialState.destination.route?.contains(NAV_ROUTE_UPLOAD_POST.IMAGEDETAIL.routeName) == true) {
                    fadeIn(tween(500))
                }
                else {
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500, easing = EaseOutExpo)) +
                            fadeIn(tween(500))
                }
            },
            popExitTransition = {
                if(targetState.destination.route?.contains(NAV_ROUTE_UPLOAD_POST.IMAGEDETAIL.routeName) == true) {
                    fadeOut(tween(500))
                }
                else {
                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500, easing = EaseOutExpo)) +
                            fadeOut(tween(500))
                }
            },
        ) {
            UploadPostImageListScreen(navController = navController, data = data, uploadPostViewModel = uploadPostViewModel)
        }
        composable(
            NAV_ROUTE_UPLOAD_POST.IMAGEEDIT.routeName,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500, easing = EaseOutExpo)) +
                        fadeIn(tween(500))
            },
            exitTransition =  {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500, easing = EaseOutExpo)) +
                        fadeOut(tween(500))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500, easing = EaseOutExpo)) +
                        fadeIn(tween(500))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500, easing = EaseOutExpo)) +
                        fadeOut(tween(500))
            },
        ) {
            UploadPostImageEditScreen(navController = navController, data = data, uploadPostViewModel = uploadPostViewModel)
        }
        composable(
            NAV_ROUTE_UPLOAD_POST.IMAGEDETAIL.routeName+"/{index}",
            enterTransition = {
                scaleIn(tween(500, easing = EaseOutExpo)) +
                        fadeIn(tween(500))
            },
            exitTransition =  {
                scaleOut(tween(500, easing = EaseOutExpo)) +
                        fadeOut(tween(200))
            },
            popEnterTransition = {
                scaleIn(tween(500, easing = EaseOutExpo)) +
                fadeIn(tween(500))
            },
            popExitTransition = {
                scaleOut(tween(500, easing = EaseOutExpo)) +
                        fadeOut(tween(200))
            },
            arguments = listOf(
                navArgument("index") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            UploadPostImageDetail(navController = navController, uploadPostViewModel = uploadPostViewModel, data = data, index = index)
        }
        composable(
            NAV_ROUTE_UPLOAD_POST.INFO.routeName,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500, easing = EaseOutExpo)) +
                        fadeIn(tween(500))
            },
            exitTransition =  {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500, easing = EaseOutExpo)) +
                        fadeOut(tween(500))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500, easing = EaseOutExpo)) +
                        fadeIn(tween(500))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500, easing = EaseOutExpo)) +
                        fadeOut(tween(500))
            },
        ) { UploadPostInfoScreen(navController = navController, uploadPostViewModel = uploadPostViewModel, data = data) }
        composable(
            NAV_ROUTE_UPLOAD_POST.TAGLOCATION.routeName+"/{page}",
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500, easing = EaseOutExpo)) +
                        fadeIn(tween(500))
            },
            exitTransition =  {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500, easing = EaseOutExpo)) +
                        fadeOut(tween(500))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500, easing = EaseOutExpo)) +
                        fadeIn(tween(500))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500, easing = EaseOutExpo)) +
                        fadeOut(tween(500))
            },
            arguments = listOf(
                navArgument("page") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val page = backStackEntry.arguments?.getInt("page") ?: 0
            UploadPostTagLocationScreen(navController = navController, uploadPostViewModel = uploadPostViewModel, data = data, page = page)
        }
    }
}