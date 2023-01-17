package com.example.modugarden.main.upload.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.data.UploadPost
import com.example.modugarden.ui.theme.TopBar
import com.example.modugarden.viewmodel.UploadPostViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.skydoves.landscapist.glide.GlideImage
import com.example.modugarden.R
import com.example.modugarden.ui.theme.EditText

@OptIn(ExperimentalPagerApi::class)
@Composable
fun UploadPostImageEditScreen(
    navController: NavHostController,
    data: UploadPost,
    uploadPostViewModel: UploadPostViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column() {
            TopBar(
                title = "설명 추가",
                titleIcon = R.drawable.ic_arrow_left_bold,
                titleIconSize = 20.dp,
                titleIconOnClick = {
                    navController.popBackStack()
                },
            )
            HorizontalPager(
                count = data.image.size,
                modifier = Modifier
                    .wrapContentSize()
            ) { page ->
                GlideImage(
                    imageModel = data.image[page],
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    requestOptions = {
                        RequestOptions()
                            .override(1000,1000)
                            .downsample(DownsampleStrategy.FIT_CENTER)
                    }
                )
            }
        }
    }
}