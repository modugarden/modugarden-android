package com.example.modugarden.main.profile.follow

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.R
import com.example.modugarden.main.profile.User
import com.example.modugarden.main.profile.user
import com.example.modugarden.ui.theme.*
import com.google.android.gms.common.api.Scope
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

// 팔로잉, 팔로워 프로필 카드
@Composable
fun ProfileCard(
    user: User,
    onClick: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .bounceClick {

            }
    ) {
        GlideImage(
            imageModel = user.image,
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
                text = user.name,
                style = TextStyle(
                    color = moduBlack,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = user.category.toString(),
                style = TextStyle(
                    color = moduGray_normal,
                    fontSize = 11.sp
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        val followState = remember { mutableStateOf(true) }
        Card(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .wrapContentSize()
                .bounceClick {
                    // 팔로우 api
                    onClick(followState.value)
                    followState.value = !followState.value
                },
            shape = RoundedCornerShape(5.dp),
            backgroundColor = if(followState.value) { moduBackground } else { moduPoint }
        ) {
            Text(
                text = if(followState.value) { "팔로잉" } else { "팔로우" },
                style = TextStyle(
                    color = if(followState.value) { Color.Black } else { Color.White },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .padding(vertical = 5.dp, horizontal = 10.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}