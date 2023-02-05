package com.example.modugarden.main.upload.post

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.data.UploadPost
import com.example.modugarden.viewmodel.UploadPostViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.example.modugarden.R
import com.example.modugarden.api.dto.CurationUploadResponse
import com.example.modugarden.api.RetrofitBuilder.postCreateAPI
import com.example.modugarden.main.upload.curation.UriUtil.toFile
import com.example.modugarden.route.NAV_ROUTE_UPLOAD_POST
import com.example.modugarden.ui.theme.*
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@Composable
fun UploadPostImageEditScreen(
    navController: NavHostController,
    data: UploadPost,
    uploadPostViewModel: UploadPostViewModel
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val descriptionData = remember { data.description }
    val locationData = remember { data.location }

    val scope = rememberCoroutineScope()

    val mContext = LocalContext.current

    val pagerState = rememberPagerState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState, reverseScrolling = true)
        ) {
            Box() {
                Column(
                    modifier = Modifier
                        .addFocusCleaner(focusManager)
                        .background(Color.White)
                        .fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalPager(
                        count = data.image.size,
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        state = pagerState
                    ) { page ->
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                            ) {
                                Box(
                                    modifier = Modifier.wrapContentSize()
                                ) {
                                    GlideImage(
                                        imageModel = data.image[page],
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(1f),
                                        requestOptions = {
                                            RequestOptions()
                                                .override(700,700)
                                                .downsample(DownsampleStrategy.FIT_CENTER)
                                        }
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .drawBehind {
                                            drawLine(
                                                color = moduGray_light,
                                                start = Offset(0f, size.height),
                                                end = Offset(size.width, size.height),
                                                strokeWidth = 2.dp.toPx()
                                            )
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(18.dp)
                                            .bounceClick {
                                                //위치 태그 추가 창으로 이동
                                                navController.navigate(NAV_ROUTE_UPLOAD_POST.TAGLOCATION.routeName + "/${page}")
                                            }
                                    ) {
                                        //위치 아이콘
                                        Image(
                                            painter = painterResource(if(locationData[page].isNotEmpty()) R.drawable.ic_location_with_circle else R.drawable.ic_plus_curation),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(40.dp)
                                        )
                                        Spacer(Modifier.size(18.dp))
                                        Text(if(locationData[page].isNotEmpty()) locationData[page].split(",")[0] else "위치 태그 추가", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if(locationData[page].isNotEmpty()) moduBlack else moduBlack, modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .weight(1f))
                                        Spacer(Modifier.size(18.dp))
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_chevron_right_bold),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(20.dp)
                                                .align(Alignment.CenterVertically)
                                        )
                                    }
                                }
                                EditTextUploadPost(hint = "일상, 사진 설명 등을 기록해요", data = descriptionData, page = page, viewModel = uploadPostViewModel)
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                //사진 인디케이터
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                ) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 18.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        moduBlack.copy(alpha = 0f),
                                        moduBlack.copy(alpha = 0.2f)
                                    )
                                )
                            )
                            .align(Alignment.BottomCenter)
                            .padding(25.dp), horizontalArrangement = Arrangement.Center

                    ) {
                        items(data.image.size) { index ->
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (index == pagerState.currentPage) Color.White else Color.White.copy(
                                            alpha = 0.4f
                                        )
                                    )
                            )

                            if (index != data.image.size - 1) {
                                Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                            }
                        }
                    }
                }
            }
        }
        //상단 바
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            moduBlack.copy(alpha = 0.2f),
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
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
        //하단 바
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            BottomButton(
                title = if(pagerState.currentPage < data.image.size - 1) "다음" else "업로드",
                onClick = {
                    if (pagerState.currentPage == data.image.size - 1) {
                        navController.navigate(NAV_ROUTE_UPLOAD_POST.UPLOADING.routeName) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                    else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun EditTextUploadPost(
    hint: String = "",
    data: SnapshotStateList<String>,
    modifier: Modifier = Modifier
        .fillMaxWidth(),
    keyboardType: KeyboardType = KeyboardType.Text, //키보드 형식 (비밀번호, 이메일 등.)
    textStyle: TextStyle = TextStyle(fontSize = 16.sp, color = moduBlack), //textField의 글자 스타일 설정.
    viewModel: UploadPostViewModel,
    page: Int
) {
    val focusRequester = remember { FocusRequester() }
    Column {
        TextField(
            modifier = modifier
                .focusRequester(focusRequester)
                .animateContentSize(),
            value = data[page],
            onValueChange = { textValue ->
                data[page] = textValue
                viewModel.addDescription(data[page], page)
                Log.d("composedata", data[page])
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            placeholder = {
                Text(hint, fontSize = 16.sp, color = moduGray_normal)
            },
        )
    }
}
