package com.example.modugarden.route

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.modugarden.signup.*

enum class NAV_ROUTE_SIGNUP(val routeName: String, val description: String) { //signup 패키지 루트.
    EMAIL("SIGNUP_EMAIL", "이메일"),
    EMAIL_CERT("SIGNUP_EMAIL_CERT", "이메일 인증"),
    PASSWORD("SIGNUP_PASSWORD", "비밀번호"),
    TERMS("SIGNUP_TERMS", "이용 약관"),
    INFO("SIGNUP_INFO", "닉네임, 생년월일"),
    CATEGORY("SIGNUP_CATEGORY", "카테고리"),
    END("SIGNUP_END", "가입 축하")
}
@Composable
fun NavigationGraphSignup(navController: NavHostController) {
    NavHost(navController, startDestination = NAV_ROUTE_SIGNUP.EMAIL.routeName,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(NAV_ROUTE_SIGNUP.EMAIL.routeName) { SignupEmailScreen(navController) }
        composable(
            NAV_ROUTE_SIGNUP.EMAIL_CERT.routeName+"/{certNumber}/{email}",
            arguments = listOf(
                navArgument("certNumber") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val certNumber = backStackEntry.arguments?.getString("certNumber") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""
            SignupEmailCertificationScreen(navController, certNumber, email)
        }
        composable(
            NAV_ROUTE_SIGNUP.PASSWORD.routeName+"/{email}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            SignupPasswordScreen(navController, email)
        }
        composable(
            NAV_ROUTE_SIGNUP.TERMS.routeName+"/{email}/{password}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("password") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val password = backStackEntry.arguments?.getString("password") ?: ""
            SignupTermsScreen(navController, email, password)
        }
        composable(
            NAV_ROUTE_SIGNUP.INFO.routeName+"/{email}/{password}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("password") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val password = backStackEntry.arguments?.getString("password") ?: ""
            SignupInfoScreen(navController, email, password)
        }
        composable(
            NAV_ROUTE_SIGNUP.CATEGORY.routeName+"/{email}/{password}/{name}/{birthday}",
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
            SignupCategoryScreen(navController, email, password, name, birthday)
        }
        composable(NAV_ROUTE_SIGNUP.END.routeName) { SignupEndScreen(navController) }
    }
}