package com.example.modugarden.main.upload.post

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.data.UploadPost
import com.example.modugarden.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UploadPostUploadSuccessfully(
    navController: NavHostController,
    data: UploadPost
) {

    val animationData = remember { mutableStateListOf(false, false, false, false) }

    LaunchedEffect(key1 = animationData) {
        while (true) {
            delay(300)
            animationData[0] = true
            delay(300)
            animationData[1] = true
            delay(300)
            animationData[2] = true
        }
    }

    Column {
        Spacer(Modifier.weight(1f))
        AnimatedVisibility(
            visible = animationData[0],
            enter = scaleIn(animationSpec = tween(500, easing = EaseOutBack))
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_check_solid),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally),
                colorFilter = ColorFilter.tint(moduPoint)
            )
        }
        Spacer(Modifier.size(100.dp))
        AnimatedVisibility(
            visible = animationData[1],
            enter = fadeIn(animationSpec = tween(300))
        ) {
            Text("포스트를 업로드 했어요", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = moduBlack, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier.padding(bottom = 18.dp).padding(horizontal = 18.dp)
        ) {
            AnimatedVisibility(
                visible = animationData[2],
                enter = scaleIn(animationSpec = tween(300, easing = EaseOutBack)) + fadeIn(animationSpec = tween(300))
            ) {
                Card(
                    shape = RoundedCornerShape(15.dp),
                    backgroundColor = moduBackground,
                    elevation = 0.dp,
                    modifier = Modifier
                        .bounceClick {

                        }
                        .fillMaxWidth()
                ) {
                    Text("확인", fontWeight = FontWeight.Bold, color = moduGray_strong, fontSize = 16.sp, modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(18.dp), textAlign = TextAlign.Center)
                }
            }
        }
    }
}