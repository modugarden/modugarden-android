package com.example.modugarden.main.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.ApplicationClass.Companion.autoLoginSetting
import com.example.modugarden.ApplicationClass.Companion.clientNickname
import com.example.modugarden.ApplicationClass.Companion.profileImage
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import com.example.modugarden.R
import com.example.modugarden.login.LoginActivity
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.SettingViewModel
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun SettingsMainScreen (
    navController: NavController,
    settingViewModel: SettingViewModel
) {
    val dialogState = remember { mutableStateOf(false) }
    val context = LocalContext.current

    if( dialogState.value )
        SmallDialog(
            text = "로그아웃 하시겠어요?",
            textColor = moduBlack,
            backgroundColor = Color.White,
            positiveButtonText = "확인",
            negativeButtonText = "취소",
            positiveButtonTextColor = Color.White,
            negativeButtonTextColor = moduBlack,
            positiveButtonColor = moduPoint,
            negativeButtonColor = moduBackground,
            dialogState = dialogState
        ) {
            sharedPreferences.edit().putBoolean(autoLoginSetting, false).apply()
            context.startActivity(
                Intent(context, LoginActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(moduBackground)
    ) {
        Row (
            modifier = Modifier
                .height(78.dp)
                .fillMaxWidth()
                .bounceClick { navController.navigate(SettingsScreen.Profile.name) }
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.width(18.dp))
            GlideImage(
                imageModel =
                sharedPreferences.getString(profileImage, null) ?: R.drawable.ic_default_profile,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                loading = {
                    ShowProgressBar()
                },
                // shows an error text if fail to load an image.
                failure = {
                    Text(text = "image request failed.")
                },
                requestOptions = {
                    RequestOptions()
                        .override(256,256)
                }
            )
            Spacer(modifier = Modifier.width(18.dp))
            Column (
                modifier = Modifier
                    .height(40.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = sharedPreferences.getString(clientNickname, "") ?: "",
                    style = TextStyle(
                        color = moduBlack,
                        fontSize = 16.sp
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "내 정보 수정하기",
                    style = TextStyle(
                        color = moduGray_strong,
                        fontSize = 12.sp
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(painter = painterResource(
                id = R.drawable.ic_chevron_right),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically),
                contentScale = ContentScale.Inside
            )
            Spacer(modifier = Modifier.width(18.dp))
        }
        Spacer(modifier = Modifier.height(18.dp))
        Options("알림") { navController.navigate(SettingsScreen.Notification.name) }
        Options("차단한 사용자") { navController.navigate(SettingsScreen.Block.name) }
        Spacer(modifier = Modifier.height(18.dp))
        Options("약관 및 개인정보 처리 동의") { navController.navigate(SettingsScreen.Terms.name) }
        Options("로그아웃") { dialogState.value = true }
        Spacer(modifier = Modifier.height(18.dp))
        Options("탈퇴하기") { navController.navigate(SettingsScreen.Withdraw.name) }
    }
}

@Composable
fun Options (
    option: String,
    onClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(58.dp)
            .bounceClick {
                onClicked()
            }
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.width(18.dp))
        Text(
            text = option,
            style = TextStyle(
                color = moduBlack,
                fontSize = 16.sp
            ),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(painter = painterResource(
            id = R.drawable.ic_chevron_right),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.CenterVertically),
            contentScale = ContentScale.Inside
        )
        Spacer(modifier = Modifier.width(18.dp))
    }
}