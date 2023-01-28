package com.example.modugarden.main.profile

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap.Companion.Square
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.modugarden.R
import com.example.modugarden.data.CurationCard
import com.example.modugarden.data.PostCard
import com.example.modugarden.data.User
import com.example.modugarden.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun ProfileTab (
    // 포스트 리스트
    postList: List<PostCard> = user.post!!
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
                Image(
                    painter = painterResource(id = postCard.image),
                    contentDescription = null,
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(15.dp))
                        .background(moduBackground),
                    contentScale =
                    if(postCard.category == "Upload") {
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
@Preview(showBackground = true)
@Composable
fun CuratorProfileTab(
    // 포스트 및 큐레이션 리스트
    postList: List<PostCard> = user.post!!,
    curationList: List<CurationCard> = user.curation!!
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
                            Image(
                                painter = painterResource(id = postCard.image),
                                contentDescription = null,
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(15.dp))
                                        .background(moduBackground),
                                contentScale =
                                if(postCard.category == "Upload") {
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
                            Image(
                                painter = painterResource(id = curationCard.thumbnail_image),
                                contentDescription = null,
                                modifier =
                                Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(moduBackground),
                                contentScale =
                                if(curationCard.category == "Upload") {
                                    ContentScale.None
                                }
                                else {
                                    ContentScale.Crop
                                }
                            )
                            Spacer(modifier = Modifier.width(18.dp))
                            if(curationCard.category == "Upload") {
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
                                        text = "${curationCard.category}, ${curationCard.time}",
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