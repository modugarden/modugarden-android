package com.example.modugarden.main.follow

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modugarden.main.settings.SettingsActivity
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBackground

@Composable //팔로우 피드.
fun FollowScreen(navController: NavController) {
    val mContext = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(moduBackground)
    ) {
        Text(
            text = "Follow Feed",
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.Center)
        )
        Card(
            modifier = Modifier
                .bounceClick {
                    mContext.startActivity(Intent(mContext, SettingsActivity::class.java))
                }
                .padding(10.dp),
            backgroundColor = Color.Blue,
        ) {
            Text("hello!!")
        }
    }
}