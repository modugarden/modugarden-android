package com.example.modugarden.ui.theme

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.RetrofitBuilder.curationAPI
import com.example.modugarden.api.RetrofitBuilder.postAPI
import com.example.modugarden.api.dto.CurationLikeResponse
import com.example.modugarden.api.dto.CurationStoreResponse
import com.example.modugarden.api.dto.FollowDtoRes
import com.example.modugarden.api.dto.GetCurationLikeStateResponse
import com.example.modugarden.api.dto.PostDTO.*
import com.example.modugarden.main.follow.moduBold
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.pow

//바운스 버튼
enum class ButtonState { Pressed, Idle }
//바운스 버튼
fun Modifier.bounceClick(onClick: () -> Unit) = composed {

    val interactionSource = MutableInteractionSource()

    val coroutineScope = rememberCoroutineScope()

    val scale = remember {
        Animatable(1f)
    }
    val scaleDown = 0.95f
    val animationDuration = 150
    val mContext = LocalContext.current

    this
        .scale(scale = scale.value)
        .alpha(
            (0.0012)
                .pow((1 - scale.value).toDouble())
                .toFloat()
        )
        .clickable(
            interactionSource = interactionSource, indication = null, onClick = {})
        .pointerInput(Unit) {
            while (true)
                awaitPointerEventScope {
                    awaitFirstDown()
                    coroutineScope.launch {
                        scale.animateTo(
                            scaleDown,
                            animationSpec = tween(animationDuration),
                        )
                    }
                    val up = waitForUpOrCancellation()
                    coroutineScope.launch {
                        scale.animateTo(
                            scaleDown,
                            animationSpec = tween(40),
                        )
                        scale.animateTo(
                            1f,
                            animationSpec = tween(animationDuration),
                        )
                    }
                    if (up.toString() != "null") {
                        onClick.invoke()
                    }
                }
        }
}

//커스텀 TextField
@Composable
fun EditText(
    title: String?, //textField 위에 들어갈 제목.
    data: MutableState<String>, //textField의 데이터 값을 저장.
    isTextFieldFocused: MutableState<Boolean>, //textField가 포커싱 되어 있는지 여부.
    modifier: Modifier = Modifier
        .fillMaxWidth(),
    keyboardType: KeyboardType = KeyboardType.Text, //키보드 형식 (비밀번호, 이메일 등.)
    singleLine: Boolean = false, //textField를 한 줄 고정할 것인지 여부.
    description: String = "", //textField 아래에 들어갈 설명.
    errorListener: MutableState<Boolean> = mutableStateOf(false), //textField에 들어갈 값의 조건이 틀렸는지 여부.
    textStyle: TextStyle = TextStyle(fontSize = 20.sp, color = moduBlack), //textField의 글자 스타일 설정.
) {
    val focusRequester = remember { FocusRequester() }
    Column {
        if (title!!.isNotEmpty()) {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = if(errorListener.value) moduErrorPoint else if (isTextFieldFocused.value) moduPoint else moduGray_strong
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
        TextField(
            modifier = modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isTextFieldFocused.value = it.isFocused
                }
                .animateContentSize(),
            value = data.value,
            onValueChange = { textValue ->
                data.value = textValue
            },
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = if (errorListener.value) moduErrorBackgroundPoint else if (isTextFieldFocused.value) moduTextFieldPoint else moduBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = singleLine,
        )
        if(description != "") {
            Spacer(modifier = Modifier.height(5.dp))
            Text(description, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = if (errorListener.value) moduErrorPoint else if (isTextFieldFocused.value) moduPoint else moduGray_strong)
        }
    }
}
//화면에서 포커스 없애주는 함수
fun Modifier.addFocusCleaner(focusManager: FocusManager, doOnClear: () -> Unit = {}): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            doOnClear()
            focusManager.clearFocus()
        })
    }
}

//키보드가 올라왔는지 확인하는 함수
enum class Keyboard { Opened, Closed }
@Composable
fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                Keyboard.Opened
            } else {
                Keyboard.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}

