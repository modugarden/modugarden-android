package com.example.modugarden.main.content

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.ApplicationClass
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.DeleteCurationResponse
import com.example.modugarden.api.dto.GetCurationResponse
import com.example.modugarden.api.dto.PostDTO
import com.example.modugarden.api.dto.ReportCurationResponse
import com.example.modugarden.api.dto.ReportPostResponse
import com.example.modugarden.data.Report
import com.example.modugarden.main.follow.moduBold
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.RefreshViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("CommitPrefEdits", "UnrememberedMutableState")
@Composable
fun CurationContentScreen(curation_id :Int) {
    val focusManager = LocalFocusManager.current
    //액티비티 종료할 때 사용할 변수
    val context = LocalContext.current
    val isButtonClickedLike = remember { mutableStateOf(false) }
    val isButtonClickedSave = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var responseBody by remember { mutableStateOf(GetCurationResponse()) }
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)//바텀 시트
    val userId
    = ApplicationClass.sharedPreferences.getInt(ApplicationClass.clientId, 0) //내 아이디
    val isLoading = remember { mutableStateOf(true) }

    val modalType = remember{ mutableStateOf(modalLocationType) }// 신고 or 위치 모달 타입 정하는 변수
    val reportCategory = remember{ mutableStateOf("") }
    val reportMessage = remember{ mutableStateOf("") }
    val reportDialogState = remember { mutableStateOf(false) }
    val messageDialogState = remember { mutableStateOf(false) }

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
            reportMessage = reportMessage
        ) {
                 Log.i("신고 정보", reportCategory.value + curation_id.toString() )
                 RetrofitBuilder.reportAPI
                     .reportCuration(curation_id, reportCategory.value)
                     .enqueue(object : Callback<ReportCurationResponse> {
                         override fun onResponse(
                             call: Call<ReportCurationResponse>,
                             response: Response<ReportCurationResponse>
                         ) {
                             if (response.body()?.isSuccess == true) {
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
            messageDialogState.value = true
        }
    }
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
        }
    RetrofitBuilder.curationAPI.getCuraionContent(curation_id)
        .enqueue(object : Callback<GetCurationResponse> {
            override fun onResponse(
                call: Call<GetCurationResponse>,
                response: Response<GetCurationResponse>
            ) {
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res != null) {
                        responseBody = res
                        Log.d("curation-activity-result", responseBody.toString())
                    }
                } else {
                    Toast.makeText(context, "데이터를 받지 못했어요", Toast.LENGTH_SHORT).show()
                    Log.d("curation-activity-result", response.toString())
                }
            }

            override fun onFailure(call: Call<GetCurationResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    if (responseBody.result != null) {
        val curation = responseBody.result

    ModalBottomSheetLayout(
        sheetElevation = 0.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetState = bottomSheetState,
        sheetContent = {
            if (modalType.value == modalReportType) {
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
                            Text(text = "큐레이션 신고", style = moduBold, fontSize = 20.sp)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                GlideImage(
                                    imageModel =
                                    curation!!.user_profile_image ?: R.drawable.ic_default_profile,
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
                                Text(text = curation.title, style = moduBold, fontSize = 14.sp,maxLines = 1, overflow = TextOverflow.Ellipsis)
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
            else {
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
                            Text(text = "큐레이션 삭제할까요?", style = moduBold, fontSize = 20.sp)

                            Row(
                                modifier = Modifier
                                    .padding(vertical = 30.dp)
                            ) {
                                GlideImage(
                                    imageModel = curation!!.user_profile_image
                                        ?: R.drawable.ic_default_profile,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .border(1.dp, moduGray_light, RoundedCornerShape(50.dp))
                                        .size(25.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(18.dp))
                                Text(
                                    curation.title,
                                    fontSize = 16.sp,
                                    color = moduBlack,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Spacer(modifier = Modifier.weight(1f))


                            }
                            //버튼
                            Row {
                                // 취소
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

                                //삭제
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .bounceClick {
                                            RetrofitBuilder.curationAPI
                                                .deleteCuration(curation_id)
                                                .enqueue(object :
                                                    Callback<DeleteCurationResponse> {
                                                    override fun onResponse(
                                                        call: Call<DeleteCurationResponse>,
                                                        response: Response<DeleteCurationResponse>
                                                    ) {
                                                        if (response.body()?.isSuccess == true) {
                                                            Log.i(
                                                                "큐레이션 삭제",
                                                                "성공"
                                                            )
                                                            (context as Activity).finish()
                                                        } else Log.i("큐레이션 삭제", "실패")
                                                    }

                                                    override fun onFailure(
                                                        call: Call<DeleteCurationResponse>,
                                                        t: Throwable
                                                    ) {
                                                        Log.i("큐레이션 삭제", "서버 연결 실패")
                                                    }
                                                })

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



        }
    )
    {
        BackHandler(enabled = bottomSheetState.isVisible) {
            scope.launch {
                bottomSheetState.hide()
            }
        }
        // 상단
        Column(
            modifier = Modifier
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth()
                    .background(Color.White)
                ,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                // 뒤로 가기 버튼
                Icon(
                    modifier = Modifier
                        .bounceClick { (context as Activity).finish()},
                    painter = painterResource(id = R.drawable.ic_arrow_left_bold),
                    contentDescription = "뒤로가기",
                    tint = moduBlack
                )
                Text(
                    text = curation!!.title, style = moduBold, fontSize = 16.sp,
                    maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f).padding(start = 10.dp))

                CurationHeartCard(
                    curationId = curation_id,
                    modifier = Modifier.padding(end = 18.dp),
                    heartState = isButtonClickedLike
                )

                // 스크랩
                CurationSaveCard(
                    curationId = curation_id,
                    modifier =  Modifier.padding(end = 18.dp),
                    saveState = isButtonClickedSave,
                    scope = scope,
                    snackbarHostState=snackbarHostState)

                // 메뉴 버튼
                Icon(
                    modifier = Modifier
                        .bounceClick {
                            //버튼 클릭하면 바텀 모달 상태 변수 바뀜
                            if(curation.user_id==userId) modalType.value = modalDeletePost
                            else modalType.value = modalReportType
                            scope.launch {
                                bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                            }},
                    painter = painterResource(id = R.drawable.ic_dot3_vertical),
                    contentDescription = "뒤로가기",
                    tint = moduBlack
                )
                /*Icon(painter = painterResource(id = R.drawable.ic_xmark),
                    contentDescription = "창 닫기",
                    modifier = Modifier.bounceClick {
                        activity?.finish()
                    })*/
            }
            // 구분선
            Divider(
                color = moduGray_light, modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
            // 컨텐츠
            Box (){
                AndroidView(
                    factory = {
                        WebView(it)
                            .apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT)

                                webViewClient = object : WebViewClient(){

                                    /*override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                        super.onPageStarted(view, url, favicon)
                                        isLoading.value=true
                                    }*/

                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        isLoading.value=false
                                    }
                                }
                            }
                    }, update = { it.loadUrl(curation!!.link) }
                )
                if (isLoading.value) ShowProgressBarV2()
            }
        }




        }
    }

}

//@Preview
//@Composable
//fun CurationContentPreview() {
//
//    CurationContent("")
//}