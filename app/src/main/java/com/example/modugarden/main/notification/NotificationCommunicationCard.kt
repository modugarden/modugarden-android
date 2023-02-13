package com.example.modugarden.main.notification

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.R
import com.example.modugarden.data.Notification
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UserViewModel
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun NotificationCommunicationCard(
    viewModel: UserViewModel,
    navController: NavHostController,
    data: Notification,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick {
                if (data.type == 0) {
                    Log.d("Notify Center", "Click Notification")
                    viewModel.setNextUserId(data.address.toInt())
                    navController.navigate(NotificationScreen.New.name)
                } else if (data.type == 1 || data.type == 2) {
                    // 포스트로 넘어가는 코드
                }
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
                imageModel =
                if(data.type == 3) R.drawable.ic_notification_restrict
                else data.image?.toUri() ?: R.drawable.ic_default_profile,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp),
                requestOptions = {
                    RequestOptions().override(256, 256)
                }
            )
        }
        Spacer(modifier = Modifier.width(18.dp))
        Column(
            modifier = Modifier
                .align(CenterVertically)
        ) {
            Row() {
                Text(
                    text = if(data.type == 3) "서비스 이용 제한" else data.name,
                    fontWeight = FontWeight.Bold,
                    color = moduBlack,
                    fontSize = 13.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = when(data.type) {
                            0 -> "님이 회원님을 팔로우했어요."
                            1 -> "님이 댓글을 남겼어요."
                            2 -> "님이 답글을 남겼어요."
                            3 -> ""
                            else -> data.type.toString()
                    },
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 13.sp,
                    color = moduBlack,
                    maxLines = 1
                )
            }
            if(data.type != 0) {
                Text(
                    text = data.description,
                    color = moduGray_strong,
                    fontSize = 11.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.ic_cross_line),
            contentDescription = null,
            modifier = Modifier
                .align(CenterVertically)
                .size(20.dp)
                .bounceClick {
                    onClick()
                },
            tint = moduGray_normal
        )

    }
}