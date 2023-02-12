package com.example.modugarden.main.profile.follow

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modugarden.R
import com.example.modugarden.api.dto.FollowListDtoResContent

import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UserViewModel
import com.skydoves.landscapist.glide.GlideImage

// 팔로잉, 팔로워 프로필 카드
@Composable
fun ProfileCard(
    user: FollowListDtoResContent,
    onUserClick: (Int) -> Unit = {},
    onClick: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .bounceClick {
                onUserClick(user.userId)
            }
    ) {
        GlideImage(
            imageModel =
            user.profileImage ?: R.drawable.ic_default_profile,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .padding(start = 20.dp, top = 6.dp, bottom = 6.dp)
        )  {
            Text(
                text = user.nickname,
                style = TextStyle(
                    color = moduBlack,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = user.categories.joinToString(", ","",""),
                style = TextStyle(
                    color = moduGray_normal,
                    fontSize = 11.sp
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        val followState = remember { mutableStateOf(user.follow) }
        FollowCard(
            id = user.userId,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .wrapContentSize(),
            snackBarAction = { onClick(followState.value) },
            followState = followState,
            contentModifier = Modifier
                .padding(vertical = 5.dp, horizontal = 10.dp)
                .align(Alignment.CenterVertically),
            fcmTokenState = remember { mutableStateOf(user.fcmTokens) }
        )
    }
}