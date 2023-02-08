package com.example.modugarden.main.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.modugarden.R
import com.example.modugarden.api.AuthCallBack
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.GetStoredCurationsResponse
import com.example.modugarden.api.dto.GetStoredCurationsResponseContent
import com.example.modugarden.api.dto.GetUserCurationsResponseContent
import com.example.modugarden.api.dto.PostDTO
import com.example.modugarden.ui.theme.TopBar
import retrofit2.Call
import retrofit2.Response

class ProfileSaveActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            // 인텐트로 포스트, 큐레이션 리스트 받아옴
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                TopBar(
                    title = "저장한 항목",
                    titleIcon = R.drawable.ic_arrow_left_bold,
                    titleIconOnClick = {
                        finish()
                    },
                    bottomLine = false
                )

                val postList = remember { mutableStateOf<List<PostDTO.GetStoredPostResponseContent>?>(
                    listOf())
                }

                val curationList = remember { mutableStateOf<List<GetStoredCurationsResponseContent>?>(
                    listOf())
                }

                RetrofitBuilder.postAPI.getMyPostStorage()
                    .enqueue(object :
                        AuthCallBack<PostDTO.GetStoredPostResponse>(this@ProfileSaveActivity, "저장된 항목 부르기 성공!") {
                        override fun onResponse(
                            call: Call<PostDTO.GetStoredPostResponse>,
                            response: Response<PostDTO.GetStoredPostResponse>
                        ) {
                            super.onResponse(call, response)
                            if(response.body()?.content != null)
                                postList.value = response.body()?.content
                        }
                    })

                RetrofitBuilder.curationAPI.getMyCurationStorage()
                    .enqueue(object :
                        AuthCallBack<GetStoredCurationsResponse>(this@ProfileSaveActivity, "저장된 항목 부르기 성공!") {
                        override fun onResponse(
                            call: Call<GetStoredCurationsResponse>,
                            response: Response<GetStoredCurationsResponse>
                        ) {
                            super.onResponse(call, response)
                            if (response.body()?.content != null)
                                curationList.value = response.body()?.content
                        }
                    })

                StoredTab(postList.value!!, curationList.value!!, context)
            }
        }
    }
}