package com.example.modugarden.main.follow

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.graphics.toColorInt
import com.example.modugarden.R
import com.example.modugarden.main.content.PostContentActivity
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_strong
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable //팔로우 피드에 표시되는 포스트 카드 item.

fun PostCard(userID:String,
             scope: CoroutineScope,
             snackbarHostState: SnackbarHostState) {
        // 버튼 바
        val isButtonClickedLike = remember { mutableStateOf(false) }
        val isButtonClickedSave = remember { mutableStateOf(false) }
        //뷰페이저, 인디케이터 페이지 상태 변수
        val order: PagerState = rememberPagerState()
        val mContext = LocalContext.current

        Card(
                modifier = Modifier
                        .padding(start = 18.dp, end = 18.dp, top = 9.dp, bottom = 9.dp)
                        .clip(RoundedCornerShape(20.dp)),
                elevation = 0.dp
        ) {
                Column(
                        modifier = Modifier
                                .background(Color.White)
                ) {


                        Column(Modifier.bounceClick {
                                mContext.startActivity(
                                        Intent(mContext, PostContentActivity::class.java))
                        }) {
                                Row(
                                        modifier = Modifier
                                                .padding(18.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        Image(
                                                painter = painterResource(id = R.drawable.ic_user),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                        .size(26.dp)
                                                        .clip(CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(18.dp))
                                        Text(
                                                text = "$userID",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                        )
                                }


                                // 포스트 카드 이미지 배열
                                val images = listOf(
                                        R.drawable.plant1,
                                        R.drawable.plant2,
                                        R.drawable.plant3
                                )
                                // 포스트 카드 이미지 슬라이드
                                HorizontalPager(
                                        count = images.size,
                                        state = order,
                                        modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(1f),
                                ) {page ->
                                        Image(
                                                painter = painterResource(id = images[page]),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                        .fillMaxWidth()
                                                        .aspectRatio(1f),
                                        )
                                }

                                // 포스트 카드 이미지 슬라이드 인디케이터
                                DotsIndicator(
                                        totalDots = images.size,
                                        selectedIndex = order.currentPage,
                                        unSelectedColor = Color("#75807A66".toColorInt())
                                )

                                Box(modifier = Modifier
                                        .background(Color.White)
                                        // 구분선
                                        .drawBehind {
                                                val strokeWidth = 1 * density
                                                val y = size.height - strokeWidth
                                                drawLine(
                                                        Color("#EBEEED".toColorInt()),
                                                        Offset(0f, y),
                                                        Offset(size.width, y),
                                                        strokeWidth
                                                )
                                        }
                                ) {
                                        // 포스트 정보 (제목, 카테고리, 업로드 시간 )
                                        Column(
                                                modifier = Modifier
                                                        .padding(18.dp)
                                        ) {
                                                Text("Title", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color("#17291F".toColorInt()))
                                                Row(Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween) {
                                                        Text("category", fontSize = 12.sp, color = Color("#75807A".toColorInt()))
                                                        Text("upload time", fontSize = 12.sp, color = Color("#75807A".toColorInt()))
                                                }

                                        }

                                }
                        }


                               Row(
                                       Modifier.padding(18.dp)) {
                                       // 좋아요
                                       Icon(modifier = Modifier
                                               .padding(end = 18.dp)
                                               .bounceClick {
                                                       if (isButtonClickedLike.value)
                                                               isButtonClickedLike.value = false
                                                       else
                                                               isButtonClickedLike.value = true
                                               }
                                               ,painter = painterResource
                                                       (id =
                                               if (isButtonClickedLike.value)
                                                       R.drawable.ic_heart_solid
                                               else
                                                       R.drawable.ic_heart_line
                                               ),
                                               contentDescription = "좋아요",
                                               tint =
                                                       if (isButtonClickedLike.value)
                                                               Color(0xFFFF6767)
                                                       else
                                                               moduBlack

                                       )
                                       // 댓글
                                       Icon(modifier = Modifier
                                               .padding(end = 18.dp)
                                               .bounceClick { },
                                               painter = painterResource(id = R.drawable.ic_chat_line),
                                               contentDescription = "댓글",
                                               tint = moduBlack
                                       )
                                       // 스크랩
                                       Icon(modifier = Modifier.bounceClick {
                                               isButtonClickedSave.value = !isButtonClickedSave.value

                                               if (isButtonClickedSave.value){
                                                       scope.launch {
                                                               snackbarHostState.showSnackbar(
                                                                       "게시물을 저장하였습니다.",
                                                                       duration = SnackbarDuration.Short
                                                               )
                                                       }
                                               }
                                       }
                                               ,painter = painterResource
                                                       (id =
                                               if (isButtonClickedSave.value)
                                                       R.drawable.ic_star_solid
                                               else
                                                       R.drawable.ic_star_line
                                               ),
                                               contentDescription = "스크랩",
                                               tint = moduBlack
                                       )
                                       Spacer(modifier = Modifier.weight(1f))
                                               Icon(modifier = Modifier.bounceClick {  },
                                                       painter = painterResource(id = R.drawable.ic_star_line),
                                                       contentDescription = "신고",
                                                       tint = moduBlack)


                               }

                }
        }

}

// 슬라이드 인디케이터 컴포넌트
@Composable
fun DotsIndicator(
        totalDots : Int,
        selectedIndex : Int,
        selectedColor: Color = moduGray_strong,
        unSelectedColor: Color ,
){
        LazyRow(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp)
                        .background(Color.White)
        , horizontalArrangement = Arrangement.Center

        ) {
                items(totalDots) { index ->
                        if (index == selectedIndex) {
                                Box(
                                        modifier = Modifier
                                                .size(5.dp)
                                                .clip(CircleShape)
                                                .background(selectedColor)
                                )
                        } else {
                                Box(
                                        modifier = Modifier
                                                .size(5.dp)
                                                .clip(CircleShape)
                                                .background(unSelectedColor)
                                )
                        }

                        if (index != totalDots - 1) {
                                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                        }
                }
        }
}
@Composable
fun moduSnackbar(modifier: Modifier){
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        SnackbarHost(
                modifier = modifier,
                hostState = snackbarHostState,
                snackbar = { snackbarData: SnackbarData ->
                        Box(
                                Modifier
                                        .fillMaxWidth()
                                        .background(
                                                Color("#62766B".toColorInt()),
                                                RoundedCornerShape(10.dp)
                                        )
                        ) {
                                Row(
                                        Modifier
                                                .padding(12.dp, 17.dp)
                                ) {
                                        Image(
                                                painter = painterResource(id = R.drawable.ic_check_solid),
                                                contentDescription = "체크",
                                                Modifier.size(16.dp),
                                        )
                                        Spacer(modifier = Modifier.size(12.dp))
                                        Text(
                                                text = snackbarData.message,
                                                color = Color.White,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                        )
                                }

                        }
                })
}

@Preview
@Composable
fun PostPreview(){
        // 팔로우 스낵바 메세지 띄울 때 필요
        val scope = rememberCoroutineScope()
        // 팔로우 스낵바 메세지 상태 변수
        val snackbarHostState = remember { SnackbarHostState() }
        PostCard(userID = "userID", scope =scope , snackbarHostState = snackbarHostState)

}

