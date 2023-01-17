package com.example.modugarden.main.follow

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.R
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_strong
import com.example.modugarden.ui.theme.moduPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
// 팔로우 추천 목록에 들어갈 카드
fun FollowRecommendCard(userID:String,
                        scope: CoroutineScope = rememberCoroutineScope(),
                        snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }){

    Column() {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(18.dp, 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 사진
            Image(
                painter = painterResource(R.drawable.ic_user),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(50)),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 18.dp)
                ) {
                    // 아이디
                    Text(
                        text = userID,
                        fontSize = 14.sp,
                        style = moduBold
                    )
                    // 카테고리
                    Text(
                        text = "category",
                        fontSize = 11.sp,
                        color = moduGray_strong
                    )
                }
                // 팔로우 버튼
                Card(
                    modifier = Modifier
                        .bounceClick {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "$userID 님을 팔로우 하였습니다.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                    shape = RoundedCornerShape(5.dp),
                    backgroundColor = moduPoint
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp, 4.dp),
                        text = "팔로우",
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }
            }
        }
        Divider(
            color = moduGray_light,
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
    }

}

@Preview
@Composable
fun card(){
    FollowRecommendCard(userID = "1")
}