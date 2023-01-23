package com.example.modugarden.main.settings

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.R
import com.example.modugarden.ui.theme.*

@Preview(showBackground = true)
@Composable
fun SettingsNotificationScreen () {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(moduBackground)
    ) {
        val commentState = remember { mutableStateOf(true) }
        val followState = remember { mutableStateOf(true) }
        val serviceState = remember { mutableStateOf(true) }
        val marketingState = remember { mutableStateOf(true) }
        val autoLoginState = remember { mutableStateOf(false) }
        Notifications(checkState = commentState, option = "댓글 알림")
        Notifications(checkState = followState, option = "팔로우 알림")
        Spacer(modifier = Modifier.height(18.dp))
        Notifications(checkState = serviceState, option = "서비스 알림")
        Spacer(modifier = Modifier.height(18.dp))
        Notifications(checkState = marketingState, option = "마케팅 알림")
        Spacer(modifier = Modifier.height(18.dp))
        Notifications(checkState = autoLoginState, option = "자동 로그인")
    }
}

@Composable
fun Notifications (
    checkState: MutableState<Boolean>,
    option: String
) {
    Row(
        modifier = Modifier
            .height(58.dp)
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.width(18.dp))
        Text(
            text = option,
            style = TextStyle(
                color = moduBlack,
                fontSize = 16.sp
            ),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.weight(1f))

        val scale = 1.5f
        val width = 30.dp
        val height = 18.dp
        val gapBetweenThumbAndTrackEdge = 1.dp
        val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge

        // To move thumb, we need to calculate the position (along x axis)
        val animatePosition = animateFloatAsState(
            targetValue = if (checkState.value)
                with(LocalDensity.current) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() }
            else
                with(LocalDensity.current) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }
        )

        Canvas(
            modifier = Modifier
                .size(width = width, height = height)
                .scale(scale = scale)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            // This is called when the user taps on the canvas
                            checkState.value = !checkState.value
                        }
                    )
                }
                .align(Alignment.CenterVertically)
        ) {
            // Track
            drawRoundRect(
                color = if (checkState.value) moduPoint else moduGray_strong,
                cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx()),
                style = Fill
            )

            // Thumb
            drawCircle(
                color = Color.White,
                radius = thumbRadius.toPx(),
                center = Offset(
                    x = animatePosition.value,
                    y = size.height / 2
                )
            )
        }
        Spacer(modifier = Modifier.width(18.dp))
    }
}