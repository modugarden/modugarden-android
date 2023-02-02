package com.example.modugarden.main.discover.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import androidx.room.Room
import com.example.modugarden.R
import com.example.modugarden.data.RecentSearch
import com.example.modugarden.data.RecentSearchDatabase
import com.example.modugarden.route.NAV_ROUTE_DISCOVER_SEARCH
import com.example.modugarden.ui.theme.addFocusCleaner
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.searchTextField
import com.example.modugarden.viewmodel.UserViewModel


@Composable
fun DiscoverSearchResultScreen(
    navController: NavHostController,
    searchedText: String,
    userViewModel: UserViewModel
) {
    val focusManager = LocalFocusManager.current
    val searchText = remember { mutableStateOf(searchedText) } //textField 데이터 값.
    val isTextFieldSearchFocused = remember { mutableStateOf(false) } //textField가 포커싱 되어 있는지 여부.

    val coroutineScope = rememberCoroutineScope()   //코루틴 스코프

    val snackbarHostState = remember { SnackbarHostState() }// 팔로우 스낵바 메세지 상태 변수

    val applicationContext = LocalContext.current.applicationContext


    val db = Room.databaseBuilder(
        applicationContext, RecentSearchDatabase::class.java, "recent_database"
    ).allowMainThreadQueries().build()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .addFocusCleaner(focusManager)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 18.dp, top = 14.dp, end = 18.dp, bottom = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                //뒤로가기 버튼으로 누르면 stack 다 날라가고 discover main 스크린으로 이동하
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_left_bold),
                    contentDescription = null,
                    modifier = Modifier
                        .bounceClick {
                            navController.popBackStack()
                        }
                )

                Spacer(modifier = Modifier.width(12.dp))

                //검색어 입력하는 텍스트 필드
                //검색창
                searchTextField(
                    searchText =  searchText,
                    isTextFieldSearchFocused = isTextFieldSearchFocused,
                    focusManager = focusManager
                )

                Spacer(modifier = Modifier.weight(1f))

                //검색버튼으로 스택관리 필요해보이는데 일단 그냥 한번 더 불러서 쌓는 식으로 해버림
                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier
                        .bounceClick {
                            //이미 전에 검색했던 거면 한번 지우고 다시 insert해줘서 맨 위로 올려줌
                            val checkData: RecentSearch? = db.recentSearchDao().findRecentSearchBySearchText(searchText.value)
                            checkData?.let {
                                db.recentSearchDao().delete(
                                    it
                                )
                            }

                            db.recentSearchDao().insert(RecentSearch(searchText.value))
                            navController.navigate(route = NAV_ROUTE_DISCOVER_SEARCH.DISCOVERSEARCHRESULT.routeName + "/" + searchText.value) {
                                popUpTo(NAV_ROUTE_DISCOVER_SEARCH.DISCOVERSEARCHING.routeName)
                            }
                        }

                )
            }
            DiscoverSearchResult(
                searchedText,
                coroutineScope ,
                snackbarHostState,
                navController,
                userViewModel
            )


        }

        // 커스텀한 팔로우 알림
        SnackbarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(18.dp),
            hostState = snackbarHostState,
            snackbar = { snackbarData: SnackbarData ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            Color("#62766B".toColorInt()),
                            RoundedCornerShape(10.dp)
                        )
                ) {
                    Row(
                        Modifier
                            .padding(12.dp, 17.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_check_solid),
                            contentDescription = "체크",
                            Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                        Text(
                            text = snackbarData.message,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
            })
    }

}