package com.example.modugarden.main.content

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Geocoder
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.ApplicationClass
import com.example.modugarden.BuildConfig
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.PostDTO
import com.example.modugarden.api.dto.ReportPostResponse
import com.example.modugarden.data.MapInfo
import com.example.modugarden.data.MapsDetailRes
import com.example.modugarden.data.Report
import com.example.modugarden.main.follow.moduBold
import com.example.modugarden.route.NAV_ROUTE_POSTCONTENT
import com.example.modugarden.ui.theme.DeleteModal
import com.example.modugarden.ui.theme.DotsIndicator
import com.example.modugarden.ui.theme.FollowCard
import com.example.modugarden.ui.theme.OneButtonSmallDialog
import com.example.modugarden.ui.theme.PostHeartCard
import com.example.modugarden.ui.theme.PostSaveCard
import com.example.modugarden.ui.theme.ReportModal
import com.example.modugarden.ui.theme.ShowProgressBar
import com.example.modugarden.ui.theme.ShowProgressBarV2
import com.example.modugarden.ui.theme.SmallDialog
import com.example.modugarden.ui.theme.SnackBar
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduErrorPoint
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_normal
import com.example.modugarden.ui.theme.moduGray_strong
import com.example.modugarden.ui.theme.moduPoint
import com.example.modugarden.viewmodel.UserViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SuspiciousIndentation", "CoroutineCreationDuringComposition",
    "UnrememberedMutableState"
)
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun PostContentScreen(
    navController: NavHostController,
    board_id: Int,
    userViewModel: UserViewModel,
) {
    val firstPagerState = rememberPagerState()
    val secondPagerState = rememberPagerState()
    val scrollState = rememberScrollState()//스크롤 상태 변수
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden)//바텀 시트
    val modalType = remember{ mutableStateOf(modalLocationType) }// 신고 or 위치 모달 타입 정하는 변수
    val reportCategory = remember{ mutableStateOf("") }
    val reportMessage = remember{ mutableStateOf("") }

    val isLoading = remember { mutableStateOf(false) }
    var responseBody by remember { mutableStateOf(PostDTO.GetPostResponse()) }
    var likeNum =  remember{ mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }// 팔로우 스낵바 메세지 상태 변수
    val context = LocalContext.current
    val followState = remember { mutableStateOf(false) }
    val userId =
        ApplicationClass.sharedPreferences.getInt(ApplicationClass.clientId, 0) //내 아이디
    val fcmTokenState = remember { mutableStateOf(listOf<String>()) }
    val reportDialogState = remember { mutableStateOf(false) }
    val messageDialogState = remember { mutableStateOf(false) }
    if (reportDialogState.value){
        SmallDialog(
            text = "정말 신고할까요?",
            text2 = "신고는 취소할 수 없습니다.",
            textColor = moduBlack,
            backgroundColor = Color.White,
            positiveButtonText = "신고",
            negativeButtonText = "취소",
            positiveButtonTextColor = Color.White,
            negativeButtonTextColor = moduBlack,
            positiveButtonColor = moduErrorPoint,
            negativeButtonColor = moduBackground,
            dialogState = reportDialogState,
            reportCategory=reportCategory.value,
            reportMessage = reportMessage
        ) {
                RetrofitBuilder.reportAPI
                    .reportPost(board_id, reportCategory.value)
                    .enqueue(object : Callback<ReportPostResponse> {
                        override fun onResponse(
                            call: Call<ReportPostResponse>,
                            response: Response<ReportPostResponse>
                        ) {
                            if (response.body()?.isSuccess == true) {
                                reportMessage.value = "소중한 의견을 주셔서 감사합니다!"
                            } else { // 또 신고 했으면 알려줌
                                reportMessage.value = response.body()!!.message
                            }
                        }

                        override fun onFailure(
                            call: Call<ReportPostResponse>,
                            t: Throwable
                        ) {
                            Log.i("게시글 신고", "서버 연결 실패")
                        }
                    }
                    )
            messageDialogState.value = true
        }
    }
    if(messageDialogState.value)
        OneButtonSmallDialog(
            text = reportMessage.value,
            textColor = moduBlack,
            backgroundColor = Color.White,
            buttonText = "확인",
            buttonTextColor = Color.White,
            buttonColor = moduPoint,
            dialogState = messageDialogState
        ){
            messageDialogState.value=false
        }
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
                    fcmTokenState.value=res.result.fcmTokens
                }
                 else {
                }
            }

            override fun onFailure(call: Call<PostDTO.GetPostResponse>, t: Throwable) {
                Log.d("follow-curation", "서버 연결 실패")
            }

        })

    if (responseBody.result != null) {
        val post = responseBody.result
        val locinfo = post!!.image[firstPagerState.currentPage].location
        val loclength = post!!.image[firstPagerState.currentPage].location.length
        val locButtonState = remember {mutableStateOf(loclength == 0) }

        ModalBottomSheetLayout(
            sheetElevation = 0.dp,
            sheetBackgroundColor = Color.Transparent,
            sheetState = bottomSheetState,
            sheetContent = {
                if (modalType.value == modalReportPost) {
                    ReportModal(
                        type = "포스트",
                        profileImage = post.user_profile_image,
                        title = post.title,
                        reportCategory = reportCategory,
                        reportDialogState = reportDialogState,
                        scope = scope,
                        bottomSheetState = bottomSheetState
                    )
                }
                if (modalType.value == modalLocationType) {
                    val location = remember { mutableStateOf("") }
                    location.value = locinfo.split("``")[0]
                    val address = remember { mutableStateOf("") }
                    val lat = remember { mutableStateOf(0.0) }
                    val lng = remember { mutableStateOf(0.0) }
                    val place_id = remember { mutableStateOf("") }
                    val photoRef:MutableState<String?> = remember { mutableStateOf(null) }
                    val photoURL:MutableState<String?>  = remember { mutableStateOf(null) }

                    if (locinfo.contains("``")) {
                        lat.value = locinfo.split("``")[1].toDouble()
                        lng.value = locinfo.split("``")[2].toDouble()
                        place_id.value = locinfo.split("``")[3]
                        RetrofitBuilder.postLocationPhotoAPI
                            .getPhotoReference(place_id.value)
                            .enqueue(object : Callback<MapsDetailRes>{
                                override fun onResponse(call: Call<MapsDetailRes>, response: Response<MapsDetailRes>) {
                                    if (response.isSuccessful){
                                        val res = response.body()?.result?.photos?.get(0)
                                        if(res!=null){
                                            photoRef.value = res.photo_reference
                                            Log.i("사진",photoRef.value.toString())
                                        }
                                        else
                                            photoRef.value = null
                                    }
                                }

                                override fun onFailure(call: Call<MapsDetailRes>, t: Throwable) {
                                }
                            })

                        if(photoRef.value!=null) {
                            photoURL.value =
                                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoRef!!.value + "&key=" + BuildConfig.google_maps_key
                        }
                        Locale.setDefault(Locale.KOREAN)
                        val geocoder = Geocoder(
                            LocalContext.current.applicationContext,
                            Locale.KOREA
                        )
                        val latlng = geocoder.getFromLocation(lat.value, lng.value, 1,)?.get(0)
                            ?.getAddressLine(0)
                        if (latlng != null) {
                            val country = geocoder.getFromLocation(lat.value, lng.value, 1)
                                ?.get(0)?.countryName
                            address.value = latlng.replace("$country ", "")
                        }
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
                                        if (locinfo.contains("``")) {
                                            Text(
                                                text = address.value,
                                                fontSize = 12.sp,
                                                color = moduGray_normal
                                            )
                                        }

                                    }

                                }

                                if (locinfo.contains("``")) {
                                    //지도
                                    Box(
                                        modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .border(1.dp, moduGray_light)
                                    ) {
                                        GoogleMap(
                                            Modifier.fillMaxSize(),
                                            cameraPositionState = CameraPositionState(
                                                position = CameraPosition.fromLatLngZoom(
                                                    LatLng(lat.value, lng.value),
                                                    20f
                                                )
                                            )
                                        ) {
                                            Marker(
                                                state = MarkerState(
                                                    position = LatLng(
                                                        lat.value,
                                                        lng.value
                                                    )
                                                ),
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
                                                            lng.value,
                                                            photoURL.value
                                                        )
                                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                                        "map_data",
                                                        map_data
                                                    )
                                                    navController.navigate(NAV_ROUTE_POSTCONTENT.MAP.routeName)
                                                    scope.launch {
                                                        bottomSheetState.hide()
                                                    }
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
                if(modalType.value== modalDeletePost) {
                    DeleteModal(
                        type = "포스트",
                        profileImage = post.user_profile_image,
                        title = post.title,
                        scope = scope,
                        bottomSheetState = bottomSheetState,
                        deleteAction = {
                            isLoading.value = true
                            RetrofitBuilder.postAPI
                                .deletePost(post.id)
                                .enqueue(object :
                                    Callback<PostDTO.DeletePostResponse> {
                                    override fun onResponse(
                                        call: Call<PostDTO.DeletePostResponse>,
                                        response: Response<PostDTO.DeletePostResponse>
                                    ) {
                                        if (response.body()?.isSuccess == true) {
                                            isLoading.value = false
                                            (context as Activity).finish()
                                        } else {
                                            TODO()
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<PostDTO.DeletePostResponse>,
                                        t: Throwable
                                    ) {
                                        TODO()
                                    }
                                })
                            scope.launch {
                                bottomSheetState.hide()
                            }
                        }
                    )

                }
            } ) {

            BackHandler(enabled = bottomSheetState.isVisible) {
                scope.launch {
                    bottomSheetState.hide()
                }
            }
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)) {
                Column {
                    val count = post!!.image.size

                    // 포스트 카드 이미지 슬라이드
                    Box{

                        val scrollingFollowingPair by remember {
                            derivedStateOf {
                                if (firstPagerState.isScrollInProgress) {
                                    firstPagerState to secondPagerState
                                } else if (secondPagerState.isScrollInProgress) {
                                    secondPagerState to firstPagerState
                                } else null
                            }
                        }
                        LaunchedEffect(scrollingFollowingPair) {
                            val (scrollingState, followingState) = scrollingFollowingPair ?: return@LaunchedEffect
                            snapshotFlow { scrollingState.currentPage + scrollingState.currentPageOffset }
                                .collect { pagePart ->
                                    val divideAndRemainder = BigDecimal.valueOf(pagePart.toDouble())
                                        .divideAndRemainder(BigDecimal.ONE)

                                    followingState.scrollToPage(
                                        divideAndRemainder[0].toInt(),
                                        divideAndRemainder[1].toFloat(),
                                    )
                                }
                        }


                        HorizontalPager(
                                modifier = Modifier.fillMaxWidth(),
                                count = count,
                                state = firstPagerState,
                            ) { page ->
                                    GlideImage(
                                        imageModel = post!!.image[page].image,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(1f),
                                        failure = {
                                            Text(text = "image request failed.")
                                        },
                                        requestOptions = {
                                            RequestOptions()
                                                .override(720, 720)
                                        },
                                        loading = {
                                            ShowProgressBar()
                                        }
                                    )
                            }
                        
                        // 포스트 카드 이미지 슬라이드 인디케이터
                        if ( post!!.image.size>1) {
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
                                selectedIndex = firstPagerState.currentPage,
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
                            .padding(25.dp, 15.dp)
                            .bounceClick {
                                userViewModel.setUserId(responseBody.result!!.user_id)
                                navController.navigate(NAV_ROUTE_POSTCONTENT.WRITER.routeName)
                                //  포스트 작성자 프로필로
                            }
                        )
                        {
                            // 작성자 프로필 사진
                            GlideImage(
                                imageModel = post!!.user_profile_image?:R.drawable.ic_default_profile,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(45.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                requestOptions = {
                                    RequestOptions()
                                        .override(45,45)
                                },
                                loading = {
                                    ShowProgressBar()
                                }
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
                                            val snackBar = scope.launch {
                                            if (followState.value) snackbarHostState.showSnackbar("${post.user_nickname} 님을 팔로우 했어요.", duration = SnackbarDuration.Indefinite)
                                            else snackbarHostState.showSnackbar("${post.user_nickname} 님을 언팔로우 했어요.", duration = SnackbarDuration.Indefinite)

                                        }
                                            delay(900)
                                            snackBar.cancel()} },
                                    followState = followState,
                                    contentModifier =Modifier
                                        .padding(vertical = 6.dp, horizontal = 10.dp),
                                    fcmTokenState = fcmTokenState
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
                            HorizontalPager(
                                modifier = Modifier
                                    .padding(horizontal = 18.dp)
                                    .padding(top = 18.dp)
                                    .weight(1f)
                                    .background(Color.White)
                                    .verticalScroll(scrollState),
                                verticalAlignment = Alignment.Top,
                                count = count,
                                state = secondPagerState)
                            {page ->
                                Column(
                                    Modifier

                                        ) {
                                    //제목, 정보
                                    if (page==0){//제목
                                        Text(
                                            text = post!!.title,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = moduBlack,
                                        )
                                        //게시글 정보
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                text = post!!.category_category + " · ",
                                                fontSize = 14.sp,
                                                color = moduGray_strong
                                            )
                                            Text(
                                                text = timeToDate(post!!.created_Date),
                                                fontSize = 14.sp,
                                                color = moduGray_strong
                                            )
                                        }
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 18.dp),
                                            text = post!!.image[page].content, fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.size(18.dp))
                                    }

                                    else{//내용
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            text = post!!.image[page].content, fontSize = 16.sp
                                        )
                                    }

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
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "fcm_token",
                                            fcmTokenState
                                        )
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

                        }
                    }

                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)) {
                    // 뒤로 가기 버튼
                    Icon(
                        modifier = Modifier
                            .bounceClick { (context as Activity).finish()},
                        painter = painterResource(id = R.drawable.ic_arrow_left_bold),
                        contentDescription = "뒤로가기",
                        tint = Color.White
                    )
                    // 메뉴 버튼
                    Icon(
                        modifier = Modifier
                            .bounceClick {
                                //버튼 클릭하면 바텀 모달 상태 변수 바뀜
                                if(post.user_id==userId) modalType.value = modalDeletePost
                                else modalType.value = modalReportPost
                                scope.launch {
                                    bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                }},
                        painter = painterResource(id = R.drawable.ic_dot3_vertical),
                        contentDescription = "뒤로가기",
                        tint = Color.White
                    )
                }
                if (isLoading.value) ShowProgressBarV2()

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(30.dp)
                ) {
                    SnackBar(snackbarHostState = snackbarHostState)
                }
            }
        }



    }
}

