package com.example.modugarden.main.follow


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.FollowRecommendationRes
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_strong
import com.example.modugarden.ui.theme.moduPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 볼드 텍스트 타입 설정
val moduBold : TextStyle = TextStyle(color = moduBlack, fontWeight = FontWeight.Bold)
@SuppressLint("UnrememberedMutableState")
@Composable //팔로잉이 3명 미만일 때 표시되는 화면.
fun NoFollowingScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
   var recommendres by remember { mutableStateOf(FollowRecommendationRes()) }

    RetrofitBuilder.followAPI.getRecommendFollowList()
        .enqueue(object : Callback<FollowRecommendationRes>{
            override fun onResponse(
                call: Call<FollowRecommendationRes>,
                response: Response<FollowRecommendationRes>
            ) {
               if (response.isSuccessful){
                   val res = response.body()
                   if (res != null) {
                       recommendres.result = res.result
                   }
               }
            }

            override fun onFailure(call: Call<FollowRecommendationRes>, t: Throwable) {

            }
        })

    val recommendList = recommendres.result
    Log.i("reclist",recommendList.toString())
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(moduBackground)
            .padding(30.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
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
                LazyColumn(){
                    // 추천 목록 데이터 넘겨서 표시될 예정
                    itemsIndexed(
                        listOf("user1","user2","user3")){
                            index, item ->
                        FollowRecommendCard(userID = item, scope = scope, snackbarHostState = snackbarHostState)
                    }
                }
            }

                Spacer(modifier = Modifier.size(50.dp))
                //  다시 탐색 버튼
                Card(
                    modifier = Modifier
                        .bounceClick {}

                        ,
                    backgroundColor = Color.White,
                    shape = RoundedCornerShape(10.dp),
                    elevation = 0.dp
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(10.dp,8.dp)) {
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
fun FollowRecommendCard(userID:String,
                        scope: CoroutineScope = rememberCoroutineScope(),
                        snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }){

    Column() {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(18.dp, 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 사진
            Image(
                painter = painterResource(R.drawable.ic_user),
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
                        text = userID,
                        fontSize = 14.sp,
                        style = moduBold
                    )
                    // 카테고리
                    Text(
                        text = "category",
                        fontSize = 11.sp,
                        color = moduGray_strong
                    )
                }
                // 팔로우 버튼
                Card(
                    modifier = Modifier
                        .bounceClick {

                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "$userID 님을 팔로우 하였습니다.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                    shape = RoundedCornerShape(5.dp),
                    backgroundColor = moduPoint
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp, 4.dp),
                        text = "팔로우",
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }
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
    NoFollowingScreen(navController = navController)

}



