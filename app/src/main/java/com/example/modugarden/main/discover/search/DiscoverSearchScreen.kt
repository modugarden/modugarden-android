package com.example.modugarden.main.discover.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.R
import com.example.modugarden.ui.theme.ButtonState
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduBlack
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


//상단에 내가 누른 카테고리 항목을 매번 서버에 받을 수는 없으니 카테고리 선택한거 담는 데이터가 필요할듯?? (여행/나들이, 식물기르기)
//이런것중에 뭐 눌렀는지 알아야할 것 같아 그래서 대충 이런 enum이나 data 클래스들 담을 폴더도 필요할듯
enum class Categories(
    val koName: String,
    val image: Int
) {
    TRIP("여행/나들이", R.drawable.ic_search),
    GROWPLANT("식물 가꾸기", R.drawable.ic_home),
    Planterrier("플랜테리어", R.drawable.ic_user)
}

//viewPager쓰면 넣어줘야하는 어노테이션
@OptIn(ExperimentalPagerApi::class)
@Composable
fun DiscoverSearchScreen() {
    //ViewPager쓸때 어디 페이지의 state를 확인할 변수
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    //어떤 카테고리 보여주는지 왼쪽 위에 아이콘이랑 카테고리 이름 바꿔줄 변수
    var selectedCategory by remember { mutableStateOf(Categories.TRIP) }

    //viewPager에 사용할 포스트, 큐레이션 나타내주는 변수
    val pages = listOf("포스트", "큐레이션")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            //상단 카테고리 선택 및 검색버튼 있는 레이아웃
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                //카테고리 선택된 거에 맞는 이름 사진 넣어버려
                Image(
                    painter = painterResource(id = selectedCategory.image),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp, 22.dp)
                )
                Text(
                    modifier = Modifier.padding(horizontal = 6.25.dp),
                    text = selectedCategory.koName,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = moduBlack
                    )
                )

                //카테고리 옆에 있는 ^ 이거 거꾸로 이미지
                Image(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    painter = painterResource(id = R.drawable.ic_chevron_down),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.weight(1f))

                //검색버튼
                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier
                        .bounceClick {  //이건 그냥 확인용으로 넣어본거
                            if(selectedCategory == Categories.TRIP){
                                selectedCategory = Categories.GROWPLANT
                            }
                            else if(selectedCategory == Categories.GROWPLANT){
                                selectedCategory = Categories.Planterrier
                            }
                            else selectedCategory = Categories.TRIP
                        }

                    )
            }
            
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
                pages.forEachIndexed { index, title ->
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
                count = 2,
                state = pagerState
            ) { page ->
                when (page) {
                    //나중에 API로 받은 값(List)도 넣어줘야할듯
                    0 -> DiscoverSearchPost()
                    1 -> DiscoverSearchCuration()
                }

            }

        }
    }

}