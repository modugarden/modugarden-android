package com.example.modugarden.ui.theme

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//바운스 버튼
enum class ButtonState { Pressed, Idle }
fun Modifier.bounceClick(onClick: () -> Unit) = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0.95f else 1f)
    val alphaScale by animateFloatAsState(
        if (buttonState == ButtonState.Pressed) 0.40f else 1f,
        animationSpec = tween(
            durationMillis = 50,
        )
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
            alpha = alphaScale
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

//커스텀 TextField
@Composable
fun EditText(
    title: String,
    data: MutableState<String>,
    isTextFieldFocused: MutableState<Boolean>,
    modifier: Modifier = Modifier
        .fillMaxWidth(),
    maxLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = false,
) {
    val focusRequester = remember { FocusRequester() }
    Column {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (isTextFieldFocused.value) moduPoint else moduGray_strong)
        Spacer(modifier = Modifier.height(5.dp))
        TextField(
            modifier = modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isTextFieldFocused.value = it.isFocused
                },
            value = data.value,
            onValueChange = { textValue ->
                data.value = textValue
            },
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = if (isTextFieldFocused.value) moduTextFieldPoint else moduBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(fontSize = 20.sp, color = moduBlack),
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = singleLine
        )
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