package com.example.modugarden.main.upload.post

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.R
import com.example.modugarden.data.UploadPost
import com.example.modugarden.route.NAV_ROUTE_UPLOAD_CURATION
import com.example.modugarden.route.NAV_ROUTE_UPLOAD_POST
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UploadPostViewModel
import com.skydoves.landscapist.glide.GlideImage
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UploadPostImageListScreen(
    navController: NavHostController,
    data: UploadPost,
    uploadPostViewModel: UploadPostViewModel
) {
    val focusManager = LocalFocusManager.current
    val keyboard by keyboardAsState()
    val mContext = LocalContext.current

    val imageData = data.image

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { //이미지 저장.
        for(i in it) {
            uploadPostViewModel.addImage(i)
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .addFocusCleaner(focusManager)
                .fillMaxSize()
        ) {
            TopBar(
                main = false,
                title = "선택한 사진",
                titleIcon = R.drawable.ic_arrow_left_bold,
                titleIconSize = 20.dp,
                titleIconOnClick = {
                    navController.popBackStack()
                },
                icon1 = R.drawable.ic_topbar_plus,
                onClick1 = {
                    galleryLauncher.launch("image/*")
                },
                iconTint1 = moduGray_strong,
                icon2 = R.drawable.ic_topbar_xmark,
                onClick2 = {
                    uploadPostViewModel.removeRangeImage(data.image.size - 1)
                },
                iconTint2 = moduGray_strong
            )
            if(imageData.isEmpty()) { //아직 이미지를 고르지 않았을때 표시할 화면.
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.ic_upload_image_mountain),
                    contentDescription = null,
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text("사진을 고르면 이곳에 표시돼요\n지금 사진을 골라보세요", fontSize = 15.sp, color = moduGray_strong, modifier = Modifier.align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    SmallButton(
                        text = "사진 고르기",
                        onClick = {
                            galleryLauncher.launch("image/*")
                        },
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            else {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 60.dp),
                    cells = GridCells.Fixed(3),
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        top = 16.dp,
                        end = 12.dp,
                        bottom = 16.dp
                    ),
                    content = {
                        itemsIndexed(data.image) { index, item ->
                            Card(
                                backgroundColor = Color.White,
                                elevation = 0.dp,
                                modifier = Modifier
                                    .bounceClick {
                                        uploadPostViewModel.removeImage(index) //선택한 사진에서 누른 사진을 지우기.
                                    },
                            ) {
                                GlideImage(
                                    imageModel = item,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .border(BorderStroke(1.dp, moduGray_light)),
                                    requestOptions = {
                                        RequestOptions()
                                            .override(256,256)
                                    }
                                )
                            }
                        }
                    }
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
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
                        if (imageData.isNotEmpty()) {
                            navController.navigate(NAV_ROUTE_UPLOAD_POST.IMAGEEDIT.routeName)
                        }
                    }
                    .padding(18.dp)
                    .fillMaxWidth()
                    .alpha(if (imageData.isNotEmpty()) 1f else 0.4f),
                shape = RoundedCornerShape(15.dp),
                backgroundColor = moduPoint,
                elevation = 0.dp
            ) {
                Text(
                    "다음",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(18.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}