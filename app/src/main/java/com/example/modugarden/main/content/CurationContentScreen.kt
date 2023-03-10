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
    //???????????? ????????? ??? ????????? ??????
    val context = LocalContext.current
    val isButtonClickedLike = remember { mutableStateOf(false) }
    val isButtonClickedSave = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var responseBody by remember { mutableStateOf(GetCurationResponse()) }
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)//?????? ??????
    val userId
    = ApplicationClass.sharedPreferences.getInt(ApplicationClass.clientId, 0) //??? ?????????
    val isLoading = remember { mutableStateOf(true) }

    val modalType = remember{ mutableStateOf(modalLocationType) }// ?????? or ?????? ?????? ?????? ????????? ??????
    val reportCategory = remember{ mutableStateOf("") }
    val reportMessage = remember{ mutableStateOf("") }
    val reportDialogState = remember { mutableStateOf(false) }
    val messageDialogState = remember { mutableStateOf(false) }

    if (reportDialogState.value){
        SmallDialog(
            text = "?????? ????????????????",
            text2 = "????????? ????????? ??? ????????????.",
            textColor = moduBlack,
            backgroundColor = Color.White,
            positiveButtonText = "??????",
            negativeButtonText = "??????",
            positiveButtonTextColor = Color.White,
            negativeButtonTextColor = moduBlack,
            positiveButtonColor = moduErrorPoint,
            negativeButtonColor = moduBackground,
            dialogState = reportDialogState,
            reportCategory=reportCategory.value,
            reportMessage = reportMessage
        ) {
                 RetrofitBuilder.reportAPI
                     .reportCuration(curation_id, reportCategory.value)
                     .enqueue(object : Callback<ReportCurationResponse> {
                         override fun onResponse(
                             call: Call<ReportCurationResponse>,
                             response: Response<ReportCurationResponse>
                         ) {
                             if (response.body()?.isSuccess == true) {
                                 reportMessage.value = "????????? ????????? ????????? ???????????????!"
                             } else {
                                 reportMessage.value = response.body()!!.message
                             }
                             messageDialogState.value = true
                         }

                         override fun onFailure(
                             call: Call<ReportCurationResponse>,
                             t: Throwable
                         ) {
                            TODO()
                         }
                     })

        }
    }
    if(messageDialogState.value)
        OneButtonSmallDialog(
            text = reportMessage.value,
            textColor = moduBlack,
            backgroundColor = Color.White,
            buttonText = "??????",
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
                    }
                } else {
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
            if (modalType.value == modalReportCuration){
                ReportModal(
                    type = "????????????",
                    profileImage = curation!!.user_profile_image,
                    title = curation.title,
                    reportCategory = reportCategory ,
                    reportDialogState = reportDialogState,
                    scope = scope,
                    bottomSheetState = bottomSheetState
                )
            }
            else
                DeleteModal(
                    type = "????????????",
                    profileImage = curation!!.user_profile_image,
                    title = curation.title,
                    scope = scope,
                    bottomSheetState = bottomSheetState,
                    deleteAction = {
                            RetrofitBuilder.curationAPI
                                .deleteCuration(curation_id)
                                .enqueue(object :
                                    Callback<DeleteCurationResponse> {
                                    override fun onResponse(
                                        call: Call<DeleteCurationResponse>,
                                        response: Response<DeleteCurationResponse>
                                    ) {
                                        if (response.body()?.isSuccess == true) {
                                            (context as Activity).finish()
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<DeleteCurationResponse>,
                                        t: Throwable
                                    ) {
                                        TODO()
                                    }
                                })


                    }
                )

        }
    )
    {
        BackHandler(enabled = bottomSheetState.isVisible) {
            scope.launch {
                bottomSheetState.hide()
            }
        }
        // ??????
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
                // ?????? ?????? ??????
                Icon(
                    modifier = Modifier
                        .bounceClick { (context as Activity).finish()},
                    painter = painterResource(id = R.drawable.ic_arrow_left_bold),
                    contentDescription = "????????????",
                    tint = moduBlack
                )
                Text(
                    text = curation!!.title, style = moduBold, fontSize = 16.sp,
                    maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp))

                CurationHeartCard(
                    curationId = curation_id,
                    modifier = Modifier.padding(end = 18.dp),
                    heartState = isButtonClickedLike
                )

                // ?????????
                CurationSaveCard(
                    curationId = curation_id,
                    modifier =  Modifier.padding(end = 18.dp),
                    saveState = isButtonClickedSave,
                    scope = scope,
                    snackbarHostState=snackbarHostState)

                // ?????? ??????
                Icon(modifier = Modifier
                        .bounceClick {
                            //?????? ???????????? ?????? ?????? ?????? ?????? ??????
                            if(curation.user_id==userId) modalType.value = modalDeletePost
                            else modalType.value = modalReportCuration
                            scope.launch {
                                bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                            }},
                    painter = painterResource(id = R.drawable.ic_dot3_vertical),
                    contentDescription = "????????????",
                    tint = moduBlack
                )
            }
            // ?????????
            Divider(
                color = moduGray_light, modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
            // ?????????
            Box (){
                AndroidView(
                    factory = {
                        WebView(it)
                            .apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT)

                                webViewClient = object : WebViewClient(){
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
