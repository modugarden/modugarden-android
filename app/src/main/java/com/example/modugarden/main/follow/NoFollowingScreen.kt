package com.example.modugarden.main.follow


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.R
import com.example.modugarden.api.dto.FollowRecommendationRes
import com.example.modugarden.api.dto.FollowRecommendationResContent
import com.example.modugarden.main.discover.search.DiscoverSearchPostCard
import com.example.modugarden.route.NAV_ROUTE_FOLLOW
import com.example.modugarden.ui.theme.FollowCard
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_strong
import com.example.modugarden.viewmodel.RefreshViewModel
import com.example.modugarden.viewmodel.UserViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// 볼드 텍스트 타입 설정
val moduBold : TextStyle = TextStyle(color = moduBlack, fontWeight = FontWeight.Bold)
//@SuppressLint("UnrememberedMutableState")
@Composable //팔로잉이 3명 미만일 때 표시되는 화면.
fun NoFollowingScreen(
/*    recommendRes: MutableState<FollowRecommendationRes>,*/
    navController: NavHostController,
    userViewModel: UserViewModel,
    refreshViewModel: RefreshViewModel) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()//스크롤 상태 변수

    //팔로우 추천
    val recommendList = remember { mutableStateOf(FollowRecommendationRes().content) }

    val recommendPage = remember { mutableStateOf(0)}


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(moduBackground)
                .padding(30.dp)
                .verticalScroll(scrollState),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier=Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                // 안내 문구
                Text(
                    text = "흥미로운 사용자를\n" + "팔로우 해보세요 \uD83D\uDC40 ",
                    style = moduBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "사용자를 3명 이상 팔로우하면\n" +
                            "팔로우 피드를 사용할 수 있어요.",
                    fontWeight = FontWeight(400),
                    fontSize = 14.sp,
                    color = moduBlack,
                    modifier = Modifier.alpha(0.5f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(44.dp))

                // 팔로우 추천 목록
                Box(
                    Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .fillMaxWidth()
                        .background(Color.White),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column(content = {
                        if (recommendList.value.size > 0 ) {
                            Column() {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(18.dp, 20.dp)
                                        .bounceClick {
                                            Log.d("추천 유저", recommendList.value[0].nickname)
                                            userViewModel.setUserId(recommendList.value[0].userId)
                                            navController.navigate(NAV_ROUTE_FOLLOW.USERPROFILE.routeName)
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // 프로필 사진
                                    GlideImage(
                                        imageModel = recommendList.value[0].profileImg,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(50.dp)
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(50)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(start = 18.dp)
                                        ) {
                                            // 아이디
                                            Text(
                                                text = recommendList.value[0].nickname,
                                                fontSize = 14.sp,
                                                style = moduBold
                                            )
                                            // 카테고리
                                            Text(
                                                text = recommendList.value[0].categories.toString()
                                                    .replace("[", "").replace("]", ""),
                                                fontSize = 11.sp,
                                                color = moduGray_strong
                                            )
                                        }
                                        // 팔로우 버튼
                                        FollowCard(
                                            id = recommendList.value[0].userId,
                                            modifier = Modifier,
                                            snackBarAction = {
//                                            scope.launch {
//                                                if (followState.value) snackbarHostState.showSnackbar("${data.nickname} 님을 팔로우 했어요.")
//                                                else snackbarHostState.showSnackbar("${data.nickname} 님을 언팔로우 했어요.")
//                                            }
                                            },
                                            followState = remember { mutableStateOf(recommendList.value[0].isFollow) },
                                            contentModifier =
                                            Modifier
                                                .padding(vertical = 6.dp, horizontal = 10.dp)
                                        )

                                    }
                                }
                                Divider(
                                    color = moduGray_light,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                )
                            }
                        Column() {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp, 20.dp)
                                    .bounceClick {
                                        Log.d("추천 유저", recommendList.value[1].nickname)
                                        userViewModel.setUserId(recommendList.value[1].userId)
                                        navController.navigate(NAV_ROUTE_FOLLOW.USERPROFILE.routeName)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 프로필 사진
                                GlideImage(
                                    imageModel = recommendList.value[1].profileImg,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(50)),
                                    contentScale = ContentScale.Crop
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 18.dp)
                                    ) {
                                        // 아이디
                                        Text(
                                            text = recommendList.value[1].nickname,
                                            fontSize = 14.sp,
                                            style = moduBold
                                        )
                                        // 카테고리
                                        Text(
                                            text = recommendList.value[1].categories.toString()
                                                .replace("[", "").replace("]", ""),
                                            fontSize = 11.sp,
                                            color = moduGray_strong
                                        )
                                    }
                                    // 팔로우 버튼
                                    FollowCard(
                                        id = recommendList.value[1].userId,
                                        modifier = Modifier,
                                        snackBarAction = {
//                                            scope.launch {
//                                                if (followState.value) snackbarHostState.showSnackbar("${data.nickname} 님을 팔로우 했어요.")
//                                                else snackbarHostState.showSnackbar("${data.nickname} 님을 언팔로우 했어요.")
//                                            }
                                        },
                                        followState = remember { mutableStateOf(recommendList.value[1].isFollow) },
                                        contentModifier =
                                        Modifier
                                            .padding(vertical = 6.dp, horizontal = 10.dp)
                                    )

                                }
                            }
                            Divider(
                                color = moduGray_light,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                            )
                        }
                            Column() {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(18.dp, 20.dp)
                                        .bounceClick {
                                            Log.d("추천 유저", recommendList.value[2].nickname)
                                            userViewModel.setUserId(recommendList.value[2].userId)
                                            navController.navigate(NAV_ROUTE_FOLLOW.USERPROFILE.routeName)
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // 프로필 사진
                                    GlideImage(
                                        imageModel = recommendList.value[2].profileImg,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(50.dp)
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(50)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(start = 18.dp)
                                        ) {
                                            // 아이디
                                            Text(
                                                text = recommendList.value[2].nickname,
                                                fontSize = 14.sp,
                                                style = moduBold
                                            )
                                            // 카테고리
                                            Text(
                                                text = recommendList.value[2].categories.toString()
                                                    .replace("[", "").replace("]", ""),
                                                fontSize = 11.sp,
                                                color = moduGray_strong
                                            )
                                        }
                                        // 팔로우 버튼
                                        FollowCard(
                                            id = recommendList.value[2].userId,
                                            modifier = Modifier,
                                            snackBarAction = {
//                                            scope.launch {
//                                                if (followState.value) snackbarHostState.showSnackbar("${data.nickname} 님을 팔로우 했어요.")
//                                                else snackbarHostState.showSnackbar("${data.nickname} 님을 언팔로우 했어요.")
//                                            }
                                            },
                                            followState = remember { mutableStateOf(recommendList.value[2].isFollow) },
                                            contentModifier =
                                            Modifier
                                                .padding(vertical = 6.dp, horizontal = 10.dp)
                                        )

                                    }
                                }
                                Divider(
                                    color = moduGray_light,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                )
                            }
                    }

