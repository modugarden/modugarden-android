package com.example.modugarden.route

import androidx.compose.animation.*
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.EaseOutCirc
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.example.modugarden.signup.*
import com.example.modugarden.viewmodel.SignupViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost

enum class NAV_ROUTE_SIGNUP(val routeName: String, val description: String) { //signup 패키지 루트.
    EMAIL("SIGNUP_EMAIL", "이메일"),
    EMAIL_CERT("SIGNUP_EMAIL_CERT", "이메일 인증"),
    PASSWORD("SIGNUP_PASSWORD", "비밀번호"),
    TERMS("SIGNUP_TERMS", "이용 약관"),
    INFO("SIGNUP_INFO", "닉네임, 생년월일"),
    CATEGORY("SIGNUP_CATEGORY", "카테고리"),
    END("SIGNUP_END", "가입 축하")
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraphSignup(
    navController: NavHostController,
    signupViewModel: SignupViewModel
) {
    val data = signupViewModel.getAllData()
    AnimatedNavHost(navController, startDestination = NAV_ROUTE_SIGNUP.EMAIL.routeName,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(
            NAV_ROUTE_SIGNUP.EMAIL.routeName,
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
        ) { SignupEmailScreen(navController, data, signupViewModel) }
        composable(
            NAV_ROUTE_SIGNUP.EMAIL_CERT.routeName+"/{certNumber}/{email}",
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
                navArgument("certNumber") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val certNumber = backStackEntry.arguments?.getString("certNumber") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""
            SignupEmailCertificationScreen(navController, certNumber, data, signupViewModel)
        }
        composable(
            NAV_ROUTE_SIGNUP.PASSWORD.routeName+"/{email}",
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
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            SignupPasswordScreen(navController, data, signupViewModel)
        }
        composable(
            NAV_ROUTE_SIGNUP.TERMS.routeName+"/{email}/{password}",
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
                navArgument("email") { type = NavType.StringType },
                navArgument("password") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val password = backStackEntry.arguments?.getString("password") ?: ""
            SignupTermsScreen(navController, data, signupViewModel)
        }
        composable(
            NAV_ROUTE_SIGNUP.INFO.routeName+"/{email}/{password}",
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
                navArgument("email") { type = NavType.StringType },
                navArgument("password") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val password = backStackEntry.arguments?.getString("password") ?: ""
            SignupInfoScreen(navController, data, signupViewModel)
        }
        composable(
            NAV_ROUTE_SIGNUP.CATEGORY.routeName+"/{email}/{password}/{name}/{birthday}",
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500, easing = EaseOutExpo)) +
                        fadeIn(tween(500))
            },
            exitTransition =  {
                              fadeOut(animationSpec = tween(500)) +
                                      scaleOut(animationSpec = tween(500, easing = EaseOutCirc))
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
                navArgument("email") { type = NavType.StringType },
                navArgument("password") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType },
                navArgument("birthday") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val password = backStackEntry.arguments?.getString("password") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val birthday = backStackEntry.arguments?.getString("birthday") ?: ""
            SignupCategoryScreen(navController, data, signupViewModel)
        }
        composable(
            NAV_ROUTE_SIGNUP.END.routeName+"/{name}",
            enterTransition = {
                              fadeIn(animationSpec = tween(500)) +
                                      scaleIn(animationSpec = tween(500, easing = EaseOutCirc))
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
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            SignupEndScreen(navController, data, signupViewModel)
        }
    }
}