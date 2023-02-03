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
import com.example.modugarden.api.dto.FollowListDtoResContent
import com.example.modugarden.main.profile.ProfileScreen

import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UserViewModel
import com.skydoves.landscapist.glide.GlideImage

// 팔로잉, 팔로워 프로필 카드
@Composable
fun ProfileCard(
    user: FollowListDtoResContent,
    navController: NavController,
    viewModel: UserViewModel,
    onClick: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .bounceClick {
                viewModel.setUserId(user.userId)
                navController.navigate(ProfileFollowScreen.Profile.name)
            }
    ) {
        GlideImage(
            imageModel = user.profileImg,
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
                text = user.categories.toString(),
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
                .align(Alignment.CenterVertically)
        )
//        Card(
//            modifier = Modifier
//                .align(Alignment.CenterVertically)
//                .wrapContentSize()
//                .bounceClick {
//                    // 팔로우 api
//                    onClick(followState.value)
//                    followState.value = !followState.value
//                },
//            shape = RoundedCornerShape(5.dp),
//            backgroundColor = if(followState.value) { moduBackground } else { moduPoint }
//        ) {
//            Text(
//                text = if(followState.value) { "팔로잉" } else { "팔로우" },
//                style = TextStyle(
//                    color = if(followState.value) { Color.Black } else { Color.White },
//                    fontSize = 11.sp,
//                    fontWeight = FontWeight.Bold,
//                    textAlign = TextAlign.Center
//                ),
//                modifier = Modifier
//                    .padding(vertical = 5.dp, horizontal = 10.dp)
//                    .align(Alignment.CenterVertically)
//            )
//        }
    }
}