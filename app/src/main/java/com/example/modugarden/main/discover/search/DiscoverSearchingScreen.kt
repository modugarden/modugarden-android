package com.example.modugarden.main.discover.search


import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.room.Room
import com.example.modugarden.R
import com.example.modugarden.data.*
import com.example.modugarden.route.NAV_ROUTE_DISCOVER_SEARCH
import com.example.modugarden.ui.theme.addFocusCleaner
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBlack


@Composable
fun DiscoverSearchingScreen(navController: NavHostController) {
    val focusManager = LocalFocusManager.current
    val textFieldSearch = remember { mutableStateOf("") } //textField 데이터 값.
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
                            navController.popBackStack(route = NAV_ROUTE_DISCOVER_SEARCH.DISCOVERMAIN.routeName, inclusive = false)
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

                //검색버튼
                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier
                        .bounceClick {
                            if(textFieldSearch.value != "") {
                                //이미 전에 검색했던 거면 한번 지우고 다시 insert해줘서 맨 위로 올려줌
                                val checkData: RecentSearch? = db.recentSearchDao().findRecentSearchBySearchText(textFieldSearch.value)
                                checkData?.let {
                                    db.recentSearchDao().delete(
                                        it
                                    )
                                }

                                db.recentSearchDao().insert(RecentSearch(textFieldSearch.value))
                                navController.navigate(route = NAV_ROUTE_DISCOVER_SEARCH.DISCOVERSEARCHRESULT.routeName + "/" + textFieldSearch.value)
                            }
                        }

                )
            }
            Text(
                modifier = Modifier
                    .padding(top = 34.dp, start = 18.dp),
                text = "최근 검색어",
                style = TextStyle(
                    color = moduBlack,
                    fontWeight = FontWeight(700),
                    fontSize = 14.sp
                )
            )
            DiscoverSearchBefore(navController, db, recentSearchItems.value)

        }
    }

}