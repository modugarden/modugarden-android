package com.example.modugarden.main.upload

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import com.example.modugarden.R
import com.example.modugarden.data.Category
import com.example.modugarden.data.UploadPost
import com.example.modugarden.main.upload.curation.UploadCurationActivity
import com.example.modugarden.main.upload.post.UploadPostActivity
import com.example.modugarden.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable //업로드.
fun UploadScreen(navController: NavHostController) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .addFocusCleaner(focusManager)
    ) {
        UploadInfoScreen()
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun UploadInfoScreen() {
    val mContext = LocalContext.current
    val selected = remember { mutableStateOf(0) }
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val pagerStateAuto = rememberPagerState()
    val curator = remember { mutableStateOf(true) }

    LaunchedEffect(key1 = pagerStateAuto) {
        launch {
            while (true) {
                delay(1000)
                with(pagerStateAuto) {
                    val target = if (currentPage < pageCount - 1) currentPage + 1 else 0

                    tween<Float>(
                        durationMillis = 500,
                        easing = EaseOutExpo
                    )
                    animateScrollToPage(page = target)
                }
            }
        }
    }

    Column() {
        TopBar(
            title = "업로드",
            main = true,
            bottomLine = false
        )
        Row(
            Modifier
                .padding(start = 18.dp)
                .padding(bottom = 18.dp)
        ) {
            Text(text = "포스트", fontSize = 20.sp, color = if(pagerState.currentPage == 0) moduBlack else moduGray_normal, fontWeight = FontWeight.Bold, modifier = Modifier.bounceClick {
                scope.launch {
                    pagerState.animateScrollToPage(0)
                }
            })
            Spacer(Modifier.size(20.dp))
            Text(text = "큐레이션", fontSize = 20.sp, color = if(pagerState.currentPage == 1) moduBlack else moduGray_normal, fontWeight = FontWeight.Bold, modifier = Modifier.bounceClick {
                scope.launch {
                    pagerState.animateScrollToPage(1)
                }
            })
        }
        HorizontalPager(count = 2, state = pagerState, modifier = Modifier.weight(1f)) { page ->
            Box(Modifier.fillMaxSize())
            {
                Column(Modifier.align(Alignment.TopCenter)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        shape = RoundedCornerShape(15.dp),
                        backgroundColor = moduBackground,
                        elevation = 0.dp
                    ) {
                        when (page) {
                            0 -> UploadPostEx(pagerStateAuto)
                            1 -> UploadCurationEx()
                        }
                    }
                    Row(Modifier.padding(horizontal = 18.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_information_circle),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            if(page == 0) "사진과 함께 글을 작성할 수 있어요" else "외부 링크를 소개할 수 있어요",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = moduGray_strong,
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    }
                }
            }
        }
        BottomMakeButton(pagerState, curator.value)
    }
}

@Composable
fun UploadCurationEx() {
    Column {
        Card(
            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            backgroundColor = Color.White,
            elevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
                .padding(top = 20.dp)
        ) {
            Row(Modifier.padding(10.dp)) {
                Card(
                    shape = CircleShape,
                    backgroundColor = moduGray_light,
                    modifier = Modifier.size(15.dp),
                    elevation = 0.dp
                ) {}
                Spacer(Modifier.size(7.dp))
                Card(
                    shape = RoundedCornerShape(4.dp),
                    backgroundColor = moduGray_light,
                    modifier = Modifier
                        .width(100.dp)
                        .height(15.dp),
                    elevation = 0.dp
                ) {}
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_up_right),
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
        Image(
            painter = painterResource(id = R.drawable.test_image1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
        Card(
            shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
                .padding(bottom = 20.dp),
            backgroundColor = Color.White,
            elevation = 0.dp
        ) {
            Column {
                Spacer(Modifier.size(10.dp))
                Card(
                    shape = RoundedCornerShape(4.dp),
                    backgroundColor = moduGray_light,
                    elevation = 0.dp,
                    modifier = Modifier
                        .height(15.dp)
                        .width(100.dp)
                        .padding(horizontal = 10.dp)
                ) { }
                Spacer(Modifier.size(7.dp))
                Row(Modifier.padding(horizontal = 10.dp)) {
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        backgroundColor = moduGray_light,
                        elevation = 0.dp,
                        modifier = Modifier
                            .height(15.dp)
                            .width(54.dp)
                    ) { }
                    Spacer(Modifier.weight(1f))
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        backgroundColor = moduGray_light,
                        elevation = 0.dp,
                        modifier = Modifier
                            .height(15.dp)
                            .width(40.dp)
                    ) { }
                }
                Spacer(Modifier.size(10.dp))
                Divider(color = moduGray_light, thickness = 1.dp)
                Spacer(Modifier.size(10.dp))
                Row(
                    Modifier
                        .padding(horizontal = 10.dp)
                        .padding(bottom = 10.dp)) {
                    Card(
                        modifier = Modifier.size(15.dp),
                        shape = CircleShape,
                        backgroundColor = moduGray_light,
                        elevation = 0.dp
                    ) {}
                    Spacer(Modifier.size(7.dp))
                    Card(
                        modifier = Modifier.size(15.dp),
                        shape = CircleShape,
                        backgroundColor = moduGray_light,
                        elevation = 0.dp
                    ) {}
                    Spacer(Modifier.weight(1f))
                    Card(
                        modifier = Modifier.size(15.dp),
                        shape = CircleShape,
                        backgroundColor = moduGray_light,
                        elevation = 0.dp
                    ) {}
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun UploadPostEx(
    pagerStateAuto: PagerState
) {
    Column {
        Card(
            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            backgroundColor = Color.White,
            elevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
                .padding(top = 20.dp)
        ) {
            Row(Modifier.padding(10.dp)) {
                Card(
                    shape = CircleShape,
                    backgroundColor = moduGray_light,
                    modifier = Modifier.size(15.dp),
                    elevation = 0.dp
                ) {}
                Spacer(Modifier.size(7.dp))
                Card(
                    shape = RoundedCornerShape(4.dp),
                    backgroundColor = moduGray_light,
                    modifier = Modifier
                        .width(100.dp)
                        .height(15.dp),
                    elevation = 0.dp
                ) {}
            }
        }
        HorizontalPager(
            count = 5, userScrollEnabled = false, reverseLayout = true, itemSpacing = 0.dp, state = pagerStateAuto,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            Image(
                painter = painterResource(id =
                when(page) {
                    0 -> R.drawable.test_image1
                    1 -> R.drawable.test_image2
                    2 -> R.drawable.test_image3
                    3 -> R.drawable.test_image4
                    4 -> R.drawable.test_image5
                    else -> R.drawable.test_image1
                }),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
        }
        Card(
            shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
                .padding(bottom = 20.dp),
            backgroundColor = Color.White,
            elevation = 0.dp
        ) {
            Column {
                Spacer(Modifier.size(10.dp))
                Card(
                    shape = RoundedCornerShape(4.dp),
                    backgroundColor = moduGray_light,
                    elevation = 0.dp,
                    modifier = Modifier
                        .height(15.dp)
                        .width(100.dp)
                        .padding(horizontal = 10.dp)
                ) { }
                Spacer(Modifier.size(7.dp))
                Row(Modifier.padding(horizontal = 10.dp)) {
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        backgroundColor = moduGray_light,
                        elevation = 0.dp,
                        modifier = Modifier
                            .height(15.dp)
                            .width(54.dp)
                    ) { }
                    Spacer(Modifier.weight(1f))
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        backgroundColor = moduGray_light,
                        elevation = 0.dp,
                        modifier = Modifier
                            .height(15.dp)
                            .width(40.dp)
                    ) { }
                }
                Spacer(Modifier.size(10.dp))
                Divider(color = moduGray_light, thickness = 1.dp)
                Spacer(Modifier.size(10.dp))
                Row(
                    Modifier
                        .padding(horizontal = 10.dp)
                        .padding(bottom = 10.dp)) {
                    Card(
                        modifier = Modifier.size(15.dp),
                        shape = CircleShape,
                        backgroundColor = moduGray_light,
                        elevation = 0.dp
                    ) {}
                    Spacer(Modifier.size(7.dp))
                    Card(
                        modifier = Modifier.size(15.dp),
                        shape = CircleShape,
                        backgroundColor = moduGray_light,
                        elevation = 0.dp
                    ) {}
                    Spacer(Modifier.size(7.dp))
                    Card(
                        modifier = Modifier.size(15.dp),
                        shape = CircleShape,
                        backgroundColor = moduGray_light,
                        elevation = 0.dp
                    ) {}
                    Spacer(Modifier.weight(1f))
                    Card(
                        modifier = Modifier.size(15.dp),
                        shape = CircleShape,
                        backgroundColor = moduGray_light,
                        elevation = 0.dp
                    ) {}
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BottomMakeButton(
    pagerState: PagerState,
    curator: Boolean
) {
    val mContext = LocalContext.current
    val alpha = animateFloatAsState(if (pagerState.currentPage == 0) 1f else if (curator) 1f else 0.4f)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .padding(bottom = 18.dp)
            .alpha(alpha.value)
            .bounceClick {
                if (pagerState.currentPage == 0) mContext.startActivity(
                    Intent(
                        mContext,
                        UploadPostActivity::class.java
                    )
                )
                else if(curator) mContext.startActivity(Intent(mContext, UploadCurationActivity::class.java))
            },
        backgroundColor = moduPoint,
        shape = RoundedCornerShape(15.dp),
        elevation = 0.dp
    ) {
        Box(
            Modifier
                .padding(18.dp)
                .fillMaxWidth()
        ) {
            AnimatedVisibility(
                visible = pagerState.currentPage == 0,
                enter = slideInVertically(animationSpec = tween(300, easing = EaseOutExpo)) + fadeIn(animationSpec = tween(300)),
                exit = slideOutVertically(animationSpec = tween(300, easing = EaseOutExpo)) + fadeOut(animationSpec = tween(200))
            ) {
                Text("포스트 만들기", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
            AnimatedVisibility(
                visible = pagerState.currentPage == 1,
                enter = slideInVertically(animationSpec = tween(300, easing = EaseOutExpo)) + fadeIn(animationSpec = tween(300)),
                exit = slideOutVertically(animationSpec = tween(300, easing = EaseOutExpo)) + fadeOut(animationSpec = tween(200))
            ) {
                Text("큐레이션 만들기", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        }
    }
}

//카테고리 설정 버튼
@Composable
fun EditTextLikeButton(
    title: String,
    data: MutableState<Category>,
    isTextFieldFocused: MutableState<Boolean>,
) {
    val mContext = LocalContext.current

    val focusRequester = remember { FocusRequester() }
    Column {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (isTextFieldFocused.value) moduPoint else moduGray_strong)
        Spacer(modifier = Modifier.height(5.dp))
        Card(
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .bounceClick {
                },
            elevation = 0.dp,
            backgroundColor = if(isTextFieldFocused.value) moduTextFieldPoint else moduBackground,
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(
                text = data.value.toString(),
                color = moduBlack,
                fontSize = 16.sp,
                modifier = Modifier.padding(15.dp)
            )
        }
    }
}