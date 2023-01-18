package com.example.modugarden.main.follow

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.R
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_normal
import com.example.modugarden.ui.theme.moduGray_strong
import com.google.accompanist.pager.ExperimentalPagerApi

@Composable //팔로우 피드.
fun FollowScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(moduBackground)) {
                LazyColumn() {
                    // 상단 로고
                    item{
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp, 30.dp, 30.dp, 8.dp),
                            ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(painter = painterResource(id = R.drawable.ic_house_with_garden),
                                    contentDescription =null )
                                Text(
                                    text = "모두의 정원",
                                    color = moduGray_strong,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp)
                            }

                            Icon(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .bounceClick { },
                                painter = painterResource(id = R.drawable.ic_notification),
                                contentDescription = "알림",
                                tint = moduBlack
                                )
                        }

                    }
                    //포스트 카드
                    itemsIndexed(
                        listOf("user1")){
                            index, item ->
                        PostCard(item,scope,snackbarHostState)
                    }
                    // 큐레이션 카드
                    item { CurationCard("user1")}
                    // 팔로우 피드 맨 끝
                    item { FollowEndCard() }
                }
        // 커스텀한 스낵바
        SnackbarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(30.dp),
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

@Preview
@Composable
fun FollowPreview(){
    val navController = rememberNavController()
    FollowScreen(navController)
}