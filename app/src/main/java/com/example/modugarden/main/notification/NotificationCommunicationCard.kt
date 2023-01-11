package com.example.modugarden.main.notification

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.R
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduErrorPoint
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_strong

@Composable
fun NotificationCommunicationCard(data: NotificationData) {
    Row(
        modifier = Modifier
            .padding(9.dp)
            .padding(horizontal = 9.dp)
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize(),
            shape = CircleShape,
            border = BorderStroke(5.dp, if(data.type == 3) moduErrorPoint else moduGray_light)
        ) {
            Image(
                painter = painterResource(id = if(data.type == 3) R.drawable.ic_launcher_foreground else data.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
            )
        }
        Spacer(modifier = Modifier.width(18.dp))
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
        ) {
            Row() {
                Text(
                    text = if(data.type == 3) "서비스 이용 제한" else data.name,
                    fontWeight = FontWeight.Bold,
                    color = moduBlack,
                    fontSize = 14.sp
                )
                Text(
                    text = when(data.type) {
                            0 -> "님이 회원님을 팔로우 했어요."
                            1 -> "님이 댓글을 달았어요."
                            2 -> "님이 덧글을 달았어요."
                            3 -> ""
                            else -> "알림 오류"
                    },
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    color = moduBlack
                )
            }
            if(data.type != 0) {
                Text(
                    text = data.description,
                    color = moduGray_strong,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }

    }
}