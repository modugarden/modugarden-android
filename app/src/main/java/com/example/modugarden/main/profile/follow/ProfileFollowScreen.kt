package com.example.modugarden.main.profile.follow

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modugarden.main.profile.ProfileImageDetail
import com.example.modugarden.main.profile.ProfileScreenV2
import com.example.modugarden.main.profile.ProfileScreenV4
import com.example.modugarden.viewmodel.UserViewModel

enum class ProfileFollowScreen (val title: String) {
    Follow(title = "팔로우"),
    Profile(title = "프로필"),
    New(title = "다른 프로필"),
    ProfileImage(title = "프로필 이미지")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileApp(
    userId: Int,
    onBNB: Boolean,
    upperNavController: NavHostController
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        //탐색 메인화면 부름
        ProfileFollow(userId, onBNB, upperNavController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileFollow (
    userId: Int,
    onBNB: Boolean,
    upperNavController: NavHostController,
    viewModel: UserViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    viewModel.setUserId(userId)
    viewModel.setOnBNB(onBNB)
    NavHost(
        navController = navController,
        startDestination = ProfileFollowScreen.Profile.name,
    ) {
        composable(route = ProfileFollowScreen.Follow.name) {
            ProfileFollowMainScreen(viewModel.getUserId(), navController) {
                viewModel.setNextUserId(it)
                navController.navigate(ProfileFollowScreen.New.name)
            }
        }
        composable(route = ProfileFollowScreen.Profile.name) {
            ProfileScreenV4(
                userId = viewModel.getUserId(),
                navController = navController,
                upperNavHostController = upperNavController,
                userViewModel = viewModel
            )
        }
        composable(route = ProfileFollowScreen.New.name) {
            ProfileApp(userId = viewModel.getNextUserId(), false, upperNavController =  navController)
        }
    }
}