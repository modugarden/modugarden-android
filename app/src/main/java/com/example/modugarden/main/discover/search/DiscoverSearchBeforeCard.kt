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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.R
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduBlack


//이전에 검색했던 검색기록들을 보여주는 카드
@Composable
fun DiscoverSearchBeforeCard() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .bounceClick {

                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                backgroundColor = moduBackground
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        modifier = Modifier
                            .size(height = 18.dp, width = 18.dp),
                        contentScale = ContentScale.Crop
                    )
                }

            }
            Spacer(modifier = Modifier.width(18.dp))

            Text(
                text = "검색 기록 검색 기록",
                style = TextStyle(
                    color = moduBlack,
                    fontWeight = FontWeight(400),
                    fontSize = 14.sp
                )
            )



        }

        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier.bounceClick {  },
            painter = painterResource(id = R.drawable.ic_cross_line),
            contentDescription = null,
        )
    }
    Spacer(modifier = Modifier.height(18.dp))

}