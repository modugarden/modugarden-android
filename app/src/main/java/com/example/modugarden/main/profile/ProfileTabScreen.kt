package com.example.modugarden.main.profile

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap.Companion.Square
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.modugarden.ApplicationClass.Companion.clientId
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import com.example.modugarden.R
import com.example.modugarden.api.AuthCallBack
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.*
import com.example.modugarden.api.dto.PostDTO.*
import com.example.modugarden.data.CurationCard
import com.example.modugarden.data.PostCard
import com.example.modugarden.data.User
import com.example.modugarden.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

@Composable
fun ProfileTab (
    postList: List<GetUserPostResponseContent>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawLine(
                    color = moduGray_light,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            },
        contentPadding = PaddingValues(18.dp)
    ) {
        items(postList) { postCard ->
            // 이미지가 들어간 버튼을 넣어야 함
            Box(modifier = Modifier.bounceClick {

            }) {
                GlideImage(
                    imageModel =
                    if(postCard.id == 0) {
                        R.drawable.plus
                    }
                    else {
                        postCard.image
                    },
                    contentDescription = null,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(15.dp))
                            .background(moduBackground),
                    contentScale =
                    if(postCard.id == 0) {
                        ContentScale.None
                    }
                    else {
                        ContentScale.Crop
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CuratorProfileTab(
    postList: List<GetUserPostResponseContent>,
    curationList: List<GetUserCurationsResponseContent>
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.White,
        contentColor = moduBlack,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState,tabPositions),
                color = moduBlack
            )
        }
    ) {
        pages.forEachIndexed { index, title ->
            Tab(
                text = {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        color =
                        if(pagerState.currentPage == index) moduBlack
                        else moduGray_strong
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }

    HorizontalPager(
        count = pages.size,
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
    ) { page ->
        when(page) {
            0 -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(18.dp)
                ) {
                    items(postList) { postCard ->
                        // 이미지가 들어간 버튼을 넣어야 함
                        Box(modifier = Modifier.bounceClick {

                        }) {
                            GlideImage(
                                imageModel =
                                if(postCard.id == 0) {
                                    R.drawable.plus
                                }
                                else {
                                    postCard.image
                                },
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(moduBackground),
                                contentScale =
                                if(postCard.id == 0) {
                                    ContentScale.None
                                }
                                else {
                                    ContentScale.Crop
                                }
                            )
                        }
                    }
                }
            }
            1 -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    contentPadding = PaddingValues(18.dp)
                ) {
                    items(curationList) { curationCard ->
                        Row(
                            modifier = Modifier
                                .height(90.dp)
                                .bounceClick {

                                }
                        ) {
                            GlideImage(
                                imageModel = if(curationCard.id == 0) {
                                    R.drawable.plus
                                }
                                else {
                                    curationCard.image
                                },
                                contentDescription = null,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(moduBackground),
                                contentScale =
                                if(curationCard.id == 0) {
                                    ContentScale.None
                                }
                                else {
                                    ContentScale.Crop
                                }
                            )
                            Spacer(modifier = Modifier.width(18.dp))
                            if(curationCard.id == 0) {
                                Text(
                                    text = curationCard.title,
                                    style = TextStyle(
                                        color = moduGray_strong,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                            else {
                                Column(
                                    modifier = Modifier
                                        .height(42.dp)
                                        .align(Alignment.CenterVertically)
                                ) {
                                    Text(
                                        text = curationCard.title,
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "${curationCard.category}, ${curationCard.created_date}",
                                        fontSize = 12.sp,
                                        color = moduGray_strong
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun StoredTab(
    postList: List<GetStoredPostResponseContent>,
    curationList: List<GetStoredCurationsResponseContent>
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.White,
        contentColor = moduBlack,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState,tabPositions),
                color = moduBlack
            )
        }
    ) {
        pages.forEachIndexed { index, title ->
            Tab(
                text = {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        color =
                        if(pagerState.currentPage == index) moduBlack
                        else moduGray_strong
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }

    HorizontalPager(
        count = pages.size,
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
    ) { page ->
        when(page) {
            0 -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(18.dp)
                ) {
                    items(postList) { postCard ->
                        // 이미지가 들어간 버튼을 넣어야 함
                        Box(modifier = Modifier.bounceClick {

                        }) {
                            GlideImage(
                                imageModel =
                                if(postCard.board_id == 0) {
                                    R.drawable.plus
                                }
                                else {
                                    postCard.preview_img
                                },
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(moduBackground),
                                contentScale =
                                if(postCard.board_id == 0) {
                                    ContentScale.None
                                }
                                else {
                                    ContentScale.Crop
                                }
                            )
                        }
                    }
                }
            }
            1 -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    contentPadding = PaddingValues(18.dp)
                ) {
                    items(curationList) { curationCard ->
                        Row(
                            modifier = Modifier
                                .height(90.dp)
                                .bounceClick {

                                }
                        ) {
                            GlideImage(
                                imageModel = if(curationCard.id == 0) {
                                    curationCard.user_id
                                }
                                else {
                                    curationCard.preview_image
                                },
                                contentDescription = null,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(moduBackground),
                                contentScale =
                                if(curationCard.id == 0) {
                                    ContentScale.None
                                }
                                else {
                                    ContentScale.Crop
                                }
                            )
                            Spacer(modifier = Modifier.width(18.dp))
                            if(curationCard.id == 0) {
                                Text(
                                    text = curationCard.title,
                                    style = TextStyle(
                                        color = moduGray_strong,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                            else {
                                Column(
                                    modifier = Modifier
                                        .height(42.dp)
                                        .align(Alignment.CenterVertically)
                                ) {
                                    Text(
                                        text = curationCard.title,
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "${curationCard.category_category}, ${curationCard.localDateTime.split('T')[0]}",
                                        fontSize = 12.sp,
                                        color = moduGray_strong
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}