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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.data.UploadCuration
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.route.NAV_ROUTE_UPLOAD_CURATION
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UploadCurationViewModel
import com.skydoves.landscapist.glide.GlideImage
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun UploadCurationImageInfoScreen(
    navController: NavHostController,
    uploadCurationViewModel: UploadCurationViewModel,
    data: UploadCuration
) {
    val focusManager = LocalFocusManager.current
    val keyboard by keyboardAsState()
    val dpScale = animateDpAsState(if (keyboard.toString() == "Closed") 18.dp else 0.dp)
    val shapeScale = animateDpAsState(if (keyboard.toString() == "Closed") 10.dp else 0.dp)

    val uriData = remember { mutableStateOf(data.uri) } //???????????? ?????? ????????? ??????.
    val uriFocused = remember { mutableStateOf(false) } //???????????? ?????? textField??? ????????? ?????? ????????? ??????.

    val scrollState = rememberScrollState() //????????? ?????? ??????.
    var imageData by remember { mutableStateOf(data.image) } //????????? ???????????? ????????? ??????.

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { //????????? ??????.
        imageData = it
        uploadCurationViewModel.saveImage(it)
    }

    val mContext = LocalContext.current

    Log.d("composeimagepick", imageData.toString())
    uploadCurationViewModel.saveUri(uriData.value) //textField??? ?????? ?????? ????????? ?????????????????? ?????????, viewModel??? ???????????? ????????? ????????????.

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
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState, reverseScrolling = true)
            ) {
                Box() {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .background(Color.White)
                            .bounceClick {
                                //Image Picker ????????????
                                galleryLauncher.launch("image/*")
                            },
                        backgroundColor = Color.White,
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
                                    painter = painterResource(id = R.drawable.ic_upload_image_mountain),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(50.dp)
                                        .height(50.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Text("????????? ????????? ????????? ????????????\n?????? ????????? ???????????????", color = moduGray_strong, fontSize = 15.sp, modifier = Modifier.align(
                                    Alignment.CenterHorizontally), textAlign = TextAlign.Center)
                                Spacer(modifier = Modifier.height(24.dp))
                                Card(
                                    backgroundColor = moduPoint,
                                    elevation = 0.dp,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .bounceClick {
                                            galleryLauncher.launch("image/*")
                                        },
                                    shape = RoundedCornerShape(7.dp)
                                ) {
                                    Text("?????? ?????????", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier
                                        .padding(8.dp)
                                        .padding(horizontal = 10.dp)
                                        .align(Alignment.CenterHorizontally))
                                }
                            }
                        }
                    }
                    if(imageData.isNotEmpty()) { //????????? ?????? ????????? ???, ?????? ????????????.
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
                                .width(50.dp)
                                .height(50.dp)
                                .bounceClick {
                                    galleryLauncher.launch("image/*")
                                },
                            shape = CircleShape,
                            backgroundColor = Color.White,
                            border = BorderStroke(1.dp, moduGray_light),
                            elevation = 2.dp
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_photo),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(15.dp)
                                    .height(15.dp)
                                    .padding(15.dp)
                                    .align(Alignment.Center)
                            )
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
                        Text(data.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = moduBlack)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(data.category.toString(), fontSize = 14.sp, color = moduGray_strong)
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(18.dp)
                        .padding(bottom = 50.dp)
                ) {
                    EditText(
                        data = uriData,
                        title = "???????????? ??????",
                        isTextFieldFocused = uriFocused,
                        keyboardType = KeyboardType.Uri
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            moduBlack.copy(alpha = if(imageData.isNotEmpty()) 0.4f else 0f),
                            moduBlack.copy(alpha = 0f)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(18.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_left_bold),
                    contentDescription = null,
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp)
                        .align(Alignment.CenterVertically)
                        .bounceClick {
                            navController.popBackStack()
                        },
                    colorFilter = ColorFilter.tint(if(imageData.isNotEmpty()) Color.White else moduBlack)
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            BottomButton(
                title = "??????",
                dpScale = dpScale.value,
                shapeScale = shapeScale.value,
                alpha = if(uriData.value != "" && imageData.isNotEmpty()) 1f else 0.4f,
                onClick = {
                    if (uriData.value != "" && imageData.isNotEmpty()) {
                        val encodedUrl = URLEncoder.encode(uriData.value.trim(), StandardCharsets.UTF_8.toString()) //trim: ?????? ??????.
                        navController.navigate(NAV_ROUTE_UPLOAD_CURATION.WEB.routeName + "/${encodedUrl}")
                    }
                }
            )
        }
    }
}