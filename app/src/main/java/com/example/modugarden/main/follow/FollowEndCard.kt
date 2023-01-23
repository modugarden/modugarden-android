package com.example.modugarden.main.follow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.route.NAV_ROUTE_BNB
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduPoint

@Composable //팔로우 피드에 표시되는 큐레이션 카드 item.
fun FollowEndCard(navController: NavHostController) {

    Card(Modifier.fillMaxWidth(),
        elevation = 0.dp, backgroundColor = Color.Transparent) {
        Column(Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

            val size = 270.dp
            Spacer(modifier = Modifier.size(size))

            // 안내 문구
            Text(
                text = "더 많은 게시물을 찾고 있나요?",
                style = moduBold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = "탐색에서 다양한 게시물을 만나보세요",
                fontWeight = FontWeight(400),
                fontSize = 14.sp,
                color = moduBlack,
                modifier = Modifier.alpha(0.5f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(18.dp))

            // 탐색 버튼
            Card(modifier = Modifier
                .bounceClick {
                    navController.navigate(NAV_ROUTE_BNB.DISCOVER.routeName)
                }
                .padding(10.dp,8.dp),
                backgroundColor = moduPoint,
                shape = RoundedCornerShape(10.dp),
                elevation = 0.dp
            ) {
                Text(
                    modifier = Modifier.padding(18.dp,8.dp),
                    text = "탐색하러 가기",
                    color = Color.White ,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.size(size))
        }

    }
}

@Preview(showBackground = true)
@Composable
fun FollowEndPreview(){
    val navController = rememberNavController()
    FollowEndCard(navController)
}