package com.example.modugarden.main.discover.search


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.room.Room
import com.example.modugarden.R
import com.example.modugarden.data.*
import com.example.modugarden.route.NAV_ROUTE_DISCOVER_SEARCH
import com.example.modugarden.ui.theme.*


@Composable
fun DiscoverSearchingScreen(navController: NavHostController) {
    val focusManager = LocalFocusManager.current
    val searchText = remember { mutableStateOf("") } //textField 데이터 값.
    val isTextFieldSearchFocused = remember { mutableStateOf(false) } //textField가 포커싱 되어 있는지 여부.

    val applicationContext = LocalContext.current.applicationContext

    val db = Room.databaseBuilder(
        applicationContext, RecentSearchDatabase::class.java, "recent_database"
    ).allowMainThreadQueries().build()

    val recentSearchItems = remember { mutableStateOf(db.recentSearchDao().getAll()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .addFocusCleaner(focusManager)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            //상단 카테고리 선택 및 검색버튼 있는 레이아웃
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 18.dp, top = 40.dp, end = 18.dp, bottom = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "탐색",
                    color = moduBlack,
                    fontSize = 22.sp,
                    fontWeight = FontWeight(700)
                )

                Spacer(modifier = Modifier.weight(1f))

                //뒤로가기 버튼으로 누르면 stack 다 날라가고 discover main 스크린으로 이동하
                Image(
                    painter = painterResource(id = R.drawable.ic_cross_line_bold_black),
                    contentDescription = null,
                    modifier = Modifier
                        .bounceClick {
                            navController.popBackStack(route = NAV_ROUTE_DISCOVER_SEARCH.DISCOVERMAIN.routeName, inclusive = false)
                        }
                )
            }

            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                //검색창
                SearchTextField(
                    searchText =  searchText,
                    isTextFieldSearchFocused = isTextFieldSearchFocused,
                    focusManager = focusManager,
                    db = db,
                    navController = navController
                )

            }

            Text(
                modifier = Modifier
                    .padding(top = 18.dp, start = 18.dp, bottom = 8.dp),
                text = "최근 검색어",
                style = TextStyle(
                    color = moduBlack,
                    fontWeight = FontWeight(700),
                    fontSize = 20.sp
                )
            )
            DiscoverSearchBefore(navController, db, recentSearchItems.value)

        }
    }

}