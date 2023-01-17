package com.example.modugarden.main.content

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.R
import com.example.modugarden.main.follow.DotsIndicator
import com.example.modugarden.main.follow.moduBold
import com.example.modugarden.route.NAV_ROUTE_POSTCONTENT
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_normal
import com.example.modugarden.ui.theme.moduGray_strong
import com.example.modugarden.ui.theme.moduPoint
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun PostContentScreen(navController: NavHostController, userID:String) {
    //뷰페이저, 인디케이터 페이지 상태 변수
    val order: PagerState = rememberPagerState()
    //스크롤 상태 변수
    val scrollState = rememberScrollState()
    //바텀 시트
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden)
    val showModalSheet = rememberSaveable{ mutableStateOf(false) }
    //액티비티 종료할 때 필요한 변수
    val activity = (LocalContext.current as? Activity)
    // 팔로우 스낵바 메세지 띄울 때 필요
    val scope = rememberCoroutineScope()
    // 팔로우 스낵바 메세지 상태 변수
    val snackbarHostState = remember { SnackbarHostState() }
        ModalBottomSheetLayout(
            sheetElevation = 0.dp,
            sheetBackgroundColor = Color.Transparent,
            sheetState = bottomSheetState,
            sheetContent = {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                    shape = RoundedCornerShape(15.dp)) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        // 회색 선
                        Box(modifier = Modifier
                            .width(40.dp)
                            .height(5.dp)
                            .alpha(0.4f)
                            .background(moduGray_normal, RoundedCornerShape(30.dp))

                        )
                        Spacer(modifier = Modifier.size(30.dp))

                        Column(modifier = Modifier
                            .padding(horizontal = 18.dp)) {
                            Text(text = "포스트 신고", style = moduBold, fontSize = 20.sp)

                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 18.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                Image(painter = painterResource(id = R.drawable.ic_user),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .border(1.dp, moduGray_light, RoundedCornerShape(50.dp))
                                        .size(25.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop)
                                Spacer(modifier = Modifier.size(18.dp))
                                Text(text = "post", style = moduBold, fontSize = 14.sp)
                            }
                        }

                        // 구분선
                        Divider(color = moduGray_light, modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp))

                        // 신고 카테고리 리스트
                        LazyColumn(modifier = Modifier
                            .padding(horizontal = 18.dp) ){
                            item {
                                categoryItem("욕설/비하")
                                categoryItem( "낚시/놀람/도배")
                                categoryItem(category = "음란물/불건전한 만남 및 대화")
                                categoryItem(category = "유출/사칭/사기")
                                categoryItem(category = "게시판 성격에 부적절함")
                            }
                        }
                        Spacer(modifier = Modifier.size(18.dp))
                    }


                }
            }
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(moduBackground)) {
                Column(
                    Modifier
                        .verticalScroll(scrollState)
                        .background(Color.White)
                ) {
                    // 포스트 카드 이미지 배열
                    val images = listOf(
                        R.drawable.plant1,
                        R.drawable.plant2,
                        R.drawable.plant3
                    )
                    // 포스트 카드 이미지 슬라이드
                    HorizontalPager(
                        count = images.size,
                        state = order,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                    ) { page ->
                        Image(
                            painter = painterResource(id = images[page]),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                        )
                    }

                    // 포스트 카드 이미지 슬라이드 인디케이터
                    DotsIndicator(
                        totalDots = images.size,
                        selectedIndex = order.currentPage,
                        unSelectedColor = Color("#75807A66".toColorInt())
                    )
                    // 제목, 카테고리, 업로드 시간
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(18.dp)
                    ) {
                        Text(
                            "Title",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color("#17291F".toColorInt())
                        )
                        Row() {
                            Text("category", fontSize = 12.sp, color = Color("#75807A".toColorInt()))
                            Spacer(modifier = Modifier.weight(1f))
                            Text("upload time", fontSize = 12.sp, color = Color("#75807A".toColorInt()))
                        }

                    }
                    // 구분선
                    Divider(
                        color = moduGray_light, modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )
                    // 포스트 작성자 영역
                    Column(modifier = Modifier
                        .background(Color.White)
                        .bounceClick {
                            //  포스트 작성자 프로필로
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(18.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            Text(text = "작성자", style = moduBold, fontSize = 16.sp)
                            // 작성자 프로필 더보기 버튼
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chevron_right),
                                contentDescription = "프로필 더보기 버튼",
                                tint = moduBlack
                            )
                        }

                        Row(modifier = Modifier.padding(18.dp, 0.dp, 18.dp, 18.dp))
                        {
                            // 작성자 프로필 사진
                            Image(
                                painter = painterResource(id = R.drawable.ic_user),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(18.dp))
                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                            ) {
                                // 작성자 아이디
                                Text(text = "userID", style = moduBold, fontSize = 12.sp,)
                                // 작성자 카테고리
                                Text(text = "category", fontSize = 14.sp, color = Color.Gray)
                            }
                            Column(
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterVertically), horizontalAlignment = Alignment.End
                            ) {
                                //팔로우 버튼
                                Card(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(5.dp))
                                        .bounceClick {
                                            // 누르면 스낵바 메세지 띄워짐
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    "$userID 님을 팔로우 하였습니다.",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        },
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
                    }

                    // 구분선
                    Divider(color = moduGray_light, modifier = Modifier.fillMaxWidth().height(1.dp))
                    // 댓글 영역
                    Column(modifier = Modifier
                        .background(Color.White)
                        .bounceClick {
                            navController.navigate(NAV_ROUTE_POSTCONTENT.COMMENT.routeName)
                        }) {
                        Row(
                            modifier = Modifier
                                .padding(18.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            Text(text = "댓글", style = moduBold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.size(10.dp))
                            // 댓글 갯수
                            Text(text = "3", color = moduGray_strong, fontSize = 16.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            // 댓글 더보기 아이콘
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chevron_right),
                                contentDescription = "프로필 더보기 버튼",
                                tint = moduBlack
                            )
                        }
                        Row(
                            modifier = Modifier
                                .padding(18.dp, 0.dp, 18.dp, 18.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 댓글 작성자 프로필 사진
                            Image(
                                painter = painterResource(id = R.drawable.ic_user),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            // 댓글 내용
                            Text(text = "comment", color = moduBlack)
                        }
                    }

                    // 구분선
                    Divider(
                        color = moduGray_light,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )
                    // 상품 태그 영역
                    Column(modifier = Modifier
                        .background(Color.White)
                        .bounceClick { }) {
                        Row(
                            modifier = Modifier
                                .padding(18.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            Text(text = "상품 태그", style = moduBold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.size(10.dp))
                            // 상품 태그 갯수
                            Text(text = "2", color = moduGray_strong, fontSize = 16.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            // 상품 더보기 아이콘
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chevron_right),
                                contentDescription = "상품 더보기 아이콘",
                                tint = moduBlack
                            )

                        }
                        Column() {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp, 0.dp, 18.dp, 18.dp))
                            {
                                // 상품 1 사진
                                Image(
                                    painter = painterResource(id = R.drawable.plant1),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .border(0.5.dp, Color(0xFFCCCCCC), CircleShape)
                                    ,
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(18.dp))
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                ) {
                                    // 상품 이름
                                    Text(text = "Name", style = moduBold, fontSize = 12.sp,)
                                    // 상품 구매처 ?
                                    Text(text = "category", fontSize = 14.sp, color = Color.Gray)
                                }

                            }
                        }
                    }

                }
                // 뒤로 가기 버튼 (변경 필요)
                Icon(
                    modifier = Modifier
                        .padding(18.dp)
                        .bounceClick { activity?.finish() },
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "뒤로가기",
                    tint = moduBlack
                )
                var visible by remember { mutableStateOf(false) }

                if (scrollState.value > 0) visible =! visible

                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    visible = visible,
                    enter = fadeIn(),
                    exit = fadeOut() ) {
                    // 좋아요, 댓글, 스크랩, 더보기
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        // 구분선
                        Divider(
                            color = moduGray_light,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )

                        //버튼들
                        Row(
                            modifier = Modifier
                                .padding(18.dp)
                        ) {
                            // 좋아요, 스크랩 버튼 상태 변수
                            val isButtonClickedLike = remember { mutableStateOf(false) }
                            val isButtonClickedSave = remember { mutableStateOf(false) }
                            // 좋아요
                            Icon(modifier = Modifier
                                .padding(end = 18.dp)
                                .bounceClick {
                                    if (isButtonClickedLike.value)
                                        isButtonClickedLike.value = false
                                    else
                                        isButtonClickedLike.value = true
                                }, painter = painterResource
                                (
                                id =
                                if (isButtonClickedLike.value)
                                    R.drawable.ic_heart_solid
                                else
                                    R.drawable.ic_heart_line
                            ),
                                contentDescription = "좋아요",
                                tint =
                                if (isButtonClickedLike.value)
                                    Color(0xFFFF6767)
                                else
                                    moduBlack

                            )
                            // 댓글
                            Icon(
                                modifier = Modifier
                                    .padding(end = 18.dp)
                                    .bounceClick { },
                                painter = painterResource(id = R.drawable.ic_chat_line),
                                contentDescription = "댓글",
                                tint = moduBlack
                            )
                            // 스크랩
                            Icon(modifier = Modifier.bounceClick {
                                if (isButtonClickedSave.value)
                                    isButtonClickedSave.value = false
                                else
                                    isButtonClickedSave.value = true
                            }, painter = painterResource
                                (
                                id =
                                if (isButtonClickedSave.value)
                                    R.drawable.ic_star_solid
                                else
                                    R.drawable.ic_star_line
                            ),
                                contentDescription = "스크랩",
                                tint = moduBlack
                            )

                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                modifier = Modifier.bounceClick {
                                    //버튼 클릭하면 바텀 모달 상태 변수 바뀜
                                    showModalSheet.value= !showModalSheet.value
                                    scope.launch {
                                        bottomSheetState.show()
                                    }
                                },
                                painter = painterResource(id = R.drawable.ic_dot3_vertical),
                                contentDescription = "신고",
                                tint = moduBlack
                            )

                        }
                    }
                }

                // 커스텀한 알림
                SnackbarHost(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 18.dp),
                    hostState = snackbarHostState,
                    snackbar = { snackbarData: SnackbarData ->
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .background(
                                    Color("#62766B".toColorInt()),
                                    RoundedCornerShape(10.dp)
                                )
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


@Preview
@Composable
fun PostContentPreview(){
    PostContentScreen(navController = rememberNavController(), userID ="" )
}