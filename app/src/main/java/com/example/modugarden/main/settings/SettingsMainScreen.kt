package com.example.modugarden.main.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.R
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_strong

@Preview(showBackground = true)
@Composable
fun SettingsMainScreen (
    onProfileClicked: () -> Unit = {},
    onNotificationClicked: () -> Unit = {},
    onBlockClicked: () -> Unit = {},
    onTermsClicked: () -> Unit = {},
    onWithdrawClicked: () -> Unit = {}
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(moduBackground)
    ) {
        Row (
            modifier = Modifier
                .height(78.dp)
                .fillMaxWidth()
                .bounceClick { onProfileClicked() }
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.width(18.dp))
            Image(
                painter = painterResource(id = R.drawable.test_image1),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(18.dp))
            Column (
                modifier = Modifier
                    .height(40.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "Mara",
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
        Options("알림", onNotificationClicked)
        Options("차단한 사용자", onBlockClicked)
        Spacer(modifier = Modifier.height(18.dp))
        Options("약관 및 개인정보 처리 동의", onTermsClicked)
        Options("탈퇴하기", onWithdrawClicked)
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