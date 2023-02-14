package com.example.modugarden.ui.theme

import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.ViewTreeObserver
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.keyframes
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
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import com.example.modugarden.ApplicationClass.Companion.clientId
import com.example.modugarden.ApplicationClass.Companion.clientNickname
import com.example.modugarden.ApplicationClass.Companion.profileImage
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.RetrofitBuilder.curationAPI
import com.example.modugarden.api.RetrofitBuilder.postAPI
import com.example.modugarden.api.dto.*
import com.example.modugarden.api.dto.PostDTO.*
import com.example.modugarden.data.RecentSearch
import com.example.modugarden.data.RecentSearchDatabase
import com.example.modugarden.main.follow.moduBold
import com.example.modugarden.route.NAV_ROUTE_DISCOVER_SEARCH
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

val shakeKeyframes: AnimationSpec<Float> = keyframes {
    durationMillis = 800
    val easing = FastOutLinearInEasing

    // generate 8 keyframes
    for (i in 1..8) {
        val x = when (i % 3) {
            0 -> 4f
            1 -> -4f
            else -> 0f
        }
        x at durationMillis / 10 * i with easing
    }
}
// 진동 효과
 fun animateShake(
    offset: Animatable<Float, AnimationVector1D>,
    coroutineScope: CoroutineScope,
    view: View? = null,
) {
    coroutineScope.launch {
        offset.animateTo(
            targetValue = 0f,
            animationSpec = shakeKeyframes,
        )
    }
    view?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view.performHapticFeedback(HapticFeedbackConstants.REJECT)
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
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
    keyboardActions: KeyboardActions = KeyboardActions(),
    placeholder: String = "",
    placeholderSize: Int = 20
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
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(10.dp),
                    color = if (isTextFieldFocused.value) if (errorListener.value) moduErrorPoint else moduPoint else if (errorListener.value) moduErrorBackgroundPoint else moduBackground
                )
                .animateContentSize(),
            value = data.value,
            keyboardActions = keyboardActions,
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
            placeholder = { Text(placeholder, color = moduGray_normal, fontSize = placeholderSize.sp) },
            visualTransformation =
            if (keyboardType == KeyboardType.Password) PasswordVisualTransformation()
            else VisualTransformation.None
        )
        if(description != "") {
            Spacer(modifier = Modifier.height(5.dp))
            Text(description, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = if (errorListener.value) moduErrorPoint else if (isTextFieldFocused.value) moduPoint else moduGray_strong)
        }
    }
}

@Composable
fun NicknameEditText(
    title: String?, //textField 위에 들어갈 제목.
    data: MutableState<String?>, //textField의 데이터 값을 저장.
    isTextFieldFocused: MutableState<Boolean>, //textField가 포커싱 되어 있는지 여부.
    modifier: Modifier = Modifier
        .fillMaxWidth(),
    keyboardType: KeyboardType = KeyboardType.Text, //키보드 형식 (비밀번호, 이메일 등.)
    keyboardActions: KeyboardActions = KeyboardActions(),
    errorState: MutableState<Boolean>,
    placeholder: String = "",
    placeholderSize: Int = 20,
) {
    val focusRequester = remember { FocusRequester() }
    Column {
        if (title!!.isNotEmpty()) {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color =
                if (errorState.value) moduErrorPoint
                else if (isTextFieldFocused.value) moduPoint
                else moduGray_strong
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
        TextField(
            modifier = modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isTextFieldFocused.value = it.isFocused
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("nickname", data.value)
                    if(!isTextFieldFocused.value)
                        RetrofitBuilder.signupAPI.signupNicknameIsDuplicatedAPI(jsonObject)
                            .enqueue(object : Callback<SignupNicknameIsDuplicatedDTO> {
                                override fun onResponse(
                                    call: Call<SignupNicknameIsDuplicatedDTO>,
                                    response: Response<SignupNicknameIsDuplicatedDTO>
                                ) {
                                    if(data.value != sharedPreferences.getString(clientNickname, ""))
                                        errorState.value = response.body()?.result?.isDuplicated!!
                                    Log.d("onResponse", "닉네임 체크 : \n" +
                                            "${response.body()}\n" +
                                            "${response.body()?.result?.isDuplicated}\n" +
                                            "${errorState.value}" )
                                }
                                override fun onFailure(
                                    call: Call<SignupNicknameIsDuplicatedDTO>,
                                    t: Throwable
                                ) {

                                }
                            })
                }
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(10.dp),
                    color =
                    if (isTextFieldFocused.value)
                        if (errorState.value) moduErrorPoint
                        else moduPoint
                    else if (errorState.value)
                        moduErrorBackgroundPoint
                    else moduBackground
                )
                .animateContentSize(),
            value = data.value ?: "",
            keyboardActions = keyboardActions,
            onValueChange = { textValue ->
                data.value = textValue
            },
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor =
                if (errorState.value) moduErrorBackgroundPoint
                else if (isTextFieldFocused.value) moduTextFieldPoint
                else moduBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(fontSize = 20.sp, color = moduBlack),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            placeholder = { Text(placeholder, color = moduGray_normal, fontSize = placeholderSize.sp) }
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(text =
        if (errorState.value) "중복된 닉네임이예요."
        else if (data.value?.length in 0..25) "2~25자의 영문, 숫자, _만 가능해요."
        else "글자 수를 초과했어요",
            fontWeight = FontWeight.Bold, fontSize = 11.sp,
            color =
            if (errorState.value) moduErrorPoint
            else if (isTextFieldFocused.value) moduPoint
            else moduGray_strong)
    }
}

@Composable
fun DisabledEditText(
    title: String?, //textField 위에 들어갈 제목.
    data: MutableState<String>, //textField의 데이터 값을 저장.
    modifier: Modifier = Modifier
        .fillMaxWidth(),
    keyboardType: KeyboardType = KeyboardType.Text, //키보드 형식 (비밀번호, 이메일 등.)
    singleLine: Boolean = false, //textField를 한 줄 고정할 것인지 여부.
    description: String = "", //textField 아래에 들어갈 설명.
    textStyle: TextStyle = TextStyle(fontSize = 20.sp, color = moduGray_normal), //textField의 글자 스타일 설정.
) {
    val focusRequester = remember { FocusRequester() }
    Column {
        if (title!!.isNotEmpty()) {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = moduGray_strong
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
        TextField(
            modifier = modifier
                .focusRequester(focusRequester)
                .animateContentSize(),
            value = data.value,
            onValueChange = { textValue ->
                data.value = textValue
            },
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = moduBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = singleLine,
            readOnly = true
        )
        if(description != "") {
            Spacer(modifier = Modifier.height(5.dp))
            Text(description, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = moduGray_strong)
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
                        .bounceClick{ titleIconOnClick.invoke() },
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
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(start = 18.dp)
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
        Text(text, fontSize = fontSize.sp, color = textColor, fontWeight = FontWeight.Bold, modifier = Modifier
            .padding(horizontal = 18.dp)
            .padding(vertical = 8.dp), textAlign = TextAlign.Center)
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

@Composable
fun ProfileUpdateBottomButton(
    title: String,
    onClick: () -> Unit,
    dpScale: Dp = 18.dp,
    alpha: Float = 1f,
    shapeScale: Dp = 15.dp,
    color: Color = moduPoint,
    textColor: Color = Color.White,
    disabled: MutableState<Boolean>
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
                    if(!disabled.value)
                        onClick.invoke()
                }
                .padding(dpScale)
                .fillMaxWidth()
                .alpha(
                    if(!disabled.value)
                        alpha
                    else
                        alpha / 2
                ),
            shape = RoundedCornerShape(shapeScale),
            backgroundColor = color,
            elevation = 0.dp,
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
fun SearchTextField(
    searchText : MutableState<String>,
    isTextFieldSearchFocused : MutableState<Boolean>,
    focusManager : FocusManager,
    db: RecentSearchDatabase,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isTextFieldSearchFocused.value) moduTextFieldPoint
                else moduBackground
            )
    ) {
        //검색어 입력하는 텍스트 필드
        TextField(
            value = searchText.value,
            onValueChange = { searchText.value = it },
            modifier = Modifier
                .padding(start = 30.dp)
                .fillMaxWidth()
                .height(52.dp)
                .onFocusChanged {
                    isTextFieldSearchFocused.value = it.isFocused
                }
                .animateContentSize(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor =
                if (isTextFieldSearchFocused.value) moduTextFieldPoint
                else moduBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            textStyle = TextStyle(fontSize = 14.sp, color = moduBlack),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                isTextFieldSearchFocused.value = false

                Log.d("keyboard-test", searchText.value)
                if(searchText.value != "") {
                    //이미 전에 검색했던 거면 한번 지우고 다시 insert해줘서 맨 위로 올려줌
                    val checkData: RecentSearch? = db.recentSearchDao().findRecentSearchBySearchText(searchText.value)
                    checkData?.let {
                        db.recentSearchDao().delete(
                            it
                        )
                    }

                    db.recentSearchDao().insert(RecentSearch(searchText.value))
                    navController.navigate(route = NAV_ROUTE_DISCOVER_SEARCH.DISCOVERSEARCHRESULT.routeName + "/" + searchText.value) {
                        popUpTo(NAV_ROUTE_DISCOVER_SEARCH.DISCOVERSEARCHING.routeName)
                    }


                }
            }),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            placeholder = {
                Text(
                    text = "게시물이나 사용자를 검색해보세요",
                    style = TextStyle(color = moduGray_normal)
                )
            }
        )
        if (searchText.value.isNotEmpty()) {
            Image(
                painterResource(id = R.drawable.ic_x_circle),
                contentDescription = "검색중 나오는 x 이미지",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp, top = 5.dp)
                    .bounceClick {
                        searchText.value = ""
                    }
            )
        }

        Image(
            painter =
            if(isTextFieldSearchFocused.value) painterResource(id = R.drawable.ic_search_in_text_green)
            else painterResource(id = R.drawable.ic_search_in_text_gray),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 14.dp)
        )
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
fun ShowProgressBarV2() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
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
    heartState: MutableState<Boolean>,
    likeNum : MutableState<Int>?
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
                            if (response.body()!=null)
                                likeNum?.value = response.body()!!.result.like_num
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
                            if (response.body()!=null)
                                likeNum?.value = response.body()!!.result.like_num
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
fun PostSaveCard(
    boardId: Int,
    modifier: Modifier,
    saveState: MutableState<Boolean>,
    scope:CoroutineScope,
    snackbarHostState: SnackbarHostState,
) {
    postAPI.getPostSaveState(boardId).enqueue(
        object : Callback<GetPostSaveStateResponse> {
            override fun onResponse(
                call: Call<GetPostSaveStateResponse>,
                response: Response<GetPostSaveStateResponse>
            ) {
                saveState.value = response.body()?.result?.check ?: false
            }

            override fun onFailure(
                call: Call<GetPostSaveStateResponse>,
                t: Throwable
            ) {

            }

        }
    )

    Icon(
        modifier = modifier
            .bounceClick {
                if(saveState.value) {
                    postAPI.saveCancelPost(boardId).enqueue(
                        object : Callback<PostStoreResponse> {
                            override fun onResponse(
                                call: Call<PostStoreResponse>,
                                response: Response<PostStoreResponse>
                            ) {
                                saveState.value = false
                            }

                            override fun onFailure(
                                call: Call<PostStoreResponse>,
                                t: Throwable
                            ) {

                            }

                        }
                    )
                }
                else
                {
                    postAPI.savePost(boardId).enqueue(
                        object : Callback<PostStoreResponse> {
                            override fun onResponse(
                                call: Call<PostStoreResponse>,
                                response: Response<PostStoreResponse>
                            ) {
                                saveState.value = true
                            }

                            override fun onFailure(
                                call: Call<PostStoreResponse>,
                                t: Throwable
                            ) {

                            }

                        }
                    )
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "게시물을 저장하였습니다.",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                /*RetrofitBuilder.postAPI.getPostLikeNum(boardId)
                    .enqueue(object :Callback<CurationLikeResponse>{
                        override fun onResponse(
                            call: Call<CurationLikeResponse>,
                            response: Response<CurationLikeResponse>
                        ) {
                            if (response.isSuccessful){
                                Log.i("좋아요 조회","성공 ${response.body()?.result?.like_num}")
                                likeNumRes = response.body()?.result?.like_num!!
                            }
                            else
                                Log.i("좋아요 조회","실패 ${response.body()?.message}")
                        }

                        override fun onFailure(call: Call<CurationLikeResponse>, t: Throwable) {
                            Log.i("좋아요 조회","서버 연결 실패")
                        }
                    })*/
            }
        ,painter = painterResource(
            id = if (saveState.value) R.drawable.ic_star_solid
            else R.drawable.ic_star_line
        ),
        contentDescription = "보관",
        tint = moduBlack

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
                Log.d("upload-result123", response.body().toString())

            }

            override fun onFailure(
                call: Call<GetCurationLikeStateResponse>,
                t: Throwable
            ) {
                Log.d("upload-result", "왜안됍")

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
    contentModifier: Modifier,
    blockState: MutableState<Boolean> = mutableStateOf(false),
    unBlockSnackBarAction: () -> Unit = {},
    fcmTokenState: MutableState<List<String>> = mutableStateOf<List<String>>(listOf())
) {
    if(id == sharedPreferences.getInt(clientId,0)) {
        Card(
            modifier = Modifier,
            elevation = 0.dp,
            backgroundColor = Color.Transparent
        ) {

        }
    } else {
        Card(
            modifier = modifier
                .bounceClick {
                    if (blockState.value) {
                        RetrofitBuilder.blockAPI.unBlockUser(id).enqueue(
                            object : Callback<UnBlockUserResponse> {
                                override fun onResponse(
                                    call: Call<UnBlockUserResponse>,
                                    response: Response<UnBlockUserResponse>
                                ) {
                                    if(response.code() == 200) {
                                        unBlockSnackBarAction()
                                        blockState.value = false
                                    }
                                }

                                override fun onFailure(call: Call<UnBlockUserResponse>, t: Throwable) {

                                }
                            }
                        )
                    }
                    // 팔로우 api
                    else {
                        if (!followState.value) {
                            RetrofitBuilder.followAPI.follow(id).enqueue(
                                object : Callback<FollowDtoRes> {
                                    override fun onResponse(
                                        call: Call<FollowDtoRes>,
                                        response: Response<FollowDtoRes>
                                    ) {
                                        if(response.body()?.code == 200) {
                                            snackBarAction()
                                            followState.value = !followState.value
                                            fcmTokenState.value.forEach {token ->
                                                Log.d("onTokenResponse", "sendNotification : $token")
                                                sendNotification(
                                                    0,
                                                    sharedPreferences.getInt(clientId, 0),
                                                    sharedPreferences.getString(clientNickname,""),
                                                    sharedPreferences.getString(profileImage, null),
                                                    titleMessage = "팔로우 알림",
                                                    fcmToken = token,
                                                    message = "님이 회원님을 팔로우했어요."
                                                )
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<FollowDtoRes>, t: Throwable) {

                                    }
                                }
                            )
                        } else {
                            RetrofitBuilder.followAPI.unFollow(id).enqueue(
                                object : Callback<FollowDtoRes> {
                                    override fun onResponse(
                                        call: Call<FollowDtoRes>,
                                        response: Response<FollowDtoRes>
                                    ) {
                                        if(response.body()?.code == 200) {
                                            snackBarAction()
                                            if (response.isSuccessful) {
                                                followState.value = !followState.value
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<FollowDtoRes>, t: Throwable) {

                                    }
                                }
                            )
                        }
                    }
                },
            shape = RoundedCornerShape(10.dp),
            backgroundColor =
            if (followState.value || blockState.value) { moduBackground }
            else { moduPoint },
            elevation = 0.dp
        ) {
            Text(
                text =
                if (blockState.value) { "차단 해제" }
                else
                    if (followState.value) { "팔로잉" }
                    else { "팔로우" },
                style = TextStyle(
                    color =
                    if (followState.value || blockState.value) { moduBlack }
                    else { Color.White },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = contentModifier
            )
        }
    }
}

@Composable
fun ModuDialog(
    onDismissRequest: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface (
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(15.dp)
        ) {
            content()
        }
    }
}

@Composable
fun SmallDialog(
    text: String,
    text2:String?=null,
    textColor: Color,
    backgroundColor: Color,
    positiveButtonText: String,
    negativeButtonText: String,
    positiveButtonTextColor: Color,
    negativeButtonTextColor: Color,
    positiveButtonColor: Color,
    negativeButtonColor: Color,
    dialogState: MutableState<Boolean>,
    reportCategory:String?=null,
    reportType:Int?=null,
    reportMessage:MutableState<String>?=null,
    onPositiveButtonClick: () -> Unit

) {
    ModuDialog {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
                .wrapContentHeight()
                .background(backgroundColor),
        ) {
            Text(
                text = text,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.CenterHorizontally)
            )
            if (text2 != null) {
                Text(
                    text = text2,
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        color = moduGray_strong,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .wrapContentHeight()
                        .align(Alignment.CenterHorizontally)
                )
            }
            Spacer(modifier = Modifier.size(18.dp))
            Row(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .bounceClick {
                            dialogState.value = false
                        },
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = negativeButtonColor,
                    elevation = 0.dp
                ) {
                    Text(
                        text = negativeButtonText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = negativeButtonTextColor,
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
                            onPositiveButtonClick()
                            dialogState.value = false
                        },
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = positiveButtonColor,
                    elevation = 0.dp
                ) {
                    Text(
                        text = positiveButtonText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = positiveButtonTextColor,
                        modifier = Modifier
                            .padding(14.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun OneButtonSmallDialog(
    text: String,
    textColor: Color,
    backgroundColor: Color,
    buttonText: String,
    buttonTextColor: Color,
    buttonColor: Color,
    dialogState: MutableState<Boolean>,
    onButtonClick: () -> Unit = {}
) {
    ModuDialog {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
                .wrapContentHeight()
                .background(backgroundColor),
        ) {
            Text(
                text = text,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.size(18.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .bounceClick {
                        onButtonClick()
                        dialogState.value = false
                    },
                shape = RoundedCornerShape(10.dp),
                backgroundColor = buttonColor,
                elevation = 0.dp
            ) {
                Text(
                    text = buttonText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = buttonTextColor,
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(14.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

fun sendNotification(
    notificationType: Int,
    targetId: Int,
    targetName: String?,
    targetImage: String?,
    titleMessage: String,
    fcmToken: String?,
    message: String
) {
    val jsonBody = JsonObject()
    val dataBody = JsonObject()
    dataBody.apply {
        addProperty("title", targetName + titleMessage)
        addProperty("body", message)
        addProperty("image", targetImage)
        addProperty("type", notificationType.toString())
        addProperty("name", targetName)
        addProperty("address", targetId.toString())
    }
    jsonBody.apply {
        addProperty("to", fcmToken)
        addProperty("priority", "high")
        add("data", dataBody)
    }
    CoroutineScope(Dispatchers.IO).launch {
        val response = RetrofitBuilder.fcmSendAPI.fcmSendAPI(jsonBody = jsonBody).execute()
        Log.d("onTokenResponse", response.toString())
    }
}