package com.example.modugarden.main.follow

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.layout.rememberLazyNearestItemsRangeState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.R
import com.example.modugarden.data.FollowCuration
import com.example.modugarden.data.FollowPost
import com.example.modugarden.data.followCurations
import com.example.modugarden.data.followPosts
import com.example.modugarden.main.content.CategoryItem
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_normal
import com.example.modugarden.ui.theme.moduGray_strong
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable //팔로우 피드.
fun FollowScreen(navController: NavHostController, followPosts:List<FollowPost>, followCurations: List<FollowCuration>) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)//바텀 시트
    val scrollState = rememberLazyListState()
    lateinit var data : Any

    ModalBottomSheetLayout(sheetElevation = 0.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetState = bottomSheetState ,
        sheetContent = {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 회색 선
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(5.dp)
                                .alpha(0.4f)
                                .background(moduGray_normal, RoundedCornerShape(30.dp))

                        )
                        Spacer(modifier = Modifier.size(30.dp))

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 18.dp)
                        ) {
                            Text(text = "포스트 신고", style = moduBold, fontSize = 20.sp)
                            /*Text(text = "큐레이션 신고", style = moduBold, fontSize = 20.sp)*/

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_user),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .border(1.dp, moduGray_light, RoundedCornerShape(50.dp))
                                        .size(25.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.size(18.dp))
                                Text(text = "Title", style = moduBold, fontSize = 14.sp)
                            }
                        }

                        // 구분선
                        Divider(
                            color = moduGray_light, modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )

                        // 신고 카테고리 리스트
                        LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = 18.dp)
                        ) {
                            item {
                                CategoryItem("욕설/비하")
                                CategoryItem("낚시/놀람/도배")
                                CategoryItem("음란물/불건전한 만남 및 대화")
                                CategoryItem("유출/사칭/사기")
                                CategoryItem("게시판 성격에 부적절함")
                            }
                        }
                        Spacer(modifier = Modifier.size(18.dp))
                    }


                }
            })
    {
        Box(){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(moduBackground)
            )
            {


                    /*Icon(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .bounceClick { },
                        painter = painterResource(id = R.drawable.ic_notification),
                        contentDescription = "알림",
                        tint = moduBlack
                    )*/

                LazyColumn(state = scrollState) {
                    // 상단 로고
                    item{
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp, 30.dp, 30.dp, 0.dp),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_house_with_garden),
                                contentDescription = null
                            )
                            Text(
                                text = " 모두의 정원",
                                color = moduGray_strong,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                    //포스트 카드
                    items(followPosts.reversed()){
                        PostCard(navController, data = it , scope, snackbarHostState, bottomSheetState)}

                    // 큐레이션 카드
                    items(followCurations) {
                        CurationCard(data = it, scope, snackbarHostState,bottomSheetState) }

                    // 팔로우 피드 맨 끝
                    item { FollowEndCard(navController) }
                }

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



}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun FollowPreview(){
    val navController = rememberNavController()
    FollowScreen(navController, followPosts, followCurations)
}