//                        for (recommendData in recommendList.value) {
//                            FollowRecommendCard(
//                                navController = navController,
//                                data = recommendData,
//                                userViewModel = userViewModel
//                            )
//                        }
//                        recommendList.value.forEach { data ->
//                            FollowRecommendCard(
//                                navController = navController,
//                                data = data,
//                                userViewModel = userViewModel
//                            )
//                        }
                    })

                }

                Spacer(modifier = Modifier.size(50.dp))
                //  다시 탐색 버튼
                Card(
                    modifier = Modifier
                        .bounceClick {
                            refreshViewModel.getRecommend(recommendList, recommendPage.value)
//                            recommendList.value = recommendRes.value.content
                            recommendPage.value += 1
                        },
                    backgroundColor = Color.White,
                    shape = RoundedCornerShape(10.dp),
                    elevation = 0.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(10.dp, 8.dp)
                    ) {
                        Icon(
                            modifier = Modifier.padding(end = 12.dp),
                            painter = painterResource(id = R.drawable.ic_search_small),
                            contentDescription = "다시 탐색",
                            tint = moduBlack
                        )
                        Text(
                            text = "다시 탐색",
                            fontSize = 16.sp,
                            style = moduBold,
                        )
                    }

                }

            }
            // 커스텀한 스낵바
            SnackbarHost(
                modifier = Modifier.align(Alignment.BottomCenter),
                hostState = snackbarHostState,
                snackbar = { snackbarData: SnackbarData ->
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(Color("#62766B".toColorInt()), RoundedCornerShape(10.dp))
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

@Composable
// 팔로우 추천 목록에 들어갈 카드
fun FollowRecommendCard(
    navController:NavHostController,
    data: FollowRecommendationResContent,
    scope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    userViewModel:UserViewModel){

    val followState = remember { mutableStateOf(false) }
    Column() {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(18.dp, 20.dp)
                .bounceClick {
                    Log.d("추천 유저", data.nickname)
                    userViewModel.setUserId(data.userId)
                    navController.navigate(NAV_ROUTE_FOLLOW.USERPROFILE.routeName)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 사진
           GlideImage(
                imageModel = data.profileImg,
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(50)),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 18.dp)
                ) {
                    // 아이디
                    Text(
                        text = data.nickname,
                        fontSize = 14.sp,
                        style = moduBold
                    )
                    // 카테고리
                    Text(
                        text = data.categories.toString().replace("[","").replace("]",""),
                        fontSize = 11.sp,
                        color = moduGray_strong
                    )
                }
               // 팔로우 버튼
                FollowCard(
                    id = data.userId,
                    modifier = Modifier,
                    snackBarAction = {
                        scope.launch {
                            if (followState.value) snackbarHostState.showSnackbar("${data.nickname} 님을 팔로우 했어요.")
                            else snackbarHostState.showSnackbar("${data.nickname} 님을 언팔로우 했어요.")
                        }
                    },
                    followState = remember { mutableStateOf(data.isFollow) },
                    contentModifier =
                    Modifier
                        .padding(vertical = 6.dp, horizontal = 10.dp)
                )

            }
        }
        Divider(
            color = moduGray_light,
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
    }

}


@Preview
@Composable
fun NoFollowingPreview() {
    val navController = rememberNavController()
    /*NoFollowingScreen()*/

}