//상단 조작 바
@Composable
fun TopBar(
    title: String, //상단 조작 바 제목.
    titleIcon: Int = 0, //제목 버튼 아이콘.
    titleIconOnClick: () -> Unit = {}, //제목 버튼을 눌렀을 때.
    titleIconSize: Dp = 20.dp,
    titleIconTint: Color = moduBlack,
    main: Boolean = false, //주요 화면인지.
    icon1: Int = 0, //오른쪽에 아이콘 버튼이 추가됨. (아이콘)
    icon2: Int = 0, //오른쪽에 아이콘 버튼이 추가됨. (아이콘)
    onClick1: () -> Unit = {}, //오른쪽에 아이콘 버튼이 추가됨. (눌렸을 때 수행할 것)
    onClick2: () -> Unit = {}, //오른쪽에 아이콘 버튼이 추가됨. (눌렀을 때 수행할 것)
    iconTint1: Color = moduGray_normal,
    iconTint2: Color = moduGray_normal,
    bottomLine: Boolean = true, //밑줄 여부.
    backgroundColor: Color = Color.White //상단 조작 바 색.
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(backgroundColor)
            .drawBehind {
                if (bottomLine)
                    drawLine(
                        color = moduGray_light,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
            }
    ) {
        Row(
            modifier = Modifier
                .padding(18.dp)
                .padding(top = if (main) 40.dp else 0.dp)
        ) {
            if(titleIcon != 0) {
                Image(
                    painter = painterResource(id = titleIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .height(titleIconSize)
                        .width(titleIconSize)
                        .align(Alignment.CenterVertically)
                        .bounceClick {
                            titleIconOnClick.invoke()
                        },
                    colorFilter = ColorFilter.tint(titleIconTint)
                )
                Spacer(modifier = Modifier.width(18.dp))
            }
            Text(title, fontSize = if(main) 24.sp else 16.sp, color = moduBlack, modifier = Modifier.align(Alignment.CenterVertically), fontWeight = if(main) FontWeight.Bold else FontWeight.Normal)
            Spacer(modifier = Modifier.weight(1f))
            if(icon2 != 0) {
                Image(
                    painter = painterResource(id = icon2),
                    contentDescription = null,
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp)
                        .bounceClick { onClick2.invoke() }
                        .align(Alignment.CenterVertically),
                    colorFilter = ColorFilter.tint(iconTint2)
                )
            }
            if(icon1 != 0) {
                Spacer(modifier = Modifier.width(18.dp))
                Image(
                    painter = painterResource(id = icon1),
                    contentDescription = null,
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp)
                        .bounceClick { onClick1.invoke() }
                        .align(Alignment.CenterVertically),
                    colorFilter = ColorFilter.tint(iconTint1)
                )
            }
        }
    }
}

//바텀 모달 시트
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheet(
    title: String,
    bottomSheetState: ModalBottomSheetState,
    uiScreen: @Composable () -> Unit,
    sheetScreen: @Composable () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetElevation = 0.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetState = bottomSheetState,
        sheetContent = {
            Card(modifier = Modifier
                .padding(10.dp),
                shape = RoundedCornerShape(15.dp))
            {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    // 회색 홀드
                    Box(modifier = Modifier
                        .width(40.dp)
                        .height(5.dp)
                        .background(moduGray_normal, RoundedCornerShape(30.dp))
                        .alpha(0.2f)
                    )
                    Spacer(modifier = Modifier.size(30.dp))
                    if(title != "") {
                        Text(
                            text = title,
                            style = moduBold,
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.Start).padding(start = 18.dp)
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                    // 리스트
                    LazyColumn(modifier = Modifier
                        .padding(horizontal = 18.dp) ){
                        item {
                            sheetScreen.invoke()
                        }
                    }
                }
            }
        }) {
        uiScreen.invoke()
    }
}
//Modal Bottom Sheet item
@Composable
fun ModalBottomSheetItem(
    icon: Int = 0,
    text: String,
    trailing: Boolean = true,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 10.dp)
        ) {
            if(icon != 0) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text, fontSize = 16.sp, color = moduBlack, modifier = Modifier.align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.weight(1f))
                if(trailing) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_chevron_right),
                        contentDescription = null,
                        modifier = Modifier
                            .width(15.dp)
                            .height(15.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
         }
    }
}
//18.8 버튼
@Composable
fun SmallButton(
    backgroundColor: Color = moduPoint,
    text: String,
    textColor: Color = Color.White,
    fontSize: Int = 14,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .bounceClick { onClick.invoke() },
        backgroundColor = backgroundColor,
        elevation = 0.dp,
        shape = RoundedCornerShape(7.dp)
    ) {
        Text(text, fontSize = fontSize.sp, color = textColor, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 18.dp).padding(vertical = 8.dp), textAlign = TextAlign.Center)
    }
}
//하단 버튼
@Composable
fun BottomButton(
    title: String,
    onClick: () -> Unit,
    dpScale: Dp = 18.dp,
    alpha: Float = 1f,
    shapeScale: Dp = 15.dp,
    color: Color = moduPoint,
    textColor: Color = Color.White
) {
    Box(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White.copy(alpha = 0f), Color.White),
                    startY = 0f,
                    endY = 50f
                )
            )
    ) {
        Card(
            modifier = Modifier
                .bounceClick {
                    onClick.invoke()
                }
                .padding(dpScale)
                .fillMaxWidth()
                .alpha(alpha),
            shape = RoundedCornerShape(shapeScale),
            backgroundColor = color,
            elevation = 0.dp
        ) {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = textColor,
                modifier = Modifier
                    .padding(18.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
//스낵바
@Composable
fun SnackBar(
    snackbarHostState: SnackbarHostState,
    icon: Int = R.drawable.ic_check_solid,
    iconTint: Color = moduPoint,
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { snackbarData: SnackbarData ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color("#62766B".toColorInt()), RoundedCornerShape(10.dp))
            ) {
                Row(
                    Modifier
                        .padding(12.dp, 17.dp)
                ) {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = "체크",
                        colorFilter = ColorFilter.tint(iconTint),
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        text = snackbarData.message,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
        })
}

@Composable
fun ScaffoldSnackBar(
    snackbarHostState: SnackbarHostState,
    icon: Int = R.drawable.ic_check_solid,
    iconTint: Color = moduPoint,
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { snackbarData: SnackbarData ->
            Box(
                Modifier
                    .padding(18.dp)
                    .fillMaxWidth()
                    .background(Color("#62766B".toColorInt()), RoundedCornerShape(10.dp))
            ) {
                Row(
                    Modifier
                        .padding(12.dp, 17.dp)
                ) {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = "체크",
                        colorFilter = ColorFilter.tint(iconTint),
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        text = snackbarData.message,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
        })
}

//탐색에 검색창
@Composable
fun searchTextField(
    searchText : MutableState<String>,
    isTextFieldSearchFocused : MutableState<Boolean>,
    focusManager : FocusManager
) {
    Box {
        //검색어 입력하는 텍스트 필드
        TextField(
            value = searchText.value,
            onValueChange = { textValue -> searchText.value = textValue },
            modifier = Modifier
                .padding(vertical = 0.dp, horizontal = 0.dp)
                .fillMaxWidth(0.9f)
                .height(52.dp)
                .onFocusChanged {
                    isTextFieldSearchFocused.value = it.isFocused
                }
                .animateContentSize(),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor =
                if (isTextFieldSearchFocused.value) moduTextFieldPoint
                else moduBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            textStyle = TextStyle(fontSize = 14.sp, color = moduBlack),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
        )
        if (searchText.value.isNotEmpty()) {
            Image(
                painterResource(id = R.drawable.ic_x_circle),
                contentDescription = "검색중 나오는 x 이미지",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp, top = 1.dp)
                    .bounceClick {
                        searchText.value = ""
                    }
            )
        }
    }
}

@Composable
fun ShowProgressBar() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0f))
    ) {
        CircularProgressIndicator(
            color = moduPoint,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun PostHeartCard(
    boardId: Int,
    modifier: Modifier,
    heartState: MutableState<Boolean>
) {
    postAPI.getPostLikeState(boardId).enqueue(
        object : Callback<GetPostLikeStateResponse> {
            override fun onResponse(
                call: Call<GetPostLikeStateResponse>,
                response: Response<GetPostLikeStateResponse>
            ) {
                heartState.value = response.body()?.result?.check ?: false
            }

            override fun onFailure(
                call: Call<GetPostLikeStateResponse>,
                t: Throwable
            ) {

            }

        }
    )

    Icon(
        modifier = modifier
        .bounceClick {
            if(heartState.value) {
                postAPI.unlikePost(boardId).enqueue(
                    object : Callback<PostLikeResponse> {
                        override fun onResponse(
                            call: Call<PostLikeResponse>,
                            response: Response<PostLikeResponse>
                        ) {
                            heartState.value = false
                        }

                        override fun onFailure(
                            call: Call<PostLikeResponse>,
                            t: Throwable
                        ) {

                        }

                    }
                )
            }
            else {
                postAPI.likePost(boardId).enqueue(
                    object : Callback<PostLikeResponse> {
                        override fun onResponse(
                            call: Call<PostLikeResponse>,
                            response: Response<PostLikeResponse>
                        ) {
                            heartState.value = true
                        }

                        override fun onFailure(
                            call: Call<PostLikeResponse>,
                            t: Throwable
                        ) {

                        }

                    }
                )

            }

        }
        ,painter = painterResource(
            id = if (heartState.value) R.drawable.ic_heart_solid
            else R.drawable.ic_heart_line
        ),
        contentDescription = "좋아요",
        tint =
        if (heartState.value) Color(0xFFFF6767)
        else moduBlack

    )
}

@Composable
fun CurationHeartCard(
    curationId: Int,
    modifier: Modifier,
    heartState: MutableState<Boolean>
) {
    curationAPI.getStateCurationLike(curationId).enqueue(
        object : Callback<GetCurationLikeStateResponse> {
            override fun onResponse(
                call: Call<GetCurationLikeStateResponse>,
                response: Response<GetCurationLikeStateResponse>
            ) {
                heartState.value = response.body()?.result?.check ?: false
            }

            override fun onFailure(
                call: Call<GetCurationLikeStateResponse>,
                t: Throwable
            ) {

            }

        }
    )

    Icon(
        modifier = modifier
            .bounceClick {
                if(heartState.value) {
                    curationAPI.unlikeCuration(curationId).enqueue(
                        object : Callback<CurationLikeResponse> {
                            override fun onResponse(
                                call: Call<CurationLikeResponse>,
                                response: Response<CurationLikeResponse>
                            ) {
                                heartState.value = false
                            }

                            override fun onFailure(
                                call: Call<CurationLikeResponse>,
                                t: Throwable
                            ) {

                            }

                        }
                    )
                }
                else {
                    curationAPI.likeCuration(curationId).enqueue(
                        object : Callback<CurationLikeResponse> {
                            override fun onResponse(
                                call: Call<CurationLikeResponse>,
                                response: Response<CurationLikeResponse>
                            ) {
                                heartState.value = true
                            }

                            override fun onFailure(
                                call: Call<CurationLikeResponse>,
                                t: Throwable
                            ) {

                            }

                        }
                    )

                }

            }
        ,painter = painterResource(
            id = if (heartState.value) R.drawable.ic_heart_solid
            else R.drawable.ic_heart_line
        ),
        contentDescription = "좋아요",
        tint =
        if (heartState.value) Color(0xFFFF6767)
        else moduBlack

    )
}


@Composable
fun CurationSaveCard(
    curationId: Int,
    modifier: Modifier,
    saveState: MutableState<Boolean>
) {
    curationAPI.getCurationStoreState(curationId).enqueue(
        object : Callback<GetCurationLikeStateResponse> {
            override fun onResponse(
                call: Call<GetCurationLikeStateResponse>,
                response: Response<GetCurationLikeStateResponse>
            ) {
                saveState.value = response.body()?.result?.check ?: false
            }

            override fun onFailure(
                call: Call<GetCurationLikeStateResponse>,
                t: Throwable
            ) {

            }

        }
    )

    Icon(modifier = modifier
        .bounceClick {
            if(saveState.value) {
                curationAPI.storeCancelCuration(curationId).enqueue(
                    object : Callback<CurationStoreResponse> {
                        override fun onResponse(
                            call: Call<CurationStoreResponse>,
                            response: Response<CurationStoreResponse>
                        ) {
                            saveState.value = false
                        }
                        override fun onFailure(
                            call: Call<CurationStoreResponse>,
                            t: Throwable
                        ) {

                        }

                    }
                )
            }
            else {
                curationAPI.storeCuration(curationId).enqueue(
                    object : Callback<CurationStoreResponse> {
                        override fun onResponse(
                            call: Call<CurationStoreResponse>,
                            response: Response<CurationStoreResponse>
                        ) {
                            saveState.value = true
                        }

                        override fun onFailure(
                            call: Call<CurationStoreResponse>,
                            t: Throwable
                        ) {

                        }

                    }
                )

            }

        }
        ,painter = painterResource(
            id =
            if (saveState.value) R.drawable.ic_star_solid
            else R.drawable.ic_star_line
        ),
        contentDescription = "스크랩",
        tint = moduBlack
    )
}


@Composable
fun FollowCard(
    id: Int,
    modifier: Modifier,
    snackBarAction: () -> Unit,
    followState: MutableState<Boolean>,
    contentModifier: Modifier
) {
    Card(
        modifier = modifier
            .bounceClick {
                // 팔로우 api
                if(!followState.value) {
                    RetrofitBuilder.followAPI.follow(id).enqueue(
                        object : Callback<FollowDtoRes> {
                            override fun onResponse(
                                call: Call<FollowDtoRes>,
                                response: Response<FollowDtoRes>
                            ) {
                                snackBarAction()
                                followState.value = !followState.value
                            }

                            override fun onFailure(call: Call<FollowDtoRes>, t: Throwable) {

                            }
                        }
                    )
                }
                else {
                    RetrofitBuilder.followAPI.unFollow(id).enqueue(
                        object : Callback<FollowDtoRes> {
                            override fun onResponse(
                                call: Call<FollowDtoRes>,
                                response: Response<FollowDtoRes>
                            ) {
                                snackBarAction()
                                followState.value = !followState.value
                            }

                            override fun onFailure(call: Call<FollowDtoRes>, t: Throwable) {

                            }
                        }
                    )
                }
            },
        shape = RoundedCornerShape(10.dp),
        backgroundColor = if(followState.value) { moduBackground } else { moduPoint },
        elevation = 0.dp
    ) {
        Text(
            text = if(followState.value) { "팔로잉" } else { "팔로우" },
            style = TextStyle(
                color = if(followState.value) { Color.Black } else { Color.White },
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            modifier = contentModifier
        )
    }
}