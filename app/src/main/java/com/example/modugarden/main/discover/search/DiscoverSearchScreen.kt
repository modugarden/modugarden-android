package com.example.modugarden.main.discover.search

import android.app.Activity
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.R
import com.example.modugarden.data.Category
import com.example.modugarden.route.NAV_ROUTE_UPLOAD_POST
import com.example.modugarden.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


//viewPager쓰면 넣어줘야하는 어노테이션
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun DiscoverSearchScreen() {
    //ViewPager쓸때 어디 페이지의 state를 확인할 변수
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    //어떤 카테고리 보여주는지 왼쪽 위에 아이콘이랑 카테고리 이름 바꿔줄 변수
    var selectedCategory by remember { mutableStateOf(Category.TRIP) }

    //viewPager에 사용할 포스트, 큐레이션 나타내주는 변수
    val mainPages = listOf("포스트", "큐레이션")

    val focusManager = LocalFocusManager.current
    val textFieldSearch = remember { mutableStateOf("") } //textField 데이터 값.
    val isTextFieldSearchFocused = remember { mutableStateOf(false) } //textField가 포커싱 되어 있는지 여부.

    var isTextFieldVisible by remember { mutableStateOf(false) }


    val showModalSheet = rememberSaveable{ mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val scope = rememberCoroutineScope()


    ModalBottomSheet(
        title = "카테고리",
        bottomSheetState = bottomSheetState,
        sheetScreen = {
            ModalBottomSheetItem(text = "식물 가꾸기", icon = R.drawable.ic_potted_plant, trailing = true, modifier = Modifier.bounceClick {
                selectedCategory = Category.GARDENING
                scope.launch {
                    bottomSheetState.hide()
                }
            })
            ModalBottomSheetItem(text = "플랜테리어", icon = R.drawable.ic_house_with_garden, trailing = true, modifier = Modifier.bounceClick {
                selectedCategory = Category.PLANTERIOR
                scope.launch {
                    bottomSheetState.hide()
                }
            })
            ModalBottomSheetItem(text = "여행/나들이", icon = R.drawable.ic_tent, trailing = true, modifier = Modifier.bounceClick {
                selectedCategory = Category.TRIP
                scope.launch {
                    bottomSheetState.hide()
                }
            })
        },
        uiScreen = {
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
                            .padding(start = 18.dp, top = 14.dp, end = 18.dp, bottom = 0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isTextFieldVisible.not()) {
                            Box(
                                modifier = Modifier.height(52.dp)
                            ) {
                                this@Row.AnimatedVisibility(
                                    visible = isTextFieldVisible.not(),
                                    enter = slideInHorizontally(initialOffsetX = {
                                        +it
                                    }),
                                    exit = slideOutHorizontally(targetOffsetX = {
                                        -it
                                    })


                                ) {
                                    Box(
                                        modifier = Modifier.align(Alignment.CenterStart)
                                            .bounceClick {
                                            focusManager.clearFocus()
                                            showModalSheet.value = !showModalSheet.value
                                            scope.launch {
                                                bottomSheetState.show()
                                            }
                                        },
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxHeight(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .width(22.dp)
                                            ) {
                                                //카테고리 선택된 거에 맞는 이름 사진 넣어버려
                                                Image(
                                                    painter = painterResource(id = selectedCategory.image),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(22.dp, 22.dp)
                                                )

                                            }
                                            Text(
                                                modifier = Modifier
                                                    .padding(horizontal = 6.25.dp),
                                                text = selectedCategory.category,
                                                style = TextStyle(
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = moduBlack
                                                )
                                            )
                                            //카테고리 옆에 있는 ^ 이거 거꾸로 이미지
                                            Image(
                                                painter = painterResource(id = R.drawable.ic_chevron_down),
                                                contentDescription = null,
                                            )

                                        }

                                    }

                                }


                            }
                        }

                        this@Row.AnimatedVisibility(
                            visible = isTextFieldVisible,
                            modifier = Modifier,
//                    enter = slideInVertically(initialOffsetY = {
//                        -it
//                    }),
//                    exit = slideOutVertically(targetOffsetY = {
//                        -it
//                    })
                            enter = slideInHorizontally(initialOffsetX = {
                                +it
                            }),
                            exit = slideOutHorizontally(targetOffsetX = {
                                +it
                            })

                        ) {
                            TextField(
                                value = textFieldSearch.value,
                                onValueChange = { textValue -> textFieldSearch.value = textValue },
                                modifier = Modifier
                                    .padding(vertical = 0.dp, horizontal = 10.dp)
                                    .fillMaxWidth(1f)
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
                        }
                        Spacer(modifier = Modifier.weight(1f))

                        //검색버튼
                        Image(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = null,
                            modifier = Modifier
                                .bounceClick {
                                    if(isTextFieldVisible) textFieldSearch.value = ""
                                    isTextFieldVisible = isTextFieldVisible.not()
                                }

                        )
                    }

                    //만약 검색하는 창이 아닐경우에 나올 기본 메인 화면 탭 레이아웃
                    if(isTextFieldVisible.not()) {
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
                            mainPages.forEachIndexed { index, title ->
                                Tab(
                                    text = {
                                        Text(
                                            text = title,
                                            fontSize = 16.sp,
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                color = moduBlack
                                            )
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
                            count = 2,
                            state = pagerState
                        ) { page ->
                            when (page) {
                                //나중에 API로 받은 값(List)도 넣어줘야할듯
                                0 -> DiscoverSearchPost("제목 제목 제목 제목")
                                1 -> DiscoverSearchCuration()
                            }

                        }
                    }
                    else{  //만약 검색하는 창인 경우에 나올 화면

                        if(textFieldSearch.value == ""){
                            DiscoverSearchBefore()
                        }
                        else{
                            DiscoverSearchResult(textFieldSearch.value)
                        }


                    }

                }
            }

        }
    )



}
