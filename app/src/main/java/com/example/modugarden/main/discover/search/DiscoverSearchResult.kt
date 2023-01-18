package com.example.modugarden.main.discover.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.modugarden.ui.theme.moduBlack
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DiscoverSearchResult(textFieldSearch: String) {

    //ViewPager쓸때 어디 페이지의 state를 확인할 변수
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()


    val searchPages = listOf("포스트", "큐레이션", "사용자")

    //포스트, 큐레이션 텝 레이아웃
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.White,
        contentColor = Color.Black,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                color = moduBlack,
            )
        },

        ) {
        searchPages.forEachIndexed { index, title ->
            Tab(
                text = {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        style = TextStyle(fontWeight = FontWeight.Bold, color = moduBlack)
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(index)
                    }
                }
            )
        }
    }
    HorizontalPager(
        modifier = Modifier
            .fillMaxSize(),
        count = 3,
        state = pagerState
    ) { page ->
        when (page) {
            //나중에 API로 받은 값(List)도 넣어줘야할듯
            0 -> DiscoverSearchPost(textFieldSearch)
            1 -> DiscoverSearchNoResultScreen(textFieldSearch)
            2 -> DiscoverSearchUser(textFieldSearch)
        }

    }
}