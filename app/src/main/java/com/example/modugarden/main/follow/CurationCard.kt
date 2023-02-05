package com.example.modugarden.main.follow

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.R
import com.example.modugarden.api.dto.GetFollowFeedCurationContent
import com.example.modugarden.main.content.CurationContentActivity
import com.example.modugarden.main.content.modalReportCuration
import com.example.modugarden.main.content.timeFomatter
import com.example.modugarden.route.NAV_ROUTE_FOLLOW
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UserViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable //팔로우 피드에 표시되는 큐레이션 카드 item.
fun CurationCard(
    navController: NavHostController,
    data: GetFollowFeedCurationContent,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    bottomSheetState: ModalBottomSheetState,
    modalType: MutableState<Int>,
    modalTitle : MutableState<String>,
    userViewModel: UserViewModel,
) {
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
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideImage(
                    imageModel = data.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
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
            Column(modifier = Modifier
                .clickable {
                    val intent = Intent(mContext, CurationContentActivity::class.java)
                    intent.putExtra("curation_id",data.curation_id)

                    mContext.startActivity(intent)
                }
            )
            {// 큐레이션 썸네일
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f), contentAlignment = Alignment.BottomCenter
                ) {

                    GlideImage(
                        imageModel = data.image,
                        contentDescription = "썸네일",
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

                    // 외부 페이지 이동
                    // 이미지 하단 블러처리
                    GlideImage(
                        imageModel = data.image,
                        contentDescription = "썸네일",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .drawWithContent { //ContentDrawScope
                                clipRect(top = size.height / 1.11f) {
                                    this@drawWithContent.drawContent()
                                    drawRect(
                                        moduBlack,
                                        alpha = 0.4f, blendMode = BlendMode.DstIn
                                    )
                                }
                            }
                            .blur(10.dp)
                            .fillMaxWidth(),
                        requestOptions = {
                            RequestOptions()
                                .override(256,256)
                        },
                        loading = {
                            ShowProgressBar()
                        },

                    )
                    // 안내 문구 및 아이콘
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 18.dp),
                            text = "외부 페이지로 이동해요.",
                            color = moduGray_light,
                            fontSize = 12.sp
                        )
                        Icon(
                            modifier = Modifier.padding(end = 18.dp),
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = "외부 페이지 이동",
                            tint = moduGray_light
                        )
                    }
                }
                Box(modifier = Modifier
                    .background(Color.White)
                    .drawBehind {
                        val strokeWidth = 1 * density
                        val y = size.height - strokeWidth / 2

                        drawLine(
                            Color("#EBEEED".toColorInt()),
                            Offset(0f, y),
                            Offset(size.width, y),
                            strokeWidth
                        )
                    }
                ) {
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
                        Row() {
                            Text(
                                data.category_category,
                                fontSize = 12.sp,
                                color = Color("#75807A".toColorInt())
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text( timeFomatter(data.created_Date), fontSize = 12.sp, color = Color("#75807A".toColorInt()))
                        }

                    }

                }
            }
            // 버튼들 ( 좋아요, 스크랩, 신고 )
            val isButtonClickedLike = remember { mutableStateOf(false) }
            val isButtonClickedSave = remember { mutableStateOf(false)}
            Row(
                Modifier.padding(18.dp)) {
                // 좋아요
                CurationHeartCard(
                    curationId = data.curation_id,
                    modifier = Modifier.padding(end = 18.dp),
                    heartState = remember { mutableStateOf(data.liked) }
                )
                Log.i("liked", data.liked.toString())
//                Icon(modifier = Modifier
//                    .padding(end = 18.dp)
//                    .bounceClick {
//                        isButtonClickedLike.value = !isButtonClickedLike.value
//                        RetrofitBuilder.curationAPI
//                            .likeCuration(data.id)
//                            .enqueue(object :retrofit2.Callback<CurationLikeResponse>{
//                                override fun onResponse(
//                                    call: Call<CurationLikeResponse>,
//                                    response: Response<CurationLikeResponse>
//                                ) {
//                                    if(response.isSuccessful) {
//                                        val res = response.body()
//                                        if(res != null) {
//                                            Log.d("like-result", res.toString())
//                                        }
//                                    }
//                                    else {
//                                        Toast.makeText(mContext, "데이터를 받지 못했어요", Toast.LENGTH_SHORT).show()
//                                        Log.d("like-result", response.toString())
//                                    }
//
//                                }
//                                override fun onFailure(
//                                    call: Call<CurationLikeResponse>,
//                                    t: Throwable
//                                ) {
//                                    Toast.makeText(mContext, "서버와 연결하지 못했어요", Toast.LENGTH_SHORT).show()
//                                }
//                            })
//                    }
//                    ,painter = painterResource
//                        (id =
//                            if (isButtonClickedLike.value)
//                                R.drawable.ic_heart_solid
//                            else
//                                R.drawable.ic_heart_line
//                        ),
//                    contentDescription = "좋아요",
//                tint = if (isButtonClickedLike.value)
//                            Color(0xFFFF6767)
//                        else
//                            moduBlack
//                )
                // 스크랩
                CurationSaveCard(
                    curationId = data.curation_id,
                    modifier = Modifier,
                    saveState =  remember { mutableStateOf(data.saved) }
                )
//                Icon(modifier = Modifier
//                    .bounceClick {
//                        isButtonClickedSave.value = !isButtonClickedSave.value
//
//                        if (isButtonClickedSave.value){
//                            scope.launch {
//                                snackbarHostState.showSnackbar(
//                                    "게시물을 저장하였습니다.",
//                                    duration = SnackbarDuration.Short
//                                )
//                            }
//                        }
//                    }
//                    ,painter = painterResource
//                        (id =
//                    if (isButtonClickedSave.value)
//                        R.drawable.ic_star_solid
//                    else
//                        R.drawable.ic_star_line
//                    ),
//                    contentDescription = "스크랩",
//                    tint = moduBlack
//                )
                Spacer(modifier = Modifier.weight(1f))
                // 신고
                    Icon(modifier = Modifier.bounceClick {
                        modalType.value = modalReportCuration
                        modalTitle.value = data.title
                        scope.launch {
                        bottomSheetState.animateTo(
                            ModalBottomSheetValue.Expanded)

                    }},
                        painter = painterResource(id = R.drawable.ic_dot3_vertical),
                        contentDescription = "신고",
                        tint = moduBlack)


            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun CurationPreview(){
    // 팔로우 스낵바 메세지 띄울 때 필요
    val scope = rememberCoroutineScope()
    // 팔로우 스낵바 메세지 상태 변수
    val snackbarHostState = remember { SnackbarHostState() }
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)//바텀 시트

    /*CurationCard(data = ,scope,snackbarHostState,bottomSheetState)*/
}