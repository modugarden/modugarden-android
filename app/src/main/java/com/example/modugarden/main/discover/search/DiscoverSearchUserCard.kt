package com.example.modugarden.main.discover.search

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.R
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBlack


//포스트, 큐레이션에 표시되는 카드들로 데이터 형식 알려주면 그때 넣겠삼삼
@Composable
fun DiscoverSearchUserCard(searchStr : String) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 50.dp, height = 50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

        }
        Spacer(modifier = Modifier.width(18.dp))

        Column {
            Text(text = searchStr,
                style = TextStyle(color = moduBlack,
                    fontWeight = FontWeight(700),
                    fontSize = 14.sp)
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(text = "카테고리 카테고리",
                style = TextStyle(color = Color(0xFF959DA7),
                    fontWeight = FontWeight(400),fontSize = 11.sp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        //팔로우 버튼
        Card(
            modifier = Modifier
                .width(51.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(5.dp))
                .bounceClick {
                    Toast.makeText(context,"팔로우 함 ㅋ", Toast.LENGTH_SHORT).show()
                },
            backgroundColor = Color(0xFF6CD09A),
        ){
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "팔로우",
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight(700), fontSize = 11.sp
                    )
                )
            }
        }


    }

    Spacer(modifier = Modifier.height(18.dp))

}