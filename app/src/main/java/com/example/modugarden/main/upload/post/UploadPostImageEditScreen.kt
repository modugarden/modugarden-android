package com.example.modugarden.main.upload.post

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
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
import com.skydoves.landscapist.glide.GlideImage
import com.example.modugarden.R
import com.example.modugarden.route.NAV_ROUTE_UPLOAD_POST
import com.example.modugarden.ui.theme.*
import com.example.modugarden.ui.theme.EditText

@OptIn(ExperimentalPagerApi::class)
@Composable
fun UploadPostImageEditScreen(
    navController: NavHostController,
    data: UploadPost,
    uploadPostViewModel: UploadPostViewModel
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val descriptionData = remember { data.description }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState, reverseScrolling = true)
                    .addFocusCleaner(focusManager)
                    .background(Color.White)
            ) {
                HorizontalPager(
                    count = data.image.size,
                    modifier = Modifier
                        .wrapContentSize()
                ) { page ->
                    Column() {
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
                        //전용 EditText 필요함. (임시 코드)
                        EditTextUploadPost(hint = "내용을 입력하세요", data = descriptionData, page = page, viewModel = uploadPostViewModel)
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            moduBlack.copy(alpha = 0.4f),
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
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomButton(
                title = "다음",
                onClick = {
                    navController.navigate(NAV_ROUTE_UPLOAD_POST.TAG.routeName)
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
                viewModel.saveDescription(data[page], page)
                Log.d("composedata", data[page])
            },
            shape = RectangleShape,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            placeholder = {
                Text(hint, fontSize = 16.sp, color = moduGray_normal)
            }
        )
    }
}