package com.example.modugarden.main.content

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modugarden.ApplicationClass
import com.example.modugarden.ApplicationClass.Companion.refresh
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.GetCurationResponse
import com.example.modugarden.main.follow.moduBold
import com.example.modugarden.ui.theme.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("CommitPrefEdits")
@Composable
fun CurationContentScreen(curation_id :Int) {
    val focusManager = LocalFocusManager.current
    //액티비티 종료할 때 사용할 변수
    val activity = (LocalContext.current as? Activity)
    val isButtonClickedLike = remember { mutableStateOf(false) }
    val isButtonClickedSave = remember { mutableStateOf(false) }
    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var responseBody by remember { mutableStateOf(GetCurationResponse()) }

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .addFocusCleaner(focusManager)
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
                Text(text = curation!!.title, style = moduBold, fontSize = 16.sp)
                Spacer(modifier = Modifier.weight(1f))

                CurationHeartCard(
                    curationId = curation_id,
                    modifier = Modifier,
                    heartState = remember { mutableStateOf(curation.isliked) }
                )

                // 스크랩
                Icon(modifier = Modifier
                    .padding(end = 18.dp)
                    .bounceClick {
                        isButtonClickedSave.value = !isButtonClickedSave.value

                        if (isButtonClickedSave.value) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "게시물을 저장하였습니다.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
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