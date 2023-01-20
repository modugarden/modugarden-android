package com.example.modugarden.main.upload.post

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.data.UploadPost
import com.example.modugarden.ui.theme.TopBar
import com.example.modugarden.viewmodel.UploadPostViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun UploadPostImageDetail(
    navController: NavHostController,
    uploadPostViewModel: UploadPostViewModel,
    data: UploadPost,
    index: Int
) {

    val deleteState = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        TopBar(
            title = "",
            titleIcon = R.drawable.ic_arrow_left_bold,
            titleIconSize = 20.dp,
            titleIconOnClick = {
                navController.popBackStack()
            },
            titleIconTint = Color.White,
            icon1 = R.drawable.ic_arrow_left_bold,
            iconTint1 = Color.White,
            onClick1 = {
                deleteState.value = true
                scope.launch {
                    delay(200)
                    navController.popBackStack()
                    uploadPostViewModel.removeImage(index)
                    if(data.description.size - 1 >= index) {
                        uploadPostViewModel.removeDescription(index)
                    }
                }
            },
            backgroundColor = Color.Black,
            bottomLine = false
        )
        AnimatedVisibility(
            visible = !deleteState.value,
            exit = scaleOut(
                animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
            ) + fadeOut(
                animationSpec = tween(durationMillis = 100, easing = FastOutLinearInEasing)
            ),
            modifier = Modifier.align(Alignment.Center)
        ) {
            GlideImage(
                imageModel = if(data.image.size - 1 >= index) data.image[index] else Color.Black,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        }
    }
}