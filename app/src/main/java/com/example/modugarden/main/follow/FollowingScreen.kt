package com.example.modugarden.main.follow

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.DeleteCurationResponse
import com.example.modugarden.api.dto.GetFollowFeedCurationContent
import com.example.modugarden.api.dto.PostDTO
import com.example.modugarden.api.dto.ReportCurationResponse
import com.example.modugarden.api.dto.ReportPostResponse
import com.example.modugarden.data.Report
import com.example.modugarden.main.content.ReportCategoryItem
import com.example.modugarden.main.content.modalDeleteCuration
import com.example.modugarden.main.content.modalDeletePost
import com.example.modugarden.main.content.reportCuration
import com.example.modugarden.main.content.reportPost
import com.example.modugarden.ui.theme.OneButtonSmallDialog
import com.example.modugarden.ui.theme.ShowProgressBar
import com.example.modugarden.ui.theme.SmallDialog
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduErrorPoint
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_normal
import com.example.modugarden.ui.theme.moduGray_strong
import com.example.modugarden.ui.theme.moduPoint
import com.example.modugarden.viewmodel.DeleteContentViewModel
import com.example.modugarden.viewmodel.UserViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    postLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    curationLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    lazyScroll: LazyListState
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)//바텀 시트
    val modalType =  remember { mutableStateOf(0) }
    val modalContentId = remember { mutableStateOf(0) }
    val modalContentImage :MutableState<String?> = remember { mutableStateOf("") }
    val modalContentTitle = remember { mutableStateOf("") }

    val reportDialogState = remember { mutableStateOf(false) }
    val messageDialogState = remember { mutableStateOf(false) }
    val reportCategory = remember{ mutableStateOf("") }
    val reportMessage = remember{ mutableStateOf("") }
    val deleteContentViewModel :DeleteContentViewModel = viewModel()


    Log.i("백백",bottomSheetState.isVisible.toString())
    val postList = remember { mutableStateListOf<PostDTO.GetFollowFeedPostContent>() }
    postList.clear()
    val curationList = remember { mutableStateListOf<GetFollowFeedCurationContent>() }
    curationList.clear()

    postList.addAll(posts)
    curationList.addAll(curations)
    if(messageDialogState.value)
        OneButtonSmallDialog(
            text = reportMessage.value,
            textColor = moduBlack,
            backgroundColor = Color.White,
            buttonText = "확인",
            buttonTextColor = Color.White,
            buttonColor = moduPoint,
            dialogState = messageDialogState
        ){
            messageDialogState.value=false
            reportMessage.value=""
        }
    if (reportDialogState.value){
        SmallDialog(
            text = "정말 신고할까요?",
            text2 = "신고는 취소할 수 없습니다.",
            textColor = moduBlack,
            backgroundColor = Color.White,
            positiveButtonText = "신고",
            negativeButtonText = "취소",
            positiveButtonTextColor = Color.White,
            negativeButtonTextColor = moduBlack,
            positiveButtonColor = moduErrorPoint,
            negativeButtonColor = moduBackground,
            dialogState = reportDialogState,
            reportCategory=reportCategory.value,
            reportType= modalType.value,
            reportMessage = reportMessage
        ) {
            Log.i("신고 정보", reportCategory.value + modalContentId.value.toString()+modalType.toString())
            if (modalType.value == reportPost){
                RetrofitBuilder.reportAPI
                    .reportPost(modalContentId.value, reportCategory.value)
                    .enqueue(object : Callback<ReportPostResponse> {
                        override fun onResponse(
                            call: Call<ReportPostResponse>,
                            response: Response<ReportPostResponse>
                        ) {
                            if (response.body()?.isSuccess == true) {
                                reportMessage.value = "소중한 의견을 주셔서 감사합니다!"
                                Log.i("게시글 신고", "성공+${response.body()}}")
                            } else { // 또 신고 했으면 알려줌
                                reportMessage.value = response.body()!!.message
                                Log.i("게시글 신고 실패", response.body()!!.message)
                            }
                        }

                        override fun onFailure(
                            call: Call<ReportPostResponse>,
                            t: Throwable
                        ) {
                            Log.i("게시글 신고", "서버 연결 실패")
                        }
                    }
                    )
            }
             if (modalType.value == reportCuration) {
                 Log.i("신고 정보", reportCategory.value + modalContentId.toString() + modalType.value)
                 RetrofitBuilder.reportAPI
                     .reportCuration(modalContentId.value, reportCategory.value)
                     .enqueue(object : Callback<ReportCurationResponse> {
                         override fun onResponse(
                             call: Call<ReportCurationResponse>,
                             response: Response<ReportCurationResponse>
                         ) {
                             if (response.body()?.isSuccess==true) {
                                 reportMessage.value = "소중한 의견을 주셔서 감사합니다!"
                                 Log.i("큐레이션 신고", "성공+${response.body()}")
                             } else {
                                 reportMessage.value = response.body()!!.message
                                 Log.i("큐레이션 신고 실패", response.body()!!.message)
                             }
                         }

                         override fun onFailure(
                             call: Call<ReportCurationResponse>,
                             t: Throwable
                         ) {
                             Log.i("큐레이션 신고", "서버 연결 실패")
                         }
                     })

             }
            messageDialogState.value = true
        }
    }

    ModalBottomSheetLayout(
        sheetElevation = 0.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetState = bottomSheetState,
        sheetContent = {
            if (modalType.value== modalDeletePost ||
                modalType.value==modalDeleteCuration){
                Card(
                    modifier = Modifier
                        .padding(10.dp),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(5.dp)
                                .background(moduGray_normal, RoundedCornerShape(30.dp))
                                .alpha(0.2f)
                        )

                        Spacer(modifier = Modifier.size(30.dp))

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 18.dp)
                        ) {
                            if (modalType.value== modalDeletePost) Text(text = "포스트를 삭제할까요?", style = moduBold, fontSize = 20.sp)
                            else Text(text = "큐레이션을 삭제할까요?", style = moduBold, fontSize = 20.sp)
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 30.dp)
                            ) {
                                GlideImage(
                                    imageModel =
                                    if(modalContentImage.value==null) R.drawable.ic_default_profile
                                    else modalContentImage.value,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .border(1.dp, moduGray_light, RoundedCornerShape(50.dp))
                                        .size(25.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop,
                                    requestOptions = {
                                        RequestOptions()
                                            .override(25,25)
                                    },
                                    loading = {
                                        ShowProgressBar()
                                    }
                                )
                                Spacer(modifier = Modifier.width(18.dp))
                                Text(
                                    modalContentTitle.value,
                                    fontSize = 16.sp,
                                    color = moduBlack,
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.weight(1f))


                            }
                            //버튼
                            Row {
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .bounceClick {
                                            scope.launch {
                                                bottomSheetState.hide()
                                            }
                                        },
                                    shape = RoundedCornerShape(10.dp),
                                    backgroundColor = moduGray_light,
                                    elevation = 0.dp
                                ) {
                                    Text(
                                        text = "취소",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = moduGray_strong,
                                        modifier = Modifier
                                            .padding(14.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Spacer(modifier = Modifier.size(18.dp))
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .bounceClick {
                                            if (modalType.value == modalDeletePost) {
                                                RetrofitBuilder.postAPI
                                                    .deletePost(modalContentId.value)
                                                    .enqueue(object :
                                                        Callback<PostDTO.DeletePostResponse> {
                                                        override fun onResponse(
                                                            call: Call<PostDTO.DeletePostResponse>,
                                                            response: Response<PostDTO.DeletePostResponse>
                                                        ) {
                                                            if (response.isSuccessful) Log.i(
                                                                "포스트 삭제",
                                                                "성공"
                                                            )
                                                            else Log.i("포스트 삭제", "실패")
                                                        }

                                                        override fun onFailure(
                                                            call: Call<PostDTO.DeletePostResponse>,
                                                            t: Throwable
                                                        ) {
                                                            Log.i("포스트 삭제", "서버 연결 실패")
                                                        }
                                                    })

                                                deleteContentViewModel.deletePost(
                                                    modalContentId.value,
                                                    postList
                                                )
                                                scope.launch {
                                                    bottomSheetState.hide()
                                                }
                                            } else {
                                                RetrofitBuilder.curationAPI
                                                    .deleteCuration(modalContentId.value)
                                                    .enqueue(object :
                                                        Callback<DeleteCurationResponse> {
                                                        override fun onResponse(
                                                            call: Call<DeleteCurationResponse>,
                                                            response: Response<DeleteCurationResponse>
                                                        ) {
                                                            if (response.body()?.isSuccess == true) Log.i(
                                                                "큐레이션 삭제",
                                                                "성공"
                                                            )
                                                            else Log.i("큐레이션 삭제", "실패")
                                                        }

                                                        override fun onFailure(
                                                            call: Call<DeleteCurationResponse>,
                                                            t: Throwable
                                                        ) {
                                                            Log.i("큐레이션 삭제", "서버 연결 실패")
                                                        }
                                                    })
                                                deleteContentViewModel.deleteCuration(
                                                    modalContentId.value,
                                                    curationList
                                                )
                                                scope.launch {
                                                    bottomSheetState.hide()
                                                }
                                            }


                                        },
                                    shape = RoundedCornerShape(10.dp),
                                    backgroundColor = Color(0xFFFF7272),
                                    elevation = 0.dp
                                ) {
                                    Text(
                                        text = "삭제",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color.White,
                                        modifier = Modifier
                                            .padding(14.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
            else {
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
                            if (modalType.value == reportPost) {
                                Text(text = "포스트 신고", style = moduBold, fontSize = 20.sp)
                            }
                            if (modalType.value == reportCuration) Text(
                                text = "큐레이션 신고",
                                style = moduBold,
                                fontSize = 20.sp
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                GlideImage(
                                    imageModel =
                                    if (modalContentImage.value == null) R.drawable.ic_default_profile
                                    else modalContentImage.value,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .border(1.dp, moduGray_light, RoundedCornerShape(50.dp))
                                        .size(25.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop,
                                    requestOptions = {
                                        RequestOptions()
                                            .override(25,25)
                                    },
                                    loading = {
                                        ShowProgressBar()
                                    }
                                )
                                Spacer(modifier = Modifier.size(18.dp))
                                Text(
                                    text = modalContentTitle.value,
                                    style = moduBold,
                                    fontSize = 14.sp
                                )
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
                                ReportCategoryItem(
                                    report = item,
                                    reportCategory = reportCategory,
                                    scope = scope,
                                    bottomSheetState=bottomSheetState,
                                    dialogState = reportDialogState
                                )
                            }
                        }
                        Spacer(modifier = Modifier.size(18.dp))
                    }


                }
            }
        })
    {
        BackHandler(enabled = bottomSheetState.isVisible) {
            scope.launch {
                bottomSheetState.hide()
            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(moduBackground)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(moduBackground)
            )
            {
                LazyColumn(
                    state = lazyScroll) {

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


                    items(postList,
                        key = { post -> post.board_id }) {post->
                        PostCard(
                            navFollowController,
                            data = post,
                            scope,
                            snackbarHostState,
                            bottomSheetState,
                            modalType = modalType,
                            modalTitle = modalContentTitle,
                            modalImage = modalContentImage,
                            modalId = modalContentId,
                            userViewModel = userViewModel,
                            feedLauncher = postLauncher,
                            fcmTokens = post.fcmTokens)
                    }

                    //큐레이션
                    Log.i("큐레이션 리스트",curationList.toString())
                       items(curationList,
                           key = { curation -> curation.curation_id }) {curation->
                           CurationCard(
                               navFollowController,
                               data = curation,
                               scope = scope,
                               snackbarHostState = snackbarHostState,
                               bottomSheetState = bottomSheetState,
                               modalType = modalType,
                               modalTitle = modalContentTitle,
                               modalImage = modalContentImage,
                               modalContentId = modalContentId,
                               userViewModel = userViewModel,
                               feedLauncher = curationLauncher
                           )
                       }

                    // 팔로우 피드 맨 끝
                    item {
                            FollowEndCard(navController)
                         }
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