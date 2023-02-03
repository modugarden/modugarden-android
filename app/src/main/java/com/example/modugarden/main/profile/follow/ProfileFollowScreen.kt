package com.example.modugarden.main.profile.follow

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modugarden.main.profile.ProfileScreen
import com.example.modugarden.viewmodel.UserViewModel

enum class ProfileFollowScreen (val title: String) {
    Main(title = "팔로우"),
    Profile(title = "프로필")
}

@Composable
fun ProfileFollow (
    userId: Int,
    activity: ProfileFollowActivity,
    viewModel: UserViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = ProfileFollowScreen.Main.name,
    ) {
        composable(route = ProfileFollowScreen.Main.name) {
            ProfileFollowMainScreen(userId, navController, viewModel) {
                activity.finish()
            }
        }
        composable(route = ProfileFollowScreen.Profile.name) {
            ProfileScreen(viewModel.getUserId())
        }
    }
}