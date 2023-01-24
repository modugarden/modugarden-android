package com.example.modugarden.main.settings

import android.app.Activity
import android.provider.Settings
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.R
import com.example.modugarden.ui.theme.TopBar

enum class SettingsScreen(val title: String) {
    Main(title = "설정"),
    Profile(title = "프로필 설정"),
    Notification(title = "알림"),
    Block(title = "차단한 사용자"),
    Terms(title = "약관 및 개인정보 처리 동의"),
    Withdraw(title = "탈퇴하기")
}

@Composable
fun ProfileSettingsScreen(
    context: Activity,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entryp
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = SettingsScreen.valueOf(
        backStackEntry?.destination?.route ?: SettingsScreen.Main.name
    )

    Scaffold(
        topBar = {
            TopBar(
                title = currentScreen.title,
                titleIcon = R.drawable.ic_arrow_left_bold,
                titleIconOnClick = {
                    if(navController.previousBackStackEntry != null)
                    { navController.navigateUp() }
                    else
                    { context.finish() }
                },
                bottomLine = currentScreen.title != SettingsScreen.Withdraw.title
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SettingsScreen.Main.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = SettingsScreen.Main.name) {
                SettingsMainScreen(
                    onProfileClicked = { navController.navigate(SettingsScreen.Profile.name) },
                    onNotificationClicked = { navController.navigate(SettingsScreen.Notification.name) },
                    onBlockClicked = { navController.navigate(SettingsScreen.Block.name) },
                    onTermsClicked = { navController.navigate(SettingsScreen.Terms.name) },
                    onWithdrawClicked = { navController.navigate(SettingsScreen.Withdraw.name) }
                )
            }
            composable(route = SettingsScreen.Profile.name) {
                SettingsProfileScreen { navController.navigateUp() }
            }
            composable(route = SettingsScreen.Notification.name) {
                SettingsNotificationScreen()
            }
            composable(route = SettingsScreen.Block.name) {
                SettingsBlockScreen()
            }
            composable(route = SettingsScreen.Terms.name) {

            }
            composable(route = SettingsScreen.Withdraw.name) {
                SettingsWithdrawScreen()
            }
        }
    }
}