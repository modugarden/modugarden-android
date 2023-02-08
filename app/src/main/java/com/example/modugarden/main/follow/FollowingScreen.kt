package com.example.modugarden.main.follow

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.api.dto.GetFollowFeedCuration
import com.example.modugarden.api.dto.GetFollowFeedCurationContent
import com.example.modugarden.api.dto.PostDTO
import com.example.modugarden.data.Report
import com.example.modugarden.main.content.ReportCategoryItem
import com.example.modugarden.main.content.modalReportPost
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_normal
import com.example.modugarden.viewmodel.RefreshViewModel
import com.example.modugarden.viewmodel.UserViewModel
import com.skydoves.landscapist.glide.GlideImage

@SuppressLint("UnrememberedMutableState", "SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable //팔로우 피드.
fun FollowingScreen(
    posts: List<PostDTO.GetFollowFeedPostContent>,
    curations: List<GetFollowFeedCurationContent>,
    navController: NavHostController,
    navFollowController: NavHostController,
    userViewModel: UserViewModel,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)//바텀 시트
    val scrollState = rememberLazyListState()

    val modalType =  mutableStateOf(0)
    val modalContentId = remember { mutableStateOf(0) }
    val modalContentImage = remember { mutableStateOf("") }
    val modalContentTitle = remember { mutableStateOf("") }



    ModalBottomSheetLayout(
        sheetElevation = 0.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetState = bottomSheetState,
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
                        if (modalType.value== modalReportPost) {
                            Text(text = "포스트 신고", style = moduBold, fontSize = 20.sp)
                        } else Text(text = "큐레이션 신고", style = moduBold, fontSize = 20.sp)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GlideImage(
                                imageModel = modalContentImage.value,
                                contentDescription = "",
                                modifier = Modifier
                                    .border(1.dp, moduGray_light, RoundedCornerShape(50.dp))
                                    .size(25.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.size(18.dp))
                            Text(text =modalContentTitle.value, style = moduBold, fontSize = 14.sp)
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
                        itemsIndexed(
                            listOf(
                                Report.ABUSE,
                                Report.TERROR,
                                Report.SEXUAL,
                                Report.FISHING,
                                Report.INAPPROPRIATE
                            )
                        ) { index, item ->
                            Log.i("신고 타입/아이디",modalType.value.toString()+"/"+modalContentId.value)
                            ReportCategoryItem(
                                report = item,
                                id =modalContentId.value,
                                modalType = modalType.value,
                                scope,bottomSheetState)
                        }


                    }
                    Spacer(modifier = Modifier.size(18.dp))
                }


            }
        })
    {
        Box() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(moduBackground)
            )
            {
                LazyColumn(state = scrollState) {

                    // 상단 로고
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp, 30.dp, 30.dp, 20.dp),
                        ) {
                            Spacer(Modifier.weight(1f))
                            Image(
                                painter = painterResource(id = R.drawable.ic_logo_modern),
                                contentDescription = null,
                            )
                            Spacer(Modifier.weight(1f))
                        }
                    }
                    //포스트 카드

                    items(posts,
                        key = { post -> post.board_id }) {
                        PostCard(
                            navController,
                            data = it,
                            scope,
                            snackbarHostState,
                            bottomSheetState,
                            modalType = modalType,
                            modalTitle = modalContentTitle,
                            modalImage = modalContentImage,
                            modalId = modalContentId,
                            userViewModel = userViewModel
                        )
                    }

                    //큐레이션
                       items(curations,
                           key = { curation -> curation.curation_id }) {
                           CurationCard(
                               navController,
                               data = it,
                               scope = scope,
                               snackbarHostState = snackbarHostState,
                               bottomSheetState = bottomSheetState,
                               modalType = modalType,
                               modalTitle = modalContentTitle,
                               modalImage = modalContentImage,
                               modalContentId,
                               userViewModel = userViewModel
                           )
                       }


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