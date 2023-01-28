package com.example.modugarden.main.discover.search

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.R
import com.example.modugarden.data.CurationCard
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBlack


//포스트, 큐레이션에 표시되는 카드들로 데이터 형식 알려주면 그때 넣겠삼삼
@Composable
fun DiscoverSearchCurationCard(curationCard: CurationCard) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick {

            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
        ) {
            //큐레이션 카드 이미지
            Image(
                painter = painterResource(id = curationCard.thumbnail_image),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 90.dp, height = 90.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )

        }
        Spacer(modifier = Modifier.width(18.dp))

        Column {
            //타이틀
            Text(
                modifier = Modifier.width(200.dp),
                text = curationCard.title,
                style = TextStyle(color = moduBlack,
                    fontWeight = FontWeight(700),
                    fontSize = 14.sp),
                //넘치면 ....으로 표시해주는놈
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(7.dp))

            //시간
            Text(text = curationCard.time,
                style = TextStyle(color = Color(0xFF959DA7),
                    fontWeight = FontWeight(400),fontSize = 11.sp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = 20.dp, height = 20.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(10.dp))
                //작성자
                Text(text = curationCard.user,
                    style = TextStyle(color = Color(0xFF252525).copy(alpha = 0.8f),
                        fontWeight = FontWeight(400),fontSize = 11.sp)
                )


            }
        }

    }

    Spacer(modifier = Modifier.height(18.dp))

}