package com.example.modugarden.main.notification

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.R
import com.example.modugarden.main.profile.ProfileScreenV4
import com.example.modugarden.main.profile.follow.ProfileApp
import com.example.modugarden.main.settings.SettingsNotificationScreen
import com.example.modugarden.ui.theme.TopBar
import com.example.modugarden.viewmodel.UserViewModel

enum class NotificationScreen (val title: String) {
    Center(title = "알림 센터"),
    New(title = "다른 프로필"),
    Setting(title = "알림 설정")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable //알림 센터.
fun NotificationScreen(navController: NavHostController) {
    Surface (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val viewModel: UserViewModel = viewModel()
        val notificationNavController = rememberNavController()
        NavHost(
            navController = notificationNavController,
            startDestination = NotificationScreen.Center.name,
        ) {
            composable(route = NotificationScreen.Center.name) {
                NotificationCommunicationScreen(notificationNavController, viewModel)
            }
            composable(route = NotificationScreen.New.name) {
                Log.d("Notify_Center", "NotificationScreen New Route")
                ProfileApp(userId = viewModel.getNextUserId(), false, upperNavController =  navController)
            }
            composable(route = NotificationScreen.Setting.name) {
                Column {
                    TopBar(
                        title = "알림",
                        main = true,
                        icon1 = R.drawable.ic_notification,
                        onClick1 = {
                            notificationNavController.navigateUp()
                        },
                        bottomLine = true
                    )
                    SettingsNotificationScreen()
                }
            }
        }
    }
}