package com.example.modugarden.main.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.modugarden.R
import com.example.modugarden.ui.theme.ShowProgressBar
import com.example.modugarden.ui.theme.TopBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.skydoves.landscapist.glide.GlideImage


class ProfileImageDetailActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageUrl = intent.getStringExtra("imageUrl")
        setContent {
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
                        finish()
                    },
                    titleIconTint = Color.White,
//            icon1 = R.drawable.ic_cross_line_bold_gray,
//            iconTint1 = Color.White,
//            onClick1 = {
//                deleteState.value = true
//                scope.launch {
//                    delay(200)
//                    navController.popBackStack()
//                    uploadPostViewModel.removeImage(index)
//                }
//            },
                    backgroundColor = Color.Black,
                    bottomLine = false
                )
                GlideImage(
                    imageModel = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f),
                    loading = {
                        ShowProgressBar()
                    },
                    // shows an error text if fail to load an image.
                    failure = {
                        Text(text = "image request failed.")
                    },
                )

            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun ProfileImageDetail(
//    navController: NavHostController,
    imageUrl: String,
) {

    val deleteState = remember { mutableStateOf(false) }

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

            },
            titleIconTint = Color.White,
//            icon1 = R.drawable.ic_cross_line_bold_gray,
//            iconTint1 = Color.White,
//            onClick1 = {
//                deleteState.value = true
//                scope.launch {
//                    delay(200)
//                    navController.popBackStack()
//                    uploadPostViewModel.removeImage(index)
//                }
//            },
            backgroundColor = Color.Black,
            bottomLine = false
        )
        AnimatedVisibility(
            visible = !deleteState.value,
            exit = scaleOut(
                animationSpec = tween(durationMillis = 200, easing = EaseInCirc)
            ) + fadeOut(
                animationSpec = tween(durationMillis = 150, easing = FastOutLinearInEasing)
            ),
            modifier = Modifier.align(Alignment.Center)
        ) {
            GlideImage(
                imageModel = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f),
                loading = {
                    ShowProgressBar()
                },
                // shows an error text if fail to load an image.
                failure = {
                    Text(text = "image request failed.")
                },
            )
        }
    }
}