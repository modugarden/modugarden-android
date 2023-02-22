package com.example.modugarden.main.follow

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.DeleteCurationResponse
import com.example.modugarden.api.dto.GetFollowFeedCurationContent
import com.example.modugarden.api.dto.PostDTO
import com.example.modugarden.api.dto.ReportCurationResponse
import com.example.modugarden.api.dto.ReportPostResponse
import com.example.modugarden.main.content.modalDeleteCuration
import com.example.modugarden.main.content.modalDeletePost
import com.example.modugarden.main.content.modalReportCuration
import com.example.modugarden.main.content.modalReportPost
import com.example.modugarden.ui.theme.DeleteModal
import com.example.modugarden.ui.theme.OneButtonSmallDialog
import com.example.modugarden.ui.theme.ReportModal
import com.example.modugarden.ui.theme.SmallDialog
import com.example.modugarden.ui.theme.SnackBar
import com.example.modugarden.ui.theme.moduBackground
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduErrorPoint
import com.example.modugarden.ui.theme.moduPoint
import com.example.modugarden.viewmodel.RefreshViewModel
import com.example.modugarden.viewmodel.UserViewModel
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
    lazyScroll: LazyListState,
    refreshViewModel: RefreshViewModel,
    postRes: MutableState<PostDTO.GetFollowFeedPost>,
    context: Context
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)//바텀 시트
    val modalType =  remember { mutableStateOf(0) }
    val modalContentId = remember { mutableStateOf(0) }
    val modalContentImage :MutableState<String?> = remember { mutableStateOf("") }
    val modalContentTitle = remember { mutableStateOf("") }
    val reportMessage = remember { mutableStateOf("") }
    val reportDialogState = remember { mutableStateOf(false) }
    val messageDialogState = remember { mutableStateOf(false) }
    val reportCategory = remember{ mutableStateOf("") }

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

            if (modalType.value == modalReportPost){
                RetrofitBuilder.reportAPI
                    .reportPost(modalContentId.value, reportCategory.value)
                    .enqueue(object : Callback<ReportPostResponse> {
                        override fun onResponse(
                            call: Call<ReportPostResponse>,
                            response: Response<ReportPostResponse>
                        ) {
                            if (response.body()?.isSuccess == true) {
                                reportMessage.value = "소중한 의견을 주셔서 감사합니다!"
                            } else {
                                reportMessage.value = response.body()!!.message
                            }
                        }

                        override fun onFailure(
                            call: Call<ReportPostResponse>,
                            t: Throwable
                        ) {
                            //서버 연결 실패
                        }
                    }
                    )
            }
             if (modalType.value == modalReportCuration) {
                 RetrofitBuilder.reportAPI
                     .reportCuration(modalContentId.value, reportCategory.value)
                     .enqueue(object : Callback<ReportCurationResponse> {
                         override fun onResponse(
                             call: Call<ReportCurationResponse>,
                             response: Response<ReportCurationResponse>
                         ) {
                             if (response.body()?.isSuccess==true) {
                                 reportMessage.value = "소중한 의견을 주셔서 감사합니다!"
                             } else {
                                 reportMessage.value = response.body()!!.message
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
                DeleteModal(
                    type = if (modalType.value== modalDeletePost)"포스트" else "큐레이션",
                    profileImage = modalContentImage.value,
                    title = modalContentTitle.value,
                    scope = scope,
                    bottomSheetState = bottomSheetState,
                    deleteAction = {
                        if (modalType.value == modalDeletePost) {
                            RetrofitBuilder.postAPI
                                .deletePost(modalContentId.value)
                                .enqueue(object :
                                    Callback<PostDTO.DeletePostResponse> {
                                    override fun onResponse(
                                        call: Call<PostDTO.DeletePostResponse>,
                                        response: Response<PostDTO.DeletePostResponse>
                                    ) {
                                        if (response.body()?.isSuccess == true) {
                                            refreshViewModel.getPosts(
                                                postRes,
                                                context
                                            )
                                            postList.remove(postList.find { it.board_id==modalContentId.value })
                                            scope.launch {
                                                bottomSheetState.hide()
                                            }
                                        } else TODO()
                                    }

                                    override fun onFailure(
                                        call: Call<PostDTO.DeletePostResponse>,
                                        t: Throwable
                                    ) {
                                        Log.i("포스트 삭제", "서버 연결 실패")
                                    }
                                })

                        }
                        else {
                            RetrofitBuilder.curationAPI
                                .deleteCuration(modalContentId.value)
                                .enqueue(object :
                                    Callback<DeleteCurationResponse> {
                                    override fun onResponse(
                                        call: Call<DeleteCurationResponse>,
                                        response: Response<DeleteCurationResponse>
                                    ) {
                                        if (response.body()?.isSuccess == true){
                                            curationList.remove(curationList.find { it.curation_id==modalContentId.value })
                                            scope.launch {
                                                bottomSheetState.hide()
                                            }
                                        }
                                        else TODO()
                                    }

                                    override fun onFailure(
                                        call: Call<DeleteCurationResponse>,
                                        t: Throwable
                                    ) {
                                        Log.i("큐레이션 삭제", "서버 연결 실패")
                                    }
                                })

                        }
                    }
                )
            }
            if(modalType.value== modalReportPost ||
                modalType.value==modalReportCuration) {
                ReportModal(
                    type =  if (modalType.value== modalReportPost)"포스트" else "큐레이션",
                    profileImage = modalContentImage.value,
                    title = modalContentTitle.value,
                    reportCategory = reportCategory,
                    reportDialogState = reportDialogState,
                    scope = scope,
                    bottomSheetState = bottomSheetState
                )

            }
            else{
                Spacer(modifier = Modifier.size(10.dp))
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
                            launcher = postLauncher,
                            fcmTokens = post.fcmTokens)
                    }

                    //큐레이션
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
                               launcher = curationLauncher
                           )
                       }

                    // 팔로우 피드 맨 끝
                    item {
                            FollowEndCard(navController)
                         }
                }

            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(30.dp)
            ) {
                SnackBar(snackbarHostState = snackbarHostState)
            }
        }
    }

}