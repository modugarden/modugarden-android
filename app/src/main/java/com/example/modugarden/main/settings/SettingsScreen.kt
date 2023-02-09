package com.example.modugarden.main.settings

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.R
import com.example.modugarden.api.AuthCallBack
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.UserSettingInfoRes
import com.example.modugarden.ui.theme.TopBar
import com.example.modugarden.viewmodel.SettingViewModel
import retrofit2.Call
import retrofit2.Response

enum class SettingsScreen(val title: String) {
    Main(title = "설정"),
    Profile(title = "프로필 설정"),
    Notification(title = "알림"),
    Block(title = "차단한 사용자"),
    Terms(title = "약관 및 개인정보 처리 동의"),
    Withdraw(title = "")
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

    val settingViewModel: SettingViewModel = viewModel()

    RetrofitBuilder.userAPI.readUserSettingInfo()
        .enqueue(object : AuthCallBack<UserSettingInfoRes>(context, "유저 정보 불러오기 성공!"){
            override fun onResponse(
                call: Call<UserSettingInfoRes>,
                response: Response<UserSettingInfoRes>
            ) {
                super.onResponse(call, response)

                // 생일 형식 변환
                val myBirth = response.body()?.result?.birth!!
                val yyyy = myBirth.substring(0,4)
                val mm = myBirth.substring(4,6)
                val dd = myBirth.substring(6,8)

                settingViewModel.setSettingInfo(
                    response.body()?.result?.nickname!!,
                    "${yyyy}년 ${mm}월 ${dd}일",
                    response.body()?.result?.email!!,
                    response.body()?.result?.categories!!,
                    null
                )

                if(response.body()?.result?.profileImage != null)
                    settingViewModel.setImage(response.body()?.result?.profileImage!!.toUri())
            }
        })

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
                SettingsMainScreen(navController, settingViewModel)
            }
            composable(route = SettingsScreen.Profile.name) {
                SettingsProfileScreen(settingViewModel) { navController.navigateUp() }
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