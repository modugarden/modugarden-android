package com.example.modugarden.main.follow

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.ApplicationClass
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.PostDTO
import com.example.modugarden.data.followPosts
import com.example.modugarden.main.content.PostContentActivity
import com.example.modugarden.main.content.modalDeletePost
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        modalType: MutableState<Int>,
        modalTitle: MutableState<String>,
        modalImage: MutableState<String?>,
        modalId: MutableState<Int>,
        feedLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
        fcmTokens: ArrayList<String>

) {
        val isButtonClickedLike = remember { mutableStateOf(false) } // 버튼 바
        val isButtonClickedSave = remember { mutableStateOf(false) }
        val order: PagerState = rememberPagerState() //뷰페이저, 인디케이터 페이지 상태 변수
        val mContext = LocalContext.current
        val userId =
                ApplicationClass.sharedPreferences.getInt(ApplicationClass.clientId, 0) //내 아이디


        feedLauncher.let {
                RetrofitBuilder.postAPI.getPostLikeState(data.board_id)
                        .enqueue(  object : Callback<PostDTO.GetPostLikeStateResponse> {
                                override fun onResponse(
                                        call: Call<PostDTO.GetPostLikeStateResponse>,
                                        response: Response<PostDTO.GetPostLikeStateResponse>
                                ) {
                                        isButtonClickedLike.value = response.body()?.result?.check ?: true
                                }

                                override fun onFailure(
                                        call: Call<PostDTO.GetPostLikeStateResponse>,
                                        t: Throwable
                                ) {

                                }

                        })

                RetrofitBuilder.postAPI.getPostSaveState(data.board_id)
                        .enqueue(  object : Callback<PostDTO.GetPostSaveStateResponse> {
                                override fun onResponse(
                                        call: Call<PostDTO.GetPostSaveStateResponse>,
                                        response: Response<PostDTO.GetPostSaveStateResponse>
                                ) {
                                        isButtonClickedSave.value = response.body()?.result?.check ?: true
                                }

                                override fun onFailure(
                                        call: Call<PostDTO.GetPostSaveStateResponse>,
                                        t: Throwable
                                ) {

                                }

                        })

        }
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
                                                        Log.d(
                                                                "postCardUserId",
                                                                data.user_id.toString()
                                                        )
                                                        navController.navigate(NAV_ROUTE_FOLLOW.USERPROFILE.routeName) {
                                                        }
                                                },//프로필
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        GlideImage(
                                                imageModel =
                                                data.user_profile_image ?: R.drawable.ic_default_profile,
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
                        // 구분선
                        Divider(
                                color = moduGray_light, modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                        )
                                Column(modifier = Modifier.clickable {
                                        //인텐트로 정보 스크린에 넘겨주기
                                        val intent = Intent(mContext, PostContentActivity::class.java)

                                        val bundle = Bundle()

                                        bundle.putInt("board_id", data.board_id)
                                        bundle.putBoolean("run", true)
                                        bundle.putStringArrayList("fcm_tokens",fcmTokens)

                                        intent.putExtras(bundle)

                                        val pendIntent: PendingIntent
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                                pendIntent = PendingIntent
                                                        .getActivity(
                                                                mContext, 0,
                                                                intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent . FLAG_MUTABLE
                                                        )

                                        } else {
                                                pendIntent = PendingIntent
                                                        .getActivity(
                                                                mContext, 0,
                                                                intent, PendingIntent.FLAG_UPDATE_CURRENT
                                                        )
                                        }

                                        feedLauncher.launch(IntentSenderRequest
                                                .Builder(pendIntent)
                                                .build())
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
                                                if(data.image.size!=1) {// 포스트 카드 이미지 슬라이드 인디케이터
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

                                        }
                                        Column(modifier = Modifier
                                                .background(Color.White)
                                                // 구분선

                                        ) {
                                                // 구분선
                                                Divider(
                                                        color = moduGray_light,
                                                        modifier = Modifier
                                                                .fillMaxWidth()
                                                                .height(1.dp)
                                                )
                                                // 포스트 정보 (제목, 카테고리, 업로드 시간 )
                                                Column(
                                                        modifier = Modifier
                                                                .padding(18.dp)
                                                ) {

                                                        Row(Modifier.fillMaxWidth(1f)){
                                                                Text(
                                                                        data.title,
                                                                        fontSize = 16.sp,
                                                                        fontWeight = FontWeight.Bold,
                                                                        color = Color("#17291F".toColorInt()),
                                                                        maxLines = 1,
                                                                        overflow = TextOverflow.Ellipsis,
                                                                        modifier = Modifier.weight(
                                                                                0.9f
                                                                        )
                                                                )
                                                                Spacer(modifier = Modifier.weight(0.1f))
                                                        }
                                                        Row(
                                                                Modifier.fillMaxWidth(),
                                                                horizontalArrangement = Arrangement.SpaceBetween
                                                        ) {

                                                                Text(
                                                                        text =  data.category_category,
                                                                        color = moduGray_strong
                                                                )
                                                                val value = remember{ mutableStateOf("") }
                                                                Text(
                                                                        text = timeFomatter( data.created_Date,value),
                                                                        fontSize = 12.sp,
                                                                        color = moduGray_strong
                                                                )
                                                        }

                                                }
                                                // 구분선
                                                Divider(
                                                        color = moduGray_light,
                                                        modifier = Modifier
                                                                .fillMaxWidth()
                                                                .height(1.dp)
                                                )

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
//
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
                                                       intent.putExtra("fcm_tokens",fcmTokens)
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
                                                       if (data.user_id==userId) modalType.value = modalDeletePost
                                                       else modalType.value = modalReportPost
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

