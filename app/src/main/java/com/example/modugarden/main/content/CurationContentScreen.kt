package com.example.modugarden.main.content

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modugarden.ApplicationClass
import com.example.modugarden.ApplicationClass.Companion.refresh
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.DeleteCurationResponse
import com.example.modugarden.api.dto.GetCurationResponse
import com.example.modugarden.api.dto.PostDTO
import com.example.modugarden.main.follow.moduBold
import com.example.modugarden.ui.theme.*
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("CommitPrefEdits")
@Composable
fun CurationContentScreen(curation_id :Int) {
    val focusManager = LocalFocusManager.current
    //액티비티 종료할 때 사용할 변수
    val activity = (LocalContext.current as? Activity)
    val isButtonClickedLike = remember { mutableStateOf(false) }
    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var responseBody by remember { mutableStateOf(GetCurationResponse()) }
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)//바텀 시트

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
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .addFocusCleaner(focusManager),
        sheetState = bottomSheetState,
        sheetContent = {
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
                                    imageModel = curation!!.user_profile_image ?: R.drawable.ic_default_profile,
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
                                            scope.launch {
                                                bottomSheetState.hide()
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
    )
    {

        // 상단
        Column(
            modifier = Modifier
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(text = curation!!.title, style = moduBold, fontSize = 16.sp,
                maxLines = 2)
                Spacer(modifier = Modifier.weight(1f))

                CurationHeartCard(
                    curationId = curation_id,
                    modifier = Modifier.padding(end = 18.dp),
                    heartState = isButtonClickedLike
                )

                // 스크랩
                Icon(
                    painter = painterResource(id = R.drawable.baseline_delete_24) ,
                    contentDescription = "삭제",
                    tint= moduBlack,
                    modifier = Modifier
                        .padding(end = 18.dp)
                        .bounceClick {
                            scope.launch {
                                bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                            }
                    })

                Icon(painter = painterResource(id = R.drawable.ic_xmark),
                    contentDescription = "창 닫기",
                    modifier = Modifier.bounceClick {
                        activity?.finish()
                    })
            }
            // 구분선
            Divider(
                color = moduGray_light, modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
            // 컨텐츠
            AndroidView(factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClient()
                    loadUrl(curation!!.link)
                }
            }, update = { it.loadUrl(curation!!.link) }
            )
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