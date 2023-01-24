package com.example.modugarden.main.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.R
import com.example.modugarden.ui.theme.BottomButton
import com.example.modugarden.ui.theme.TopBar
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduErrorPoint

@Composable
fun SettingsWithdrawScreen () {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = "탈퇴하시면\n다음 서비스를 이용할 수 없어요",
            style = TextStyle(
                color = moduBlack,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(horizontal = 18.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomButton(
            title = "탈퇴하기",
            color = moduErrorPoint,
            onClick = {
                // 탈퇴 api
            }
        )
    }
}