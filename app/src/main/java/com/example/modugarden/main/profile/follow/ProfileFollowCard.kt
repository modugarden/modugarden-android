package com.example.modugarden.main.profile.follow

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
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
import com.example.modugarden.ui.theme.*

// 팔로잉, 팔로워 프로필 카드
@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun ProfileCard(

) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .bounceClick {

            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.test_image1),
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

        val btnText = remember { mutableStateOf("팔로우") }
        val btnColor = remember { mutableStateOf(moduPoint) }
        val btnTextColor = remember { mutableStateOf(Color.White) }
        Card(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .wrapContentSize()
                .bounceClick {
                    // 팔로우 api
                },
            shape = RoundedCornerShape(5.dp),
            backgroundColor = btnColor.value
        ) {
            Text(
                text = btnText.value,
                style = TextStyle(
                    color = btnTextColor.value,
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