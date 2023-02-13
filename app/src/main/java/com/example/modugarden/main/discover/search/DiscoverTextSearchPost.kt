package com.example.modugarden.main.discover.search

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.PostDTO.*
import com.example.modugarden.data.Category
import com.example.modugarden.ui.theme.ShowProgressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//탐색피드에서 탭 래이아웃 아래에 보여줄 검색결과 뜨는 Composable인데
//생각해보면 Post랑 Curation이랑 똑같이 생겨서 보내줄 인수만 바꿔줘도 되지 않을까???
@Composable
fun DiscoverTextSearchPost(searchText: String){
    val context = LocalContext.current

    val responseBody  = remember { mutableStateOf(GetSearchPost()) }

    val isLoading = remember { mutableStateOf(true) }

    RetrofitBuilder.postAPI
        .getTitleSearchPost(searchText)
        .enqueue(object: Callback<GetSearchPost> {
            override fun onResponse(
                call: Call<GetSearchPost>,
                response: Response<GetSearchPost>
            ) {
                if(response.isSuccessful) {
                    val res = response.body()
                    if(res != null) {
                        responseBody.value = res
                        Log.d("upload-result123", responseBody.toString())
                        isLoading.value = false
                    }
                }
                else {
                    Toast.makeText(context, "데이터를 받지 못했어요", Toast.LENGTH_SHORT).show()
                    Log.d("upload-result", response.toString())
                }
            }

            override fun onFailure(call: Call<GetSearchPost>, t: Throwable) {
                Toast.makeText(context, "서버와 연결하지 못했어요", Toast.LENGTH_SHORT).show()

                Log.d("upload-result", "왜안됍")
            }

        })

    if(isLoading.value){
        ShowProgressBar()
    }
    else {
        val posts = responseBody.value.content

        if(posts == null){
            DiscoverSearchNoResultScreen(searchText)
        }
        else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp)
            ) {
                itemsIndexed(posts) { idx, item ->
                    DiscoverSearchPostCard(item, responseBody)
                }
            }
        }
    }




}

