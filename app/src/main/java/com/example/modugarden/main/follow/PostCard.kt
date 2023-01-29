package com.example.modugarden.main.follow

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.graphics.toColorInt
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.R
import com.example.modugarden.data.FollowPost
import com.example.modugarden.data.User
import com.example.modugarden.data.followPosts
import com.example.modugarden.main.content.PostContentActivity
import com.example.modugarden.main.content.modalReportPost
import com.example.modugarden.main.content.updateTime
import com.example.modugarden.route.NAV_ROUTE_POSTCONTENT
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_strong
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable //팔로우 피드에 표시되는 포스트 카드 item.

fun PostCard(navController:NavHostController,
             data: FollowPost,
             scope: CoroutineScope,
             snackbarHostState: SnackbarHostState,
             bottomSheetState: ModalBottomSheetState,
             modalType: MutableState<Int>
) {

        val isButtonClickedLike = remember { mutableStateOf(false) } // 버튼 바
        val isButtonClickedSave = remember { mutableStateOf(false) }
        val order: PagerState = rememberPagerState() //뷰페이저, 인디케이터 페이지 상태 변수
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
                                Row(
                                        modifier = Modifier
                                                .padding(18.dp)
                                                .bounceClick {
                                                        navController.navigate(NAV_ROUTE_POSTCONTENT.WRITER.routeName)},//프로필
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        GlideImage(
                                                imageModel = data.writer.image,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                        .size(26.dp)
                                                        .clip(CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(18.dp))
                                        Text(
                                                text = data.writer.name,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                        )
                                }

                                Column(modifier = Modifier.clickable {
                                        //인텐트로 정보 스크린에 넘겨주기
                                        val intent = Intent(mContext, PostContentActivity::class.java)
                                        intent.putExtra("post_data",data)
                                        mContext.startActivity(intent)
                                })
                                {       // 포스트 카드 이미지 슬라이드
                                        Box() {
                                                // 포스트 카드 이미지 슬라이드
                                                HorizontalPager(
                                                        count = data.image.size,
                                                        state = order,
                                                        modifier = Modifier
                                                                .fillMaxWidth()
                                                                .aspectRatio(1f)
                                                                .align(Alignment.TopCenter),
                                                ) { page ->
                                                        GlideImage(
                                                                imageModel = data.image[page],
                                                                contentDescription = null,
                                                                contentScale = ContentScale.Crop,
                                                                modifier = Modifier
                                                                        .fillMaxWidth()
                                                                        .aspectRatio(1f),
                                                        )

                                                }

                                                // 포스트 카드 이미지 슬라이드 인디케이터
                                                DotsIndicator(
                                                        modifier = Modifier
                                                                .fillMaxWidth()
                                                                .background(
                                                                        brush = Brush.verticalGradient(
                                                                                colors = listOf(
                                                                                        moduBlack.copy(
                                                                                                alpha = 0f
                                                                                        ),
                                                                                        moduBlack.copy(
                                                                                                alpha = 0.2f
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                                .align(Alignment.BottomCenter)
                                                                .padding(25.dp),
                                                        dotSize = 8,
                                                        dotPadding = 5,
                                                        totalDots = data.image.size,
                                                        selectedIndex = order.currentPage,
                                                        selectedColor = Color.White,
                                                        unSelectedColor = Color("#75FFFFFF".toColorInt())
                                                )
                                        }
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
                                                        Text(
                                                                data.title,
                                                                fontSize = 16.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color("#17291F".toColorInt())
                                                        )
                                                        Row(
                                                                Modifier.fillMaxWidth(),
                                                                horizontalArrangement = Arrangement.SpaceBetween
                                                        ) {

                                                                Text(
                                                                        text =  data.category.component1(),
                                                                        color = moduGray_strong
                                                                )
                                                                Text(
                                                                        data.time,
                                                                        fontSize = 12.sp,
                                                                        color = moduGray_strong
                                                                )
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
                                                       if (isButtonClickedLike.value) {
                                                               isButtonClickedLike.value = false
                                                               data.likesCount = data.likesCount + 1
                                                       }
                                                       else {
                                                               isButtonClickedLike.value = true
                                                               data.likesCount = data.likesCount - 1
                                                       }

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
                                               .bounceClick {
                                                       navController.navigate("${NAV_ROUTE_POSTCONTENT.COMMENT.routeName}/${data.boardId}")
                                               },
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
                                               Icon(modifier = Modifier.bounceClick {
                                                       modalType.value = modalReportPost
                                                       scope.launch {
                                                               bottomSheetState.animateTo(
                                                                       ModalBottomSheetValue.Expanded)
                                                       }
                                               },
                                                       painter = painterResource(id = R.drawable.ic_dot3_vertical),
                                                       contentDescription = "신고",
                                                       tint = moduBlack)


                               }

                }
        }

}

// 슬라이드 인디케이터 컴포넌트
@Composable
fun DotsIndicator(
        modifier: Modifier,
        dotSize: Int,
        dotPadding:Int,
        totalDots: Int,
        selectedIndex: Int,
        selectedColor: Color = moduGray_strong,
        unSelectedColor: Color,
){
        LazyRow(
                modifier = modifier
        , horizontalArrangement = Arrangement.Center
        , verticalAlignment = Alignment.Bottom

        ) {
                items(totalDots) { index ->
                        if (index == selectedIndex) {
                                Box(
                                        modifier = Modifier
                                                .size(dotSize.dp)
                                                .clip(CircleShape)
                                                .background(selectedColor)
                                )
                        } else {
                                Box(
                                        modifier = Modifier
                                                .size(dotSize.dp)
                                                .clip(CircleShape)
                                                .background(unSelectedColor)
                                )
                        }

                        if (index != totalDots - 1) {
                                Spacer(modifier = Modifier.padding(horizontal = dotPadding.dp))
                        }
                }
        }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PostPreview(){
        // 팔로우 스낵바 메세지 띄울 때 필요
        val navController =  rememberNavController()
        val scope = rememberCoroutineScope()
        // 팔로우 스낵바 메세지 상태 변수
        val snackbarHostState = remember { SnackbarHostState() }
        val bottomSheetState= rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden)//바텀 시트
        val followPost = followPosts[0]

      /*  PostCard(navController, data = followPost, scope =scope , snackbarHostState = snackbarHostState,bottomSheetState, re)*/

}

