package com.example.modugarden.main.upload.post

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.R
import com.example.modugarden.data.UploadPost
import com.example.modugarden.route.NAV_ROUTE_UPLOAD_POST
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UploadPostViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun UploadPostImageListScreen(
    navController: NavHostController,
    data: UploadPost,
    uploadPostViewModel: UploadPostViewModel
) {
    val focusManager = LocalFocusManager.current
    val keyboard by keyboardAsState()
    val mContext = LocalContext.current

    val deleteState = remember { mutableStateOf(false) } //이미지를 삭제할 수 있는 상태인지.
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val imageData = data.image
    val locationData = data.location

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { //이미지 저장.
        for(i in it) {
            uploadPostViewModel.addImage(i)
        }
    }

    if(data.image.isEmpty()) deleteState.value = false

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
                title = "",
                titleIcon = R.drawable.ic_arrow_left_bold,
                titleIconSize = 20.dp,
                titleIconOnClick = {
                    navController.popBackStack()
                },
                bottomLine = false,
                icon1 = R.drawable.ic_topbar_plus,
                onClick1 = {
                    galleryLauncher.launch("image/*")
                },
                iconTint1 = moduGray_strong,
                icon2 = if(data.image.isNotEmpty()) R.drawable.ic_topbar_xmark else 0,
                onClick2 = {
                    deleteState.value = !deleteState.value
                },
                iconTint2 = moduGray_strong
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .padding(top = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("사진 선택", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = moduBlack)
                Spacer(modifier = Modifier.width(10.dp))
                Card(
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = moduGray_light
                ) {
                    Text(data.image.size.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = moduGray_strong,
                    modifier = Modifier
                        .padding(3.dp)
                        .padding(horizontal = 5.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                Box() {
                    Text("", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
                }
                //사진 모두 삭제 버튼
                AnimatedVisibility(
                    visible = deleteState.value,
                    enter = fadeIn(animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 150, easing = FastOutLinearInEasing))
                ) {
                    SmallButton(
                        text = "모두 삭제",
                        fontSize = 12,
                        backgroundColor = if(deleteState.value) moduErrorBackgroundPoint else Color.White,
                        textColor = if(deleteState.value) moduErrorPoint else Color.White,
                        onClick = {
                            if(imageData.isNotEmpty()) {
                                uploadPostViewModel.removeRangeImage(imageData.size)
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
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
                    SmallButton(text = "사진 고르기") {
                        galleryLauncher.launch("image/*")
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
                Spacer(modifier = Modifier.weight(1f))
            }
            else {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 60.dp),
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        top = 16.dp,
                        end = 12.dp,
                        bottom = 16.dp
                    ),
                    content = {
                        itemsIndexed(data.image) { index, item ->
                            UploadPostImageListItem(
                                index = index,
                                data = data,
                                deleteState = deleteState,
                                navController = navController,
                                uploadPostViewModel = uploadPostViewModel,
                            )
                        }
                    }
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
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
                            if(imageData.isNotEmpty()) {
                                if(imageData.size <= 10) {
                                    navController.navigate(NAV_ROUTE_UPLOAD_POST.IMAGEEDIT.routeName)
                                }
                                else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("사진은 10개까지 추가할 수 있어요", duration = SnackbarDuration.Short)
                                    }
                                }
                            }
                        }
                        .padding(18.dp)
                        .fillMaxWidth(),
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
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(30.dp)
        ) {
            SnackBar(snackbarHostState = snackbarHostState)
        }
    }
}
//사진 리스트 아이템
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UploadPostImageListItem(
    index: Int,
    data: UploadPost,
    deleteState: MutableState<Boolean>,
    navController: NavHostController,
    uploadPostViewModel: UploadPostViewModel,
) {

    Box() {
        Card(
            modifier = Modifier
                .bounceClick {
                    Log.d("composeindex", data.image.size.toString())
                    navController.navigate(NAV_ROUTE_UPLOAD_POST.IMAGEDETAIL.routeName + "/${index}")
                }
                .padding(8.dp),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, moduGray_light)
        ) {
            GlideImage(
                imageModel = data.image[index],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f),
                requestOptions = {
                    RequestOptions()
                        .override(256,256)
                },
                loading = {
                    ShowProgressBar()
                }
            )
            //사진 삭제 버튼
            AnimatedVisibility(
                visible = deleteState.value,
                enter = scaleIn(
                    animationSpec = tween(durationMillis = 200, easing = EaseOutCirc)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 50, easing = FastOutLinearInEasing)
                ),
                exit = scaleOut(
                    animationSpec = tween(durationMillis = 100, easing = FastOutLinearInEasing)
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 50, easing = FastOutLinearInEasing)
                )
            ) {
                Card(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .padding(5.dp)
                        .align(Alignment.TopEnd)
                        .bounceClick {
                            if (deleteState.value) {

                                uploadPostViewModel.removeImage(index) //누른 사진을 삭제합니다.
                            }
                        },
                    backgroundColor = Color.White,
                    border = BorderStroke(1.dp, moduGray_light),
                    shape = CircleShape
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_xmark),
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                            .padding(5.dp)
                            .align(Alignment.Center),
                        colorFilter = ColorFilter.tint(moduErrorPoint)
                    )
                }
            }
        }
    }
}