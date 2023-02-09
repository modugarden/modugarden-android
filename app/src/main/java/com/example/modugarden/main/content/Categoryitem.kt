package com.example.modugarden.main.content

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.ReportCommentResponse
import com.example.modugarden.api.dto.ReportCurationResponse
import com.example.modugarden.api.dto.ReportPostResponse
import com.example.modugarden.data.Report
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_strong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReportCategoryItem(
    report: Report,
    id: MutableState<Int>,
    modalType: MutableState<Int>,
    scope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState
){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 18.dp)
        .bounceClick {
            Log.i("신고 타입",modalType.value.toString())
            if (modalType.value == modalReportPost) {
                Log.i("신고 정보",report.name+id.toString()+modalType.value)
                RetrofitBuilder.reportAPI
                    .reportPost(id.value, report.name)
                    .enqueue(object :Callback<ReportPostResponse>{
                        override fun onResponse(
                            call: Call<ReportPostResponse>,
                            response: Response<ReportPostResponse>
                        ) {
                            if (response.body()?.isSuccess == true) {
                                Log.i("게시글 신고", "성공+${response.body()}}")
                            } else Log.i("게시글 신고 실패", response.body().toString())
                        }

                        override fun onFailure(call: Call<ReportPostResponse>, t: Throwable) {
                            Log.i("게시글 신고", "서버 연결 실패")
                        }
                    }
                    )
            }
            if (modalType.value == modalReportCuration) {
                Log.i("신고 정보",report.name+id.toString()+modalType.value)
                RetrofitBuilder.reportAPI
                    .reportCuration(id.value,report.name)
                    .enqueue(object :Callback<ReportCurationResponse>{
                        override fun onResponse(
                            call: Call<ReportCurationResponse>,
                            response: Response<ReportCurationResponse>
                        ) {
                            if (response.isSuccessful) {
                                Log.i("큐레이션 신고", "성공+${response.body()}")
                            } else Log.i("큐레이션 신고", "실패")
                        }

                        override fun onFailure(call: Call<ReportCurationResponse>, t: Throwable) {
                            Log.i("큐레이션 신고", "서버 연결 실글")
                        }
                    })

            }
            if (modalType.value== modalReportComment) {
                Log.i("신고 정보",report.name+id.value.toString()+modalType.value)
                RetrofitBuilder.reportAPI
                    .reportComment(id.value, report.name)
                    .enqueue(object : Callback<ReportCommentResponse> {
                        override fun onResponse(
                            call: Call<ReportCommentResponse>,
                            response: Response<ReportCommentResponse>
                        ) {
                            if (response.isSuccessful) {
                                Log.i("댓글 신고", "성공+${response.body()}")
                            } else Log.i("댓글 신고", "실패")
                        }

                        override fun onFailure(
                            call: Call<ReportCommentResponse>,
                            t: Throwable
                        ) {
                            Log.i("댓글 신고", "서버 연결 실패")
                        }
                    })
            }
            scope.launch {
                bottomSheetState.hide()
            }
        },
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = report.type, color = moduBlack, fontSize = 16.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null, tint = moduGray_strong
        )
    }
}