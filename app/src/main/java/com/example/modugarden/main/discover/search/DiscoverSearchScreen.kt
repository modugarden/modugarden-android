package com.example.modugarden.main.discover.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.api.dto.GetSearchCuration
import com.example.modugarden.data.Category
import com.example.modugarden.route.NAV_ROUTE_DISCOVER_SEARCH
import com.example.modugarden.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun DiscoverSearchScreen(navController: NavHostController) {
    val pagerState = rememberPagerState()

    val selectedCategory = remember { mutableStateOf(Category.GARDENING) }


    val focusManager = LocalFocusManager.current

    val curationResponseBody  = remember { mutableStateOf(GetSearchCuration()) }

    val showModalSheet = rememberSaveable{ mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        title = "카테고리",
        bottomSheetState = bottomSheetState,
        sheetScreen = {
            ModalBottomSheetItem(text = "식물 가꾸기", icon = R.drawable.ic_potted_plant, trailing = true, modifier = Modifier.bounceClick {
                selectedCategory.value = Category.GARDENING

                scope.launch {
                    bottomSheetState.hide()
                }
            })
            ModalBottomSheetItem(text = "플랜테리어", icon = R.drawable.ic_house_with_garden, trailing = true, modifier = Modifier.bounceClick {

                selectedCategory.value = Category.PLANTERIOR

                scope.launch {
                    bottomSheetState.hide()
                }
            })
            ModalBottomSheetItem(text = "여행/나들이", icon = R.drawable.ic_tent, trailing = true, modifier = Modifier.bounceClick {
                selectedCategory.value = Category.TRIP

                scope.launch {
                    bottomSheetState.hide()
                }
            })
        },
        uiScreen = {
            BackHandler(enabled = bottomSheetState.isVisible) {
                scope.launch {
                    bottomSheetState.hide()
                }
            }
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
                            .padding(start = 18.dp, top = 40.dp, end = 18.dp, bottom = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "탐색",
                            color = moduBlack,
                            fontSize = 22.sp,
                            fontWeight = FontWeight(700)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Image(
                            painter = painterResource(id = R.drawable.ic_search_big),
                            contentDescription = null,
                            modifier = Modifier
                                .bounceClick {
                                    navController.navigate(route = NAV_ROUTE_DISCOVER_SEARCH.DISCOVERSEARCHING.routeName)
                                }

                        )
                    }
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
                                        scope.launch {
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
                                    scope.launch {
                                        pagerState.animateScrollToPage(1)
                                    }

                                }
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Box(
                            modifier = Modifier
                                .bounceClick {
                                    focusManager.clearFocus()
                                    showModalSheet.value = !showModalSheet.value
                                    scope.launch {
                                        bottomSheetState.show()
                                    }
                                }
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = moduBackground),

                            ) {
                            Row(
                                modifier = Modifier.fillMaxHeight(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 13.dp),
                                    text = selectedCategory.value.category,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(600),
                                        color = moduGray_strong
                                    )
                                )
                                //카테고리 옆에 있는 ^ 이거 거꾸로 이미지
                                Image(
                                    modifier = Modifier.padding(start = 5.dp, end = 12.dp),
                                    painter = painterResource(id = R.drawable.ic_chevron_down_new),
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    //포스트, 큐레이션 텝 레이아웃
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxSize(),
                        count = 2,
                        state = pagerState
                    ) { page ->
                        when (page) {
                            //나중에 API로 받은 값(List)도 넣어줘야할듯
                            0 -> DiscoverCategorySearchPost(selectedCategory.value)
                            1 -> DiscoverCategorySearchCuration(curationResponseBody, selectedCategory.value)
                        }

                    }

                }
            }

        }
    )



}
