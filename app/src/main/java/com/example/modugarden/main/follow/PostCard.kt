package com.example.modugarden.main.follow

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.ApplicationClass
import com.example.modugarden.R
import com.example.modugarden.api.dto.PostDTO
import com.example.modugarden.data.ReportInfo
import com.example.modugarden.data.followPosts
import com.example.modugarden.main.content.PostContentActivity
import com.example.modugarden.main.content.modalReportPost
import com.example.modugarden.main.content.timeFomatter
import com.example.modugarden.route.NAV_ROUTE_FOLLOW
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UserViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("CommitPrefEdits")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable //팔로우 피드에 표시되는 포스트 카드 item.

fun PostCard(
        navController: NavHostController,
        data: PostDTO.GetFollowFeedPostContent,
        scope: CoroutineScope,
        snackbarHostState: SnackbarHostState,
        bottomSheetState: ModalBottomSheetState,
        userViewModel: UserViewModel,
        modalType:MutableState<Int>,
        modalTitle:MutableState<String>,
        modalImage:MutableState<String>,
        modalId:MutableState<Int>
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
                                                        userViewModel.setUserId(data.user_id)
                                                        navController.navigate(NAV_ROUTE_FOLLOW.USERPROFILE.routeName) {
                                                        }
                                                },//프로필
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        GlideImage(
                                                imageModel = data.user_profile_image,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                        .size(26.dp)
                                                        .clip(CircleShape),
                                                requestOptions = {
                                                        RequestOptions()
                                                                .override(256,256)
                                                },
                                                loading = {
                                                        ShowProgressBar()
                                                },
                                        )
                                        Spacer(modifier = Modifier.width(18.dp))
                                        Text(
                                                text = data.user_nickname,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                        )
                                }

                                Column(modifier = Modifier.clickable {
                                        //인텐트로 정보 스크린에 넘겨주기
                                        val intent = Intent(mContext, PostContentActivity::class.java)
                                        intent.putExtra("run",true)
                                        intent.putExtra("board_id",data.board_id)
                                        mContext.startActivity(intent)
                                })
                                {       // 포스트 카드 이미지 슬라이드
                                        Box() {
                                                // 포스트 카드 이미지 슬라이드
                                                HorizontalPager(
                                                        count =data.image.size,
                                                        state = order,
                                                        modifier = Modifier
                                                                .fillMaxWidth()
                                                                .aspectRatio(1f)
                                                                .align(Alignment.TopCenter),
                                                ) { page ->
                                                        GlideImage(
                                                                imageModel =data.image[page].image,
                                                                contentDescription = null,
                                                                contentScale = ContentScale.Crop,
                                                                modifier = Modifier
                                                                        .fillMaxWidth()
                                                                        .aspectRatio(1f),
                                                                requestOptions = {
                                                                        RequestOptions()
                                                                                .override(700,700)
                                                                },
                                                                loading = {
                                                                        ShowProgressBar()
                                                                },
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
                                                                .padding(25.dp)
                                                        ,
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
                                                                        text =  data.category_category,
                                                                        color = moduGray_strong
                                                                )
                                                                Text(
                                                                        timeFomatter( data.created_Date),
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
                                       PostHeartCard(
                                               boardId = data.board_id,
                                               heartState = isButtonClickedLike,
                                               modifier = Modifier.padding(end = 18.dp),
                                               likeNum = null
                                       )
//
//                                       Icon(modifier = Modifier
//                                               .padding(end = 18.dp)
//                                               .bounceClick {
//                                                       if (isButtonClickedLike.value) {
//                                                               isButtonClickedLike.value = false
//                                                               data.liKeNum = data.liKeNum + 1
//                                                       }
//                                                       else {
//                                                               isButtonClickedLike.value = true
//                                                               data.liKeNum = data.liKeNum - 1
//                                                       }
//
//                                               }
//                                               ,painter = painterResource
//                                                       (id =
//                                               if (isButtonClickedLike.value)
//                                                       R.drawable.ic_heart_solid
//                                               else
//                                                       R.drawable.ic_heart_line
//                                               ),
//                                               contentDescription = "좋아요",
//                                               tint =
//                                                       if (isButtonClickedLike.value)
//                                                               Color(0xFFFF6767)
//                                                       else
//                                                               moduBlack
//
//                                       )
                                       // 댓글
                                       Icon(modifier = Modifier
                                               .padding(end = 18.dp)
                                               .bounceClick {
                                                       val intent = Intent(
                                                               mContext,
                                                               PostContentActivity::class.java
                                                       )
                                                       intent.putExtra("board_id", data.board_id)
                                                       intent.putExtra("run", false)
                                                       mContext.startActivity(intent)

                                               },
                                               painter = painterResource(id = R.drawable.ic_chat_line),
                                               contentDescription = "댓글",
                                               tint = moduBlack
                                       )
                                       // 스크랩
                                       PostSaveCard(
                                               boardId = data.board_id,
                                               modifier = Modifier
                                                       .padding(end = 18.dp),
                                               saveState =isButtonClickedSave ,
                                               scope =scope,
                                               snackbarHostState = snackbarHostState
                                       )
                                       Spacer(modifier = Modifier.weight(1f))
                                               Icon(modifier = Modifier.bounceClick {
                                                       modalTitle.value  = data.title
                                                       modalImage.value = data.user_profile_image
                                                       modalType.value = modalReportPost
                                                       modalId.value = data.board_id
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

