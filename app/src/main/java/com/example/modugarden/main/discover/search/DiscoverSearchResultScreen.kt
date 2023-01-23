package com.example.modugarden.main.discover.search

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.room.Room
import com.example.modugarden.R
import com.example.modugarden.data.RecentSearch
import com.example.modugarden.data.RecentSearchDatabase
import com.example.modugarden.route.NAV_ROUTE_DISCOVER_SEARCH
import com.example.modugarden.ui.theme.addFocusCleaner
import com.example.modugarden.ui.theme.bounceClick


@Composable
fun DiscoverSearchResultScreen(navController: NavHostController, searchText : String) {
    val focusManager = LocalFocusManager.current
    val textFieldSearch = remember { mutableStateOf(searchText) } //textField 데이터 값.
    val isTextFieldSearchFocused = remember { mutableStateOf(false) } //textField가 포커싱 되어 있는지 여부.


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
                TextField(
                    value = textFieldSearch.value,
                    onValueChange = { textValue -> textFieldSearch.value = textValue },
                    modifier = Modifier
                        .padding(vertical = 0.dp, horizontal = 0.dp)
                        .fillMaxWidth(0.9f)
                        .height(52.dp)
                        .onFocusChanged {
                            isTextFieldSearchFocused.value = it.isFocused
                        }
                        .animateContentSize(),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor =
                        if (isTextFieldSearchFocused.value) Color(0xFFEDF5F0)
                        else Color(0xFFF3F5F4),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    singleLine = true,
                )

                Spacer(modifier = Modifier.weight(1f))

                //검색버튼으로 스택관리 필요해보이는데 일단 그냥 한번 더 불러서 쌓는 식으로 해버림
                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier
                        .bounceClick {
                            db.recentSearchDao().insert(RecentSearch(textFieldSearch.value))
                            navController.navigate(route = NAV_ROUTE_DISCOVER_SEARCH.DISCOVERSEARCHRESULT.routeName + "/" + textFieldSearch.value)
                        }

                )
            }
            DiscoverSearchResult(searchText)

        }
    }

}