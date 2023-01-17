package com.example.modugarden.ui.theme

import android.graphics.Rect
import android.view.ViewTreeObserver
import android.widget.Space
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.R
import kotlinx.coroutines.launch
import kotlin.math.pow

//바운스 버튼
fun Modifier.bounceClick(onClick: () -> Unit) = composed {

    val interactionSource = MutableInteractionSource()

    val coroutineScope = rememberCoroutineScope()

    val scale = remember {
        Animatable(1f)
    }
    val scaleDown = 0.93f
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
    title: String, //textField 위에 들어갈 제목.
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
        Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if(errorListener.value) moduErrorPoint else if (isTextFieldFocused.value) moduPoint else moduGray_strong)
        Spacer(modifier = Modifier.height(5.dp))
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
    titleIconSize: Dp = 16.dp,
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
                        }
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