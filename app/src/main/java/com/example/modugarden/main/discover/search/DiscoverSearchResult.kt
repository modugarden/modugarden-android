package com.example.modugarden.main.discover.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modugarden.api.dto.GetSearchCuration
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UserViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DiscoverSearchResult(
    textFieldSearch: String,
    coroutineScope: CoroutineScope ,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    userViewModel: UserViewModel
) {

    //ViewPager쓸때 어디 페이지의 state를 확인할 변수
    val pagerState = rememberPagerState()

    var curationResponseBody = remember { mutableStateOf(GetSearchCuration()) }

    Spacer(modifier = Modifier.height(18.dp))
    Row(
        modifier = Modifier
            .height(36.dp)
            .fillMaxWidth()
            .padding(end = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(
            Modifier
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "포스트",
                fontSize = 20.sp,
                color =
                if(pagerState.currentPage == 0) moduBlack
                else moduGray_normal,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .bounceClick {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    })
            Spacer(Modifier.size(20.dp))
            Text(text = "큐레이션",
                fontSize = 20.sp,
                color =
                if(pagerState.currentPage == 1) moduBlack
                else moduGray_normal,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.bounceClick {
                    curationResponseBody.value = GetSearchCuration()
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                }
            )
            Spacer(Modifier.size(20.dp))
            Text(text = "사용자",
                fontSize = 20.sp,
                color =
                if(pagerState.currentPage == 2) moduBlack
                else moduGray_normal,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.bounceClick {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(2)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))


    }

    Spacer(modifier = Modifier.height(14.dp))

    HorizontalPager(
        modifier = Modifier
            .fillMaxSize(),
        count = 3,
        state = pagerState
    ) { page ->
        when (page) {
            //나중에 API로 받은 값(List)도 넣어줘야할듯
            0 -> DiscoverTextSearchPost(textFieldSearch)
            1 -> DiscoverTextSearchCuration(curationResponseBody, textFieldSearch)
            2 -> DiscoverSearchUser(textFieldSearch, coroutineScope, snackbarHostState, navController, userViewModel)
        }

    }

}