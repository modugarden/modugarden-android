package com.example.modugarden.main.profile

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

val pages = listOf("포스트", "큐레이션")

val postResponse = listOf(
    PostCard(
        R.drawable.test_image1, "타이틀1", "카테고리1",
        "2023년 1월 6일", "유저1"
    ),
    PostCard(
        R.drawable.test_image2, "타이틀2", "카테고리2",
        "2023년 1월 6일", "유저2"
    ),
    PostCard(
        R.drawable.test_image3, "타이틀3", "카테고리3",
        "2023년 1월 6일", "유저3"
    ),
    PostCard(
        R.drawable.test_image4, "타이틀4", "카테고리4",
        "2023년 1월 6일", "유저4"
    ),
    PostCard(
        R.drawable.test_image5, "타이틀5", "카테고리5",
        "2023년 1월 6일", "유저5"
    ),
    PostCard(
        R.drawable.test_image1, "타이틀6", "카테고리6",
        "2023년 1월 6일", "유저6"
    ),
    PostCard(
        R.drawable.test_image2, "타이틀7", "카테고리7",
        "2023년 1월 6일", "유저7"
    ),
    PostCard(
        R.drawable.test_image3, "타이틀7", "카테고리7",
        "2023년 1월 6일", "유저7"
    ),
    PostCard(
        R.drawable.test_image4, "타이틀7", "카테고리7",
        "2023년 1월 6일", "유저7"
    ),
    PostCard(
        R.drawable.test_image5, "타이틀7", "카테고리7",
        "2023년 1월 6일", "유저7"
    ),
    PostCard(
        R.drawable.test_image1, "타이틀7", "카테고리7",
        "2023년 1월 6일", "유저7"
    ),
    PostCard(
        R.drawable.test_image2, "타이틀7", "카테고리7",
        "2023년 1월 6일", "유저7"
    ),
    PostCard(
        R.drawable.test_image3, "타이틀7", "카테고리7",
        "2023년 1월 6일", "유저7"
    )
)

val curationResponse = listOf(
    CurationCard(
        R.drawable.test_image1, "타이틀1", "카테고리1",
        "2023년 1월 6일", "유저1"
    ),
    CurationCard(
        R.drawable.test_image2, "타이틀2", "카테고리2",
        "2023년 1월 6일", "유저2"
    ),
    CurationCard(
        R.drawable.test_image3, "타이틀3", "카테고리3",
        "2023년 1월 6일", "유저3"
    ),
    CurationCard(
        R.drawable.test_image4, "타이틀4", "카테고리4",
        "2023년 1월 6일", "유저4"
    ),
    CurationCard(
        R.drawable.test_image5, "타이틀5", "카테고리5",
        "2023년 1월 6일", "유저5"
    ),
    CurationCard(
        R.drawable.test_image1, "타이틀6", "카테고리6",
        "2023년 1월 6일", "유저6"
    ),
    CurationCard(
        R.drawable.test_image2, "타이틀7", "카테고리7",
        "2023년 1월 6일", "유저7"
    )
)

val categoryResponse = listOf("식물 키우기", "식물 부수기", "식물 심기")

val user = User(
    R.drawable.test_image1, "Mara", categoryResponse, 100, 50,
    true, postResponse, curationResponse
)

@Composable
fun ProfileTab (
    // 포스트 리스트
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .padding(18.dp)
            .fillMaxSize()
    ) {
        items(user.post) { postCard ->
            // 이미지가 들어간 버튼을 넣어야 함
            Image(
                painter = painterResource(id = postCard.image),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CuratorProfileTab(
    // 포스트 및 큐레이션 리스트
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState,tabPositions),
                color = Color.Black
            )
        }
    ) {
        pages.forEachIndexed { index, title ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(index)
                    }
                },
                modifier = Modifier
                    .height(50.dp)
                    .background(Color.White)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight =
                        if(pagerState.currentPage == index)
                            FontWeight.Bold
                        else
                            FontWeight.Normal
                    )
                )
            }
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
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .padding(18.dp)
                        .fillMaxSize()
                ) {
                    items(user.post) { postCard ->
                        // 이미지가 들어간 버튼을 넣어야 함
                        Image(
                            painter = painterResource(id = postCard.image),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(5.dp))
                                .fillMaxSize(),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
            }
            1 -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(18.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(user.curation) { curationCard ->
                        Row(
                            modifier = Modifier.height(100.dp)
                        ) {
                            Image(
                                painter = painterResource(id = curationCard.thumbnail_image),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp))
                                    .fillMaxHeight(),
                                contentScale = ContentScale.FillHeight
                            )
                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = curationCard.title,
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                )
                                Text(
                                    text = "${curationCard.category}, ${curationCard.time}",
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}