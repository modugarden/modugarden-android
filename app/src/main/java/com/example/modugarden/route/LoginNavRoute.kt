package com.example.modugarden.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.modugarden.login.MainLoginScreen

enum class NAV_ROUTE_LOGIN(val routeName: String, val description: String) {
    LOGIN("LOGIN_MAIN", "로그인 창"),
}
@Composable
fun NavigationGraphLogin(navController: NavHostController) {
    NavHost(navController, startDestination = NAV_ROUTE_LOGIN.LOGIN.routeName) {
        composable(NAV_ROUTE_LOGIN.LOGIN.routeName) { MainLoginScreen(navController) }
    }
}