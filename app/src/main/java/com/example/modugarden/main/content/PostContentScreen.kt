package com.example.modugarden.main.content

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Geocoder
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import com.example.modugarden.ApplicationClass
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.PostDTO
import com.example.modugarden.data.MapInfo
import com.example.modugarden.data.Report
import com.example.modugarden.main.follow.DotsIndicator
import com.example.modugarden.main.follow.moduBold
import com.example.modugarden.route.NAV_ROUTE_POSTCONTENT
import com.example.modugarden.ui.theme.FollowCard
import com.example.modugarden.ui.theme.PostHeartCard
import com.example.modugarden.ui.theme.PostSaveCard
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_normal
import com.example.modugarden.ui.theme.moduGray_strong
import com.example.modugarden.ui.theme.moduPoint
import com.example.modugarden.viewmodel.UserViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SuspiciousIndentation", "CoroutineCreationDuringComposition",
    "UnrememberedMutableState"
)
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun PostContentScreen(
    navController: NavHostController,
    board_id:Int,
    userViewModel: UserViewModel
) {
    val pagerState= rememberPagerState()//뷰페이저, 인디케이터 페이지 상태 변수
    val scrollState = rememberScrollState()//스크롤 상태 변수
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden)//바텀 시트
    val modalType = remember{ mutableStateOf(0) } // 신고 or 위치 모달 타입 정하는 변수

    var responseBody by remember { mutableStateOf(PostDTO.GetPostResponse()) }
    var likeNum =  remember{ mutableStateOf(0) }
    val activity = (LocalContext.current as? Activity)//액티비티 종료할 때 필요한 변수
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }// 팔로우 스낵바 메세지 상태 변수
    val context = LocalContext.current.applicationContext
    val followState = remember { mutableStateOf(false) }

    val userId =
        ApplicationClass.sharedPreferences.getInt(ApplicationClass.clientId, 0) //내 아이디

    RetrofitBuilder.postAPI
        .getPostContent(board_id)
        .enqueue(object : Callback<PostDTO.GetPostResponse> {
            override fun onResponse(
                call: Call<PostDTO.GetPostResponse>,
                response: Response<PostDTO.GetPostResponse>
            ) {
                val res = response.body()
                if (res != null) {
                    responseBody = res
                    likeNum.value = res.result?.like_num!!
                    followState.value = res.result.isFollowed
                    Log.d("post-activity-result", responseBody.result?.image.toString())
                }
                 else {
                    Toast.makeText(context, "데이터를 받지 못했어요", Toast.LENGTH_SHORT).show()
                    Log.d("follow-post-result", response.toString())
                }
            }

            override fun onFailure(call: Call<PostDTO.GetPostResponse>, t: Throwable) {
                Toast.makeText(context, "서버와 연결하지 못했어요", Toast.LENGTH_SHORT).show()

                Log.d("follow-curation", "실패")
            }

        })

    if (responseBody.result != null) {

        val post = responseBody.result
        val locinfo = post!!.image[pagerState.currentPage].location
        val loclength = post!!.image[pagerState.currentPage].location.length
        val locButtonState = remember {mutableStateOf(loclength == 0 || locinfo.contains(",").not()) }

        ModalBottomSheetLayout(
            sheetElevation = 0.dp,
            sheetBackgroundColor = Color.Transparent,
            sheetState = bottomSheetState,
            sheetContent = {
                if(modalType.value== modalReportType) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // 회색 선
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(5.dp)
                                    .alpha(0.4f)
                                    .background(moduGray_normal, RoundedCornerShape(30.dp))

                            )
                            Spacer(modifier = Modifier.size(30.dp))

                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 18.dp)
                            ) {
                                Text(text = "포스트 신고", style = moduBold, fontSize = 20.sp)

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 18.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_user),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .border(1.dp, moduGray_light, RoundedCornerShape(50.dp))
                                            .size(25.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.size(18.dp))
                                    Text(text = post!!.title, style = moduBold, fontSize = 14.sp)
                                }
                            }

                            // 구분선
                            Divider(
                                color = moduGray_light, modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                            )

                            // 신고 카테고리 리스트
                            LazyColumn(
                                modifier = Modifier
                                    .padding(horizontal = 18.dp)
                            ) {
                                itemsIndexed(
                                    listOf(
                                        Report.ABUSE,
                                        Report.TERROR,
                                        Report.SEXUAL,
                                        Report.FISHING,
                                        Report.INAPPROPRIATE
                                    )
                                ) { index, item ->
                                    ReportCategoryItem(
                                        report = item,
                                        mutableStateOf(board_id),
                                        modalType,scope,bottomSheetState)
                                }
                            }
                            Spacer(modifier = Modifier.size(18.dp))
                        }


                    }
                }
                else if(modalType.value == modalLocationType) {
                    if(loclength == 0 || locinfo.contains(",").not()){
                        Spacer(modifier = Modifier.size(10.dp))
                    }

                    else{
                    val location = remember { mutableStateOf("") }
                        location.value = locinfo.split(",")[0]
                    val address = remember { mutableStateOf("") }
                    val lat = remember { mutableStateOf(0.0) }
                        lat.value=locinfo.split(",")[1].toDouble()
                    val lng =  remember { mutableStateOf(0.0) }
                        lng.value=locinfo.split(",")[2].toDouble()

                    val geocoder = Geocoder(
                        LocalContext.current.applicationContext,
                        Locale.KOREA
                    )
                    val latlng = geocoder.getFromLocation(lat.value, lng.value, 1)?.get(0)?.getAddressLine(0)
                        if (latlng != null) {
                            val country = geocoder.getFromLocation(lat.value, lng.value, 1)?.get(0)?.countryName!!
                            latlng.replace(country,"")
                            address.value = latlng.replace(country,"")
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // 회색 선
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(5.dp)
                                    .alpha(0.4f)
                                    .background(moduGray_normal, RoundedCornerShape(30.dp))

                            )
                            Spacer(modifier = Modifier.size(30.dp))

                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 18.dp)
                            ) {
                                Text(text = "위치 태그", style = moduBold, fontSize = 20.sp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 18.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(moduGray_light)
                                            .size(40.dp),
                                        contentAlignment = Alignment.Center
                                    )
                                    {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_map_pin),
                                            contentDescription = "",
                                            modifier = Modifier
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(18.dp))
                                    Column(
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                    ) {
                                        Text(
                                            text = location.value,
                                            style = moduBold,
                                            fontSize = 14.sp,
                                        )
                                        Text(
                                            text = address.value,
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )

                                    }

                                }

                                Log.i("latlng",lat.toString())
                                //지도
                               Box(modifier=
                               Modifier
                                   .fillMaxWidth()
                                   .height(200.dp)
                                   .clip(RoundedCornerShape(10.dp))
                                   .border(1.dp, moduGray_light)
                                ) {
                                    GoogleMap(
                                        Modifier.fillMaxSize(),
                                        cameraPositionState = CameraPositionState( position = CameraPosition.fromLatLngZoom(LatLng(lat.value,lng.value), 20f))
                                    ){
                                        Marker(
                                            state = MarkerState(position = LatLng(lat.value,lng.value)),
                                            title = location.value,
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.size(18.dp))

                                //버튼
                                Row {
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .bounceClick {
                                                scope.launch {
                                                    bottomSheetState.hide()
                                                }
                                            },
                                        shape = RoundedCornerShape(10.dp),
                                        backgroundColor = moduGray_light,
                                        elevation = 0.dp
                                    ) {
                                        Text(
                                            text = "닫기",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = moduGray_strong,
                                            modifier = Modifier
                                                .padding(14.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    Spacer(modifier = Modifier.size(18.dp))
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .bounceClick {
                                                val map_data =
                                                    MapInfo(
                                                        location.value,
                                                        address.value,
                                                        lat.value,
                                                        lng.value
                                                    )
                                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                                    "map_data",
                                                    map_data
                                                )
                                                navController.navigate(NAV_ROUTE_POSTCONTENT.MAP.routeName)
                                            },
                                        shape = RoundedCornerShape(10.dp),
                                        backgroundColor = moduPoint,
                                        elevation = 0.dp
                                    ) {
                                        Text(
                                            text = "자세히보기",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = Color.White,
                                            modifier = Modifier
                                                .padding(14.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                        }


                    }
                }
                }
                else {
                    Card(
                        modifier = Modifier
                            .padding(10.dp),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(5.dp)
                                    .background(moduGray_normal, RoundedCornerShape(30.dp))
                                    .alpha(0.2f)
                            )

                            Spacer(modifier = Modifier.size(30.dp))

                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 18.dp)
                            ) {
                                Text(text = "댓글을 삭제할까요?", style = moduBold, fontSize = 20.sp)

                                Row(
                                    modifier = Modifier
                                        .padding(vertical = 30.dp)
                                ) {
                                    GlideImage(
                                        imageModel = post.user_profile_image,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .border(1.dp, moduGray_light, RoundedCornerShape(50.dp))
                                            .size(25.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(18.dp))
                                    Text(
                                       post.title,
                                        fontSize = 16.sp,
                                        color = moduBlack,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))


                                }
                                //버튼
                                Row {
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .bounceClick {
                                                scope.launch {
                                                    bottomSheetState.hide()
                                                }
                                            },
                                        shape = RoundedCornerShape(10.dp),
                                        backgroundColor = moduGray_light,
                                        elevation = 0.dp
                                    ) {
                                        Text(
                                            text = "취소",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = moduGray_strong,
                                            modifier = Modifier
                                                .padding(14.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    Spacer(modifier = Modifier.size(18.dp))
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .bounceClick {
                                                RetrofitBuilder.postAPI
                                                    .deletePost(post.id)
                                                    .enqueue(object :
                                                        Callback<PostDTO.DeletePostResponse> {
                                                        override fun onResponse(
                                                            call: Call<PostDTO.DeletePostResponse>,
                                                            response: Response<PostDTO.DeletePostResponse>
                                                        ) {
                                                            if (response.isSuccessful) Log.i(
                                                                "포스트 삭제",
                                                                "성공"
                                                            )
                                                            else Log.i("포스트 삭제", "실패")
                                                        }

                                                        override fun onFailure(
                                                            call: Call<PostDTO.DeletePostResponse>,
                                                            t: Throwable
                                                        ) {
                                                            Log.i("포스트 삭제", "서버 연결 실패")
                                                        }
                                                    })
                                                scope.launch {
                                                    bottomSheetState.hide()
                                                }

                                            },
                                        shape = RoundedCornerShape(10.dp),
                                        backgroundColor = Color(0xFFFF7272),
                                        elevation = 0.dp
                                    ) {
                                        Text(
                                            text = "삭제",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = Color.White,
                                            modifier = Modifier
                                                .padding(14.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }


                }
            }
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)) {
                Column {
                    // 포스트 카드 이미지 슬라이드
                    Box{

                        HorizontalPager(
                                modifier = Modifier.wrapContentSize(),
                                count = post!!.image.size,
                                state = pagerState,
                            ) { page ->
                                    GlideImage(
                                        imageModel = post!!.image[page].image,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(1f),
                                    )
                            }
                        
                        // 포스트 카드 이미지 슬라이드 인디케이터
                        if ( post!!.image.size!=1) {
                            DotsIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                moduBlack.copy(alpha = 0f),
                                                moduBlack.copy(alpha = 0.2f)
                                            )
                                        )
                                    )
                                    .padding(bottom = 30.dp),
                                dotSize = 8,
                                dotPadding = 5,
                                totalDots = post.image.size,
                                selectedIndex = pagerState.currentPage,
                                selectedColor = Color.White,
                                unSelectedColor = Color("#75FFFFFF".toColorInt())
                            )
                        }

                    }
                    // 포스트 작성자 영역
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                    ) {
                        Row(modifier = Modifier
                            .padding(25.dp, 18.dp)
                            .bounceClick {
                                userViewModel.setUserId(responseBody.result!!.user_id)
                                navController.navigate(NAV_ROUTE_POSTCONTENT.WRITER.routeName)
                                //  포스트 작성자 프로필로

                            }
                        )
                        {
                            // 작성자 프로필 사진
                            GlideImage(
                                imageModel = post!!.user_profile_image,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(45.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(18.dp))
                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)

                            ) {
                                // 작성자 아이디
                                Text(
                                    text = post.user_nickname,
                                    style = moduBold,
                                    fontSize = 14.sp,
                                )
                                // 작성자 카테고리
                                Text(
                                    text = post.category_category,
                                    fontSize = 12.sp,
                                    color = moduGray_strong
                                )
                            }
                            Column(
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterVertically),
                                horizontalAlignment = Alignment.End
                            ) {

                                FollowCard(
                                    id = post.user_id,
                                    modifier =Modifier ,
                                    snackBarAction = {
                                        scope.launch {
                                        if (followState.value) snackbarHostState.showSnackbar("${post.user_nickname} 님을 팔로우 했어요.")
                                        else snackbarHostState.showSnackbar("${post.user_nickname} 님을 언팔로우 했어요.")
                                    }},
                                    followState = followState,
                                    contentModifier =Modifier
                                        .padding(vertical = 6.dp, horizontal = 10.dp)
                                )

                            }

                        }
                    }
                    // 구분선
                    Divider(
                        color = moduGray_light, modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )
                    // 제목, 카테고리, 업로드 시간, 내용

                            VerticalPager(
                                modifier = Modifier
                                    .padding(18.dp)
                                    .weight(1f, false)
                                    .fillMaxHeight()
                                    .background(Color.White),
                                count = post.image.size,
                            state = pagerState)
                            {page ->
                                Column(Modifier.verticalScroll(scrollState)) {
                                    //제목
                                    Text(
                                            text = post!!.title,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = moduBlack,
                                        )
                                    //게시글 정보
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                text = post!!.category_category+" · ",
                                                fontSize = 14.sp,
                                                color = moduGray_strong
                                            )
                                            Text(text = timeToDate(post!!.created_Date) ,
                                                fontSize = 14.sp,
                                                color = moduGray_strong)
                                        }
                                    //내용
                                    Column(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 25.dp))
                                    {
                                        Text(text = post!!.image[page].content, fontSize = 16.sp)
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }

// 좋아요, 댓글, 스크랩, 더보기
                    Box(modifier = Modifier

                    ) {
                        // 구분선
                        Divider(
                            color = moduGray_light, modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )
                        //버튼들
                        Row(
                            modifier = Modifier
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 좋아요, 스크랩 버튼 상태 변수
                            val isButtonClickedLike = remember { mutableStateOf(false) }
                            val isButtonClickedSave = remember { mutableStateOf(false) }
                            // 좋아요
                            PostHeartCard(
                                boardId = board_id,
                                heartState = isButtonClickedLike,
                                modifier = Modifier.padding(end = 18.dp),
                                likeNum = likeNum)

                            Text(text = "${likeNum.value}"+"명", style = moduBold, fontSize = 14.sp)
                            Text(text = "이 좋아해요", color = moduBlack, fontSize = 14.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            if (locButtonState.value) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_location_line),
                                    contentDescription = "위치",
                                    tint = moduGray_normal
                                )
                            }
                            else{
                                Icon(
                                    modifier = Modifier.bounceClick {
                                        modalType.value = modalLocationType
                                        scope.launch {
                                            bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                        }
                                    },
                                    painter = painterResource(id = R.drawable.ic_location_line),
                                    contentDescription = "위치",
                                    tint = moduBlack
                                )
                            }
                            // 댓글
                            Icon(
                                modifier = Modifier
                                    .padding(horizontal = 18.dp)
                                    .bounceClick {
                                        navController.navigate("${NAV_ROUTE_POSTCONTENT.COMMENT.routeName}/${post.id}")
                                    },
                                painter = painterResource(id = R.drawable.ic_chat_line),
                                contentDescription = "댓글",
                                tint = moduBlack
                            )
                            // 스크랩
                            PostSaveCard(boardId = board_id,
                                modifier=Modifier,
                                saveState = isButtonClickedSave,
                                scope = scope,
                                snackbarHostState = snackbarHostState)
                            /* Icon(modifier =
                             Modifier
                                 .bounceClick {
                                     isButtonClickedSave.value = !isButtonClickedSave.value
                                 }
                                 .padding(
                                     if (isButtonClickedSave.value)
                                         1.75.dp
                                     else
                                         0.dp
                                 )
                                 ,
                                 painter = painterResource
                                     (id =
                                 if (isButtonClickedSave.value)
                                     R.drawable.ic_star_solid
                                 else
                                     R.drawable.ic_star_line
                                 ),
                                 contentDescription = "스크랩",
                                 tint = moduBlack
                             )*/

                        }
                    }

                /*        // 구분선
                        Divider(
                            color = moduGray_light,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )
                        // 상품 태그 영역
                        Column(modifier = Modifier
                            .background(Color.White)) {
                            Row(
                                modifier = Modifier
                                    .padding(18.dp, 18.dp, 18.dp, 0.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            )
                            {
                                Text(text = "위치 태그", style = moduBold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.size(10.dp))
                                // 상품 태그 갯수
                                Text(text = "1", color = moduGray_strong, fontSize = 16.sp)
                                Spacer(modifier = Modifier.weight(1f))
                                // 상품 더보기 아이콘
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_chevron_right),
                                    contentDescription = "상품 더보기 아이콘",
                                    tint = moduBlack
                                )

                            }
                            Column {
                                Tagitem(modalType,scope,bottomSheetState)
                            }

                        }

*/
                }


                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)) {
                    // 뒤로 가기 버튼
                    Icon(
                        modifier = Modifier
                            .bounceClick { activity?.finish() },
                        painter = painterResource(id = R.drawable.ic_arrow_left_bold),
                        contentDescription = "뒤로가기",
                        tint = Color.White
                    )
                    // 메뉴 버튼
                    Icon(
                        modifier = Modifier
                            .bounceClick {
                                //버튼 클릭하면 바텀 모달 상태 변수 바뀜
                                if(post.user_id==userId) modalType.value = modalDeleteType
                                else modalType.value = modalReportType
                                scope.launch {
                                    bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                }},
                        painter = painterResource(id = R.drawable.ic_dot3_vertical),
                        contentDescription = "뒤로가기",
                        tint = Color.White
                    )
                }



                // 커스텀한 알림
                SnackbarHost(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(18.dp),
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
        }



    }
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Tagitem(modalType:MutableState<Int>,
            scope: CoroutineScope,
            bottomSheetState:ModalBottomSheetState){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
            .bounceClick {
                modalType.value = modalLocationType
                scope.launch {
                    bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                }

            })
        {
            // 상품 1 사진
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(0.5.dp, Color(0xFFCCCCCC), CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(18.dp))
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = "Location", style = moduBold, fontSize = 12.sp,)
                Text(text = "adress", fontSize = 14.sp, color = Color.Gray)
            }

        }

}}

/*
@RequiresApi(Build.VERSION_CODES.O)

@Composable
fun PostContentPreview(){
    val dana = User(
        image = "https://ifh.cc/g/jDDHBg.png".toUri(),
        name = "dana",
        category = listOf(""),
        follower = 1,
        following = 1,
        state = false,
        post = null,
        curation = null
    )

    PostContentScreen(navController = rememberNavController(), post = followPosts[0], userViewModel = viewModel())

}*/
