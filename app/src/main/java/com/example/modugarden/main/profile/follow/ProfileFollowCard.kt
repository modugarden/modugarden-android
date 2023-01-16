package com.example.modugarden.main.profile.follow

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_normal
import com.example.modugarden.ui.theme.moduPoint

// 팔로잉, 팔로워 프로필 카드
@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun ProfileFollowCard() {
    Row(
        modifier = Modifier.height(50.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.cog_8_tooth),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .clip(CircleShape),
            contentScale = ContentScale.FillHeight
        )
        Column(
            modifier = Modifier
                .padding(start = 20.dp, top = 6.dp, bottom = 6.dp)
        )  {
            Text(
                text = "Mara",
                style = TextStyle(
                    color = moduBlack,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "follower.category.toString()",
                style = TextStyle(
                    color = moduGray_normal,
                    fontSize = 11.sp
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Card(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .wrapContentSize(),
            shape = RoundedCornerShape(5.dp),
            backgroundColor = moduPoint,
            onClick = {}
        ) {
            Text(
                text = "팔로우",
                style = TextStyle(
                    color = Color.White,
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