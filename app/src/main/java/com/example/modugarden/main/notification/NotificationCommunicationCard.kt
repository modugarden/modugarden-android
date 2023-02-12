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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.ApplicationClass
import com.example.modugarden.ApplicationClass.Companion.commentChildNotification
import com.example.modugarden.ApplicationClass.Companion.commentNotification
import com.example.modugarden.ApplicationClass.Companion.followNotification
import com.example.modugarden.ApplicationClass.Companion.serviceNotification
import com.example.modugarden.R
import com.example.modugarden.data.Notification
import com.example.modugarden.ui.theme.*
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun NotificationCommunicationCard(data: Notification, lastItem: Boolean) {
    Row(
        modifier = Modifier
            .padding(top = 25.dp)
            .padding(horizontal = 18.dp)
            .padding(bottom = if (lastItem) 25.dp else 0.dp)
            .fillMaxWidth()
            .bounceClick {
            }
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize(),
            shape = CircleShape,
            border = BorderStroke(1.dp, if(data.type == 3) Color.Transparent else moduGray_light),
            elevation = 0.dp
        ) {
            GlideImage(
                imageModel = if(data.type == 3) R.drawable.ic_notification_restrict else data.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp),
                requestOptions = {
                    RequestOptions().override(256, 256)
                }
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
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = when(data.type) {
                            0 -> "님이 회원님을 팔로우 했어요."
                            1 -> "님이 댓글을 남겼어요."
                            2 -> "님이 답글을 남겼어요."
                            3 -> ""
                            else -> data.type.toString()
                    },
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
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