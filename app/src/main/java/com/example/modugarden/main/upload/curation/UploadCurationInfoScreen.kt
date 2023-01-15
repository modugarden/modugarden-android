package com.example.modugarden.main.upload.curation

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.data.UploadCuration
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.route.NAV_ROUTE_UPLOAD
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UploadCurationViewModel
import com.skydoves.landscapist.glide.GlideImage
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun UploadCurationInfoScreen(
    navController: NavHostController,
    title: String,
    category: String,
    uploadCurationViewModel: UploadCurationViewModel,
    data: UploadCuration
) {
    val focusManager = LocalFocusManager.current
    val keyboard by keyboardAsState()
    val dpScale = animateDpAsState(if (keyboard.toString() == "Closed") 18.dp else 0.dp)
    val shapeScale = animateDpAsState(if (keyboard.toString() == "Closed") 10.dp else 0.dp)

    val uriData = remember { mutableStateOf(data.uri) } //큐레이션 주소 데이터 저장.
    val uriFocused = remember { mutableStateOf(false) } //큐레이션 주소 textField가 포커싱 되어 있는지 확인.

    val scrollState = rememberScrollState() //스크롤 상태 기억.
    var imageData by remember { mutableStateOf(data.image) } //이미지 데이터가 있는지 확인.

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { //이미지 저장.
        imageData = it
        uploadCurationViewModel.saveImage(it)
    }

    val mContext = LocalContext.current

    Log.d("composeimagepick", imageData.toString())
    uploadCurationViewModel.saveUri(uriData.value) //textField의 값이 변할 때마다 리컴포지션을 하면서, viewModel에 큐레이션 주소를 저장한다.

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .addFocusCleaner(focusManager)
        ) {
            TopBar(
                title = "큐레이션 업로드",
                titleIcon = R.drawable.ic_arrow_left,
                titleIconOnClick = {
                    (mContext as Activity).finish() //UploadCurationActivity 창 끄기.
                }
            )
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState, reverseScrolling = true)
            ) {
                Box() {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .background(moduBackground)
                            .bounceClick {
                                //Image Picker 불러오기
                                galleryLauncher.launch("image/*")
                            },
                        backgroundColor = moduBackground,
                        elevation = 0.dp,
                        shape = RectangleShape
                    ) {
                        Box(
                            modifier = Modifier
                        ) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_plus_curation),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(50.dp)
                                        .height(50.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text("사진 추가", color = moduGray_strong, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.align(
                                    Alignment.CenterHorizontally))
                            }
                        }
                    }
                    if(imageData.isNotEmpty()) { //사진이 입력 되었을 때, 사진 표시하기.
                        GlideImage(
                            imageData[0],
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        )
                        Card(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(18.dp)
                                .bounceClick {
                                             galleryLauncher.launch("image/*")
                                },
                            shape = RoundedCornerShape(10.dp),
                            backgroundColor = moduGray_strong,
                            border = BorderStroke(0.5.dp, moduGray_normal),
                            elevation = 0.dp
                        ) {
                            Text("사진 바꾸기", modifier = Modifier.padding(18.dp), color = Color.White, fontSize = 14.sp)
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawLine(
                                color = moduGray_light,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = moduBlack)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(category, fontSize = 14.sp, color = moduGray_strong)
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(18.dp)
                        .padding(bottom = 50.dp)
                ) {
                    EditText(
                        data = uriData,
                        title = "큐레이션 주소",
                        isTextFieldFocused = uriFocused,
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
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
                        if (uriData.value != "" && imageData.isNotEmpty()) {
                            val encodedUrl = URLEncoder.encode(uriData.value, StandardCharsets.UTF_8.toString())
                            navController.navigate(NAV_ROUTE_UPLOAD.CURATION_WEB.routeName + "/${encodedUrl}")
                        }
                    }
                    .padding(dpScale.value)
                    .fillMaxWidth()
                    .alpha(if (uriData.value != "" && imageData.isNotEmpty()) 1f else 0.4f),
                shape = RoundedCornerShape(shapeScale.value),
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