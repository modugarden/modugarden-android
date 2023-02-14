package com.example.modugarden.main.profile

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.ApplicationClass
import com.example.modugarden.ApplicationClass.Companion.categorySetting
import com.example.modugarden.ApplicationClass.Companion.clientId
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import com.example.modugarden.R
import com.example.modugarden.api.AuthCallBack
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.*
import com.example.modugarden.main.content.CurationContentActivity
import com.example.modugarden.main.content.PostContentActivity
import com.example.modugarden.main.content.timeToDate
import com.example.modugarden.main.profile.follow.ProfileFollowScreen
import com.example.modugarden.main.settings.SettingsActivity
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.RefreshViewModel
import com.example.modugarden.viewmodel.UserViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun ProfileScreenV4(
    userId: Int = 0,
    upperNavHostController: NavHostController,
    navController: NavController = NavController(LocalContext.current),
    userViewModel: UserViewModel
) {
    val myId = sharedPreferences.getInt(clientId, 0)

    val focusManager = LocalFocusManager.current
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val data = remember { mutableStateOf( UserInfoResResult() ) }
    val followState = remember { mutableStateOf(false) }
    val loadingState = remember { mutableStateOf(true) }
    val refreshViewModel = RefreshViewModel()
    val isRefreshing by refreshViewModel.isRefreshing.collectAsState()
    val blockDialogState = remember { mutableStateOf(false) }
    val alreadyBlockDialogState = remember { mutableStateOf(false) }
    val reportDialogState = remember { mutableStateOf(false) }
    val blockState = remember { mutableStateOf(false) }
    val blockedState = remember { mutableStateOf(false) }
    val fcmTokenState = remember { mutableStateOf<List<String>>(listOf())}

    val postList = remember {
        mutableStateOf<List<PostDTO.GetUserPostResponseContent>?>(
            listOf()
        )
    }

    val curationList = remember {
        mutableStateOf<List<GetUserCurationsResponseContent>?>(
            listOf()
        )
    }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.StartIntentSenderForResult()) {
        loadingState.value = true
        RetrofitBuilder.userAPI.readUserInfo(userId)
            .enqueue(object : AuthCallBack<UserInfoRes>(context, "설정 -> 프로필 복귀 시 api") {
                override fun onResponse(call: Call<UserInfoRes>, response: Response<UserInfoRes>) {
                    super.onResponse(call, response)
                    data.value = response.body()?.result!!
                    followState.value = response.body()!!.result.follow
                    blockState.value = response.body()!!.result.block
                    blockedState.value = response.body()!!.result.blocked
                    loadingState.value = false
                }
            })
    }

    val postLauncher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.StartIntentSenderForResult()) {
        loadingState.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val postResponse = RetrofitBuilder.postAPI.getUserPost(userId).execute()
            postList.value = postResponse.body()?.content
            loadingState.value = false
        }
    }

    val curationLauncher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.StartIntentSenderForResult()) {
        loadingState.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val curationResponse = RetrofitBuilder.curationAPI.getUserCuration(userId).execute()
            curationList.value = curationResponse.body()?.content
            loadingState.value = false
        }
    }

    RetrofitBuilder.userAPI.readUserInfo(userId)
        .enqueue(object : AuthCallBack<UserInfoRes>(context, "유저 정보 불러오기") {
            override fun onResponse(call: Call<UserInfoRes>, response: Response<UserInfoRes>) {
                super.onResponse(call, response)
                data.value = response.body()?.result!!
                followState.value = response.body()!!.result.follow
                blockState.value = response.body()!!.result.block
                blockedState.value = response.body()!!.result.blocked
                fcmTokenState.value = response.body()!!.result.fcmTokens
                loadingState.value = false
                sharedPreferences.edit().putStringSet(categorySetting, response.body()?.result?.categories!!.toSet()).apply()
            }
        })

    ModalBottomSheet(
        title = "",
        bottomSheetState = bottomSheetState,
        sheetScreen = {
            ModalBottomSheetItem(
                text = "신고",
                icon = R.drawable.ic_report,
                trailing = true,
                modifier = Modifier.bounceClick {
                    scope.launch {
                        reportDialogState.value = true
                        bottomSheetState.hide()
                    }
                })
            ModalBottomSheetItem(
                text = "차단",
                icon = R.drawable.ic_block,
                trailing = true,
                modifier = Modifier.bounceClick {
                    scope.launch {
                        if(blockState.value)
                            alreadyBlockDialogState.value = true
                        else
                            blockDialogState.value = true
                        bottomSheetState.hide()
                    }
                })
        },
        uiScreen = {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White),
                scaffoldState = scaffoldState,
                snackbarHost = {
                    ScaffoldSnackBar(
                        snackbarHostState = it
                    )
                }
            ) { it ->
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                    onRefresh = {
                        refreshViewModel.refresh()
                        refreshViewModel.getUserInfo(data, context, loadingState)
                        followState.value = data.value.follow
                    }) {

                    if(alreadyBlockDialogState.value)
                        OneButtonSmallDialog(
                            text = "이미 차단한 유저예요.",
                            textColor = moduBlack,
                            backgroundColor = Color.White,
                            buttonText = "확인",
                            buttonTextColor = Color.White,
                            buttonColor = moduPoint,
                            dialogState = alreadyBlockDialogState
                        )

                    if(blockDialogState.value)
                        SmallDialog(
                            text = "${data.value.nickname}님을\n차단하시겠습니까?",
                            textColor = moduBlack,
                            backgroundColor = Color.White,
                            positiveButtonText = "차단",
                            negativeButtonText = "취소",
                            positiveButtonTextColor = Color.White,
                            negativeButtonTextColor = moduBlack,
                            positiveButtonColor = moduErrorPoint,
                            negativeButtonColor = moduBackground,
                            dialogState = blockDialogState
                        ) {
                            CoroutineScope(Dispatchers.IO).launch {
                                val blockResponse = RetrofitBuilder.blockAPI.blockUser(userId).execute()
                                blockState.value = true
                                Log.d("onResponse", blockResponse.toString())
                            }
                        }

                    if(reportDialogState.value)
                        SmallDialog(
                            text = "${data.value.nickname}님을\n신고하시겠습니까?",
                            textColor = moduBlack,
                            backgroundColor = Color.White,
                            positiveButtonText = "신고",
                            negativeButtonText = "취소",
                            positiveButtonTextColor = Color.White,
                            negativeButtonTextColor = moduBlack,
                            positiveButtonColor = moduErrorPoint,
                            negativeButtonColor = moduBackground,
                            dialogState = reportDialogState
                        ) {
                            CoroutineScope(Dispatchers.IO).launch {
                                val reportResponse = RetrofitBuilder.reportAPI.reportUser(userId).execute()
                                Log.d("onResponse", reportResponse.toString())
                            }
                        }

                    BoxWithConstraints {
                        val screenHeight = maxHeight
                        val scrollState = rememberScrollState()
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(it)
                                .background(color = Color.White)
                                .verticalScroll(state = scrollState)
                        ) {
                            Spacer(modifier = Modifier.size(18.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .padding(horizontal = 18.dp)
                            ) {
                                if (!userViewModel.getOnBNB()) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_arrow_left_bold),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .bounceClick {
                                                upperNavHostController.navigateUp()
                                            }
                                            .size(24.dp),
                                        tint = moduBlack
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                if (myId != data.value.id) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ellipsis_vertical),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .bounceClick {
                                                focusManager.clearFocus()
                                                scope.launch {
                                                    bottomSheetState.show()
                                                }
                                            }
                                            .size(24.dp)
                                    )
                                }

                            }
                            Row(
                                modifier = Modifier
                                    .align(CenterHorizontally)
                                    .wrapContentSize()
                            ) {
                                Spacer(modifier = Modifier.width(42.dp))
                                Text(
                                    text = data.value.nickname,
                                    style = TextStyle(
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = moduBlack
                                    )
                                )
                                Spacer(modifier = Modifier.width(18.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.cog_8_tooth),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .align(CenterVertically)
                                        .size(24.dp)
                                        .bounceClick {
                                            if (myId == data.value.id) {
                                                val intent =
                                                    Intent(context, SettingsActivity::class.java)

                                                val pendIntent: PendingIntent
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                                    pendIntent = PendingIntent
                                                        .getActivity(
                                                            context,
                                                            0,
                                                            intent,
                                                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                                                        )

                                                } else {
                                                    pendIntent = PendingIntent
                                                        .getActivity(
                                                            context,
                                                            0,
                                                            intent,
                                                            PendingIntent.FLAG_UPDATE_CURRENT
                                                        )
                                                }

                                                launcher.launch(
                                                    IntentSenderRequest
                                                        .Builder(pendIntent)
                                                        .build()
                                                )
                                            }
                                        },
                                    tint =
                                    if (myId == data.value.id)
                                        moduGray_normal
                                    else
                                        Color.Transparent
                                )
                            }
                            Spacer(modifier = Modifier.size(10.dp))
                            Text(
                                text = data.value.categories.joinToString(", ", "", ""),
                                style = TextStyle(
                                    color = moduGray_normal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                ),
                                modifier = Modifier
                                    .align(CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.size(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.95f)
                                    .padding(horizontal = 42.dp)
                                    .align(CenterHorizontally)
                                    .height(120.dp)
                            ) {
                                Card(
                                    modifier = Modifier
                                        .align(CenterStart)
                                        .wrapContentSize(),
                                    elevation = 0.dp
                                ) {
                                    Column {
                                        Text(
                                            text = ((postList.value?.size ?: 0) + (curationList.value?.size ?: 0)).toString(),
                                            style = TextStyle(
                                                color = moduBlack,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 20.sp
                                            ),
                                            modifier = Modifier.align(CenterHorizontally)
                                        )
                                        Text(
                                            text = "게시물",
                                            style = TextStyle(
                                                color = moduGray_normal,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .align(Center)
                                ) {
                                    GlideImage(
                                        imageModel =
                                        data.value.profileImage ?: R.drawable.ic_default_profile,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .aspectRatio(1f)
                                            .clip(CircleShape),
                                        loading = {
                                            ShowProgressBar()
                                        },
                                        // shows an error text if fail to load an image.
                                        failure = {
                                            Text(text = "image request failed.")
                                        },
                                        requestOptions = {
                                            RequestOptions()
                                                .override(256, 256)
                                        }
                                    )
                                    if(data.value.userAuthority == UserAuthority.ROLE_CURATOR.name)
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_user_state),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .clip(CircleShape)
                                                .padding(8.dp)
                                        )
                                }
                                Card(
                                    modifier = Modifier
                                        .bounceClick {
                                            navController.navigate(ProfileFollowScreen.Follow.name)
                                        }
                                        .align(CenterEnd)
                                        .wrapContentSize(),
                                    elevation = 0.dp
                                ) {
                                    Column {
                                        Text(
                                            text = data.value.followerCount.toString(),
                                            style = TextStyle(
                                                color = moduBlack,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 20.sp
                                            ),
                                            modifier = Modifier.align(CenterHorizontally)
                                        )
                                        Text(
                                            text = "팔로워",
                                            style = TextStyle(
                                                color = moduGray_normal,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.size(30.dp))

                            if (myId != userId && !blockedState.value) {
                                FollowCard(
                                    id = userId,
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(36.dp)
                                        .align(CenterHorizontally),
                                    snackBarAction = {
                                        scope.launch {
                                            if (followState.value) scaffoldState.snackbarHostState.showSnackbar(
                                                "${data.value.nickname} 님을 팔로우 했어요."
                                            )
                                            else scaffoldState.snackbarHostState.showSnackbar("${data.value.nickname} 님을 언팔로우 했어요.")
                                        }
                                    },
                                    followState = followState,
                                    blockState = blockState,
                                    contentModifier = Modifier
                                        .padding(vertical = 6.dp, horizontal = 10.dp),
                                    unBlockSnackBarAction = {
                                        scope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar("${data.value.nickname} 님을 차단해제했어요.")
                                        }
                                    },
                                    fcmTokenState = fcmTokenState
                                )
                            }

                            RetrofitBuilder.postAPI.getUserPost(userId)
                                .enqueue(object :
                                    AuthCallBack<PostDTO.GetUserPostResponse>(context, "성공!") {
                                    override fun onResponse(
                                        call: Call<PostDTO.GetUserPostResponse>,
                                        response: Response<PostDTO.GetUserPostResponse>
                                    ) {
                                        super.onResponse(call, response)
                                        if (response.body()?.content != null)
                                            postList.value = response.body()?.content
                                    }
                                })

                            if (data.value.postCount > 0) {
                                RetrofitBuilder.curationAPI.getUserCuration(userId)
                                    .enqueue(object :
                                        AuthCallBack<GetUserCurationsResponse>(context, "성공!") {
                                        override fun onResponse(
                                            call: Call<GetUserCurationsResponse>,
                                            response: Response<GetUserCurationsResponse>
                                        ) {
                                            super.onResponse(call, response)
                                            if (response.body()?.content != null)
                                                curationList.value = response.body()?.content
                                        }
                                    })
                            }
                            Spacer(modifier = Modifier.size(12.dp)) // 290

                            Column(modifier = Modifier.height(screenHeight)) {
                                Spacer(modifier = Modifier.size(18.dp))
                                Row(
                                    Modifier
                                        .padding(horizontal = 18.dp)
                                ) {
                                    Text(text = "포스트",
                                        fontSize = 20.sp,
                                        color =
                                        if (pagerState.currentPage == 0) moduBlack
                                        else moduGray_normal,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .bounceClick {
                                                scope.launch {
                                                    pagerState.animateScrollToPage(0)
                                                }
                                            })
                                    Spacer(Modifier.size(20.dp))
                                    Text(text = "큐레이션",
                                        fontSize = 20.sp,
                                        color =
                                        if (pagerState.currentPage == 1) moduBlack
                                        else moduGray_normal,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.bounceClick {
                                            scope.launch {
                                                pagerState.animateScrollToPage(1)
                                            }
                                        })
                                    Spacer(modifier = Modifier.weight(1f))
                                    if (myId == data.value.id) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_profile_saved),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .bounceClick {
                                                    context.startActivity(
                                                        Intent(
                                                            context,
                                                            ProfileSaveActivity::class.java
                                                        )
                                                    )
                                                }
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.size(18.dp))
                                HorizontalPager(
                                    count = pages.size,
                                    state = pagerState,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .nestedScroll(remember {
                                            object : NestedScrollConnection {
                                                override fun onPreScroll(
                                                    available: Offset,
                                                    source: NestedScrollSource
                                                ): Offset {
                                                    return if (available.y > 0) Offset.Zero else Offset(
                                                        x = 0f,
                                                        y = -scrollState.dispatchRawDelta(-available.y)
                                                    )
                                                }
                                            }
                                        })
                                ) { page ->
                                    when (page) {
                                        0 -> {
                                            if (blockedState.value) {
                                                NoContentScreenV4(loadingState = loadingState, false)
                                            } else if (postList.value != null && postList.value!!.isNotEmpty()) {
                                                LazyVerticalGrid(
                                                    columns = GridCells.Fixed(2),
                                                    verticalArrangement = Arrangement.spacedBy(18.dp),
                                                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                                                    modifier = Modifier
                                                        .fillMaxSize(),
                                                    contentPadding = PaddingValues(18.dp)
                                                ) {
                                                        items(postList.value ?: listOf()) { postCard ->
                                                        // 이미지가 들어간 버튼을 넣어야 함
                                                        Box(modifier = Modifier
                                                            .bounceClick {
                                                                val intent = Intent(context, PostContentActivity::class.java)
                                                                val bundle = Bundle()

                                                                bundle.putInt("board_id", postCard.id)
                                                                bundle.putBoolean("run", true)

                                                                intent.putExtras(bundle)

                                                                val pendIntent: PendingIntent
                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                                                    pendIntent = PendingIntent
                                                                        .getActivity(
                                                                            context, 0,
                                                                            intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent . FLAG_MUTABLE
                                                                        )

                                                                } else {
                                                                    pendIntent = PendingIntent
                                                                        .getActivity(
                                                                            context, 0,
                                                                            intent, PendingIntent.FLAG_UPDATE_CURRENT
                                                                        )
                                                                }
                                                                postLauncher.launch(
                                                                    IntentSenderRequest
                                                                        .Builder(pendIntent)
                                                                        .build()
                                                            )
                                                        }) {
                                                            GlideImage(
                                                                imageModel = postCard.image,
                                                                contentDescription = null,
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .aspectRatio(1f)
                                                                    .clip(RoundedCornerShape(15.dp))
                                                                    .background(moduBackground),
                                                                contentScale =
                                                                if (postCard.id == 0) {
                                                                    ContentScale.None
                                                                } else {
                                                                    ContentScale.Crop
                                                                },
                                                                loading = {
                                                                    ShowProgressBar()
                                                                },
                                                                // shows an error text if fail to load an image.
                                                                failure = {
                                                                    Text(text = "image request failed.")
                                                                },
                                                                requestOptions = {
                                                                    RequestOptions()
                                                                        .override(256, 256)
                                                                }
                                                            )
                                                        }
                                                    }
                                                }
                                            } else {
                                                NoContentScreenV4(loadingState, userId == myId)
                                            }
                                        }
                                        1 -> {
                                            if (blockedState.value) {
                                                NoContentScreenV4(loadingState = loadingState, false)
                                            } else if (curationList.value != null && curationList.value!!.isNotEmpty()) {
                                                LazyColumn(
                                                    modifier = Modifier
                                                        .fillMaxSize(),
                                                    verticalArrangement = Arrangement.spacedBy(15.dp),
                                                    contentPadding = PaddingValues(18.dp)
                                                ) {
                                                    items(curationList.value ?: listOf()) { curationCard ->
                                                        Row(
                                                            modifier = Modifier
                                                                .height(90.dp)
                                                                .bounceClick {
                                                                    val intent = Intent(
                                                                        context,
                                                                        CurationContentActivity::class.java
                                                                    )
                                                                    val bundle = Bundle()

                                                                    bundle.putInt(
                                                                        "curation_id",
                                                                        curationCard.id
                                                                    )

                                                                    intent.putExtras(bundle)

                                                                    Log.d(
                                                                        "result-like",
                                                                        "send : curation_id $curationCard"
                                                                    )
                                                                    val pendIntent: PendingIntent
                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                                                        pendIntent = PendingIntent
                                                                            .getActivity(
                                                                                context,
                                                                                0,
                                                                                intent,
                                                                                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                                                                            )

                                                                    } else {
                                                                        pendIntent = PendingIntent
                                                                            .getActivity(
                                                                                context,
                                                                                0,
                                                                                intent,
                                                                                PendingIntent.FLAG_UPDATE_CURRENT
                                                                            )
                                                                    }

                                                                    curationLauncher.launch(
                                                                        IntentSenderRequest
                                                                            .Builder(pendIntent)
                                                                            .build()
                                                                    )
                                                                }
                                                        ) {
                                                            GlideImage(
                                                                imageModel = curationCard.image,
                                                                contentDescription = null,
                                                                modifier = Modifier
                                                                    .size(90.dp)
                                                                    .clip(RoundedCornerShape(15.dp))
                                                                    .background(moduBackground),
                                                                contentScale =
                                                                if (curationCard.id == 0) {
                                                                    ContentScale.None
                                                                } else {
                                                                    ContentScale.Crop
                                                                },
                                                                loading = {
                                                                    ShowProgressBar()
                                                                },
                                                                // shows an error text if fail to load an image.
                                                                failure = {
                                                                    Text(text = "image request failed.")
                                                                },
                                                                requestOptions = {
                                                                    RequestOptions()
                                                                        .override(256, 256)
                                                                }
                                                            )
                                                            Spacer(modifier = Modifier.width(18.dp))
                                                            if (curationCard.id == 0) {
                                                                Text(
                                                                    text = curationCard.title,
                                                                    style = TextStyle(
                                                                        color = moduGray_strong,
                                                                        fontSize = 14.sp,
                                                                        fontWeight = FontWeight.Bold
                                                                    ),
                                                                    modifier = Modifier.align(
                                                                        CenterVertically
                                                                    )
                                                                )
                                                            } else {
                                                                Column(
                                                                    modifier = Modifier
                                                                        .height(42.dp)
                                                                        .align(CenterVertically)
                                                                ) {
                                                                    Text(
                                                                        text = curationCard.title,
                                                                        style = TextStyle(
                                                                            color = Color.Black,
                                                                            fontWeight = FontWeight.Bold,
                                                                            fontSize = 14.sp
                                                                        )
                                                                    )
                                                                    Spacer(
                                                                        modifier = Modifier.weight(
                                                                            1f
                                                                        )
                                                                    )
                                                                    Text(
                                                                        text = "${curationCard.category}, ${
                                                                            timeToDate(
                                                                                curationCard.created_date
                                                                            )
                                                                        }",
                                                                        fontSize = 12.sp,
                                                                        color = moduGray_strong
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                NoContentScreenV4(loadingState, userId == myId)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun NoContentScreenV4(
    loadingState: MutableState<Boolean>,
    isMyProfile: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 75.dp)
    ) {
        if (loadingState.value) {
            CircularProgressIndicator(
                color = moduPoint,
                modifier = Modifier.align(TopCenter)
            )
        } else {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .align(TopCenter)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_no_post),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .align(CenterHorizontally)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "게시물이 없어요.",
                    style = TextStyle(
                        color = moduBlack,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.align(CenterHorizontally)
                )
                Spacer(modifier = Modifier.size(10.dp))
                if(isMyProfile)
                    Text(
                        text = "업로드에서 게시물을 추가해보세요.",
                        style = TextStyle(
                            color = moduGray_strong,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.align(CenterHorizontally)
                    )
                else
                    Spacer(modifier = Modifier.size(14.dp))
            }
        }
    }
}