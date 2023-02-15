package com.example.modugarden.main.upload.curation

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduPoint
import kotlinx.coroutines.delay


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UploadCurationUploadSuccessfully(
    navController: NavHostController,
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

    Column(Modifier.background(Color.White)) {
        Spacer(Modifier.weight(1f))
        Box(Modifier.align(Alignment.CenterHorizontally)) {
            UploadPostUploadSuccessfullyIcon(animationData = animationData)
        }
        Spacer(Modifier.size(100.dp))
        Box(Modifier.align(Alignment.CenterHorizontally)) {
            Text("큐레이션을 업로드 했어요", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            UploadCurationUploadSuccessfullyTitle(animationData = animationData)
        }
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier
                .padding(bottom = 18.dp)
                .padding(horizontal = 18.dp)
        ) {
            Box() {
                Card(
                    shape = RoundedCornerShape(15.dp),
                    backgroundColor = Color.White,
                    elevation = 0.dp,
                    modifier = Modifier
                        .bounceClick {}
                        .fillMaxWidth()
                ) {
                    Text("확인", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp, modifier = Modifier
                        .padding(18.dp), textAlign = TextAlign.Center)
                }
                UploadCurationUploadSuccessfullyButton(animationData = animationData, navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UploadPostUploadSuccessfullyIcon(
    animationData: MutableList<Boolean>
) {
    Box() {
        Box(Modifier.size(100.dp))
        AnimatedVisibility(
            visible = animationData[0],
            enter = scaleIn(animationSpec = tween(500, easing = EaseOutExpo)) + fadeIn(tween(300)),
            modifier = Modifier.size(100.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_check_solid),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center),
                colorFilter = ColorFilter.tint(moduPoint)
            )
        }
    }
}

@Composable
fun UploadCurationUploadSuccessfullyTitle(
    animationData: MutableList<Boolean>
) {
    AnimatedVisibility(
        visible = animationData[1],
        enter = fadeIn(animationSpec = tween(300))
    ) {
        Text("큐레이션을 업로드 했어요", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = moduBlack, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UploadCurationUploadSuccessfullyButton(
    animationData: MutableList<Boolean>,
    navController: NavHostController
) {
    val mContext = LocalContext.current

    AnimatedVisibility(
        visible = animationData[2],
        enter = scaleIn(animationSpec = tween(300, easing = EaseOutExpo)) + fadeIn(animationSpec = tween(300))
    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            backgroundColor = moduPoint,
            elevation = 0.dp,
            modifier = Modifier
                .bounceClick {
                    (mContext as Activity).finish()
                }
                .fillMaxWidth()
        ) {
            Text("확인", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp, modifier = Modifier
                .padding(18.dp), textAlign = TextAlign.Center)
        }
    }
}