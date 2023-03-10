package com.example.modugarden.main.discover.search

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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


@Composable
fun DiscoverCategorySearchPost(category: Category){
    val context = LocalContext.current

    val responseBody  = remember { mutableStateOf(GetSearchPost()) }

    val isLoading = remember { mutableStateOf(true) }

    RetrofitBuilder.postAPI
        .getCategorySearchPost(category.category)
        .enqueue(object: Callback<GetSearchPost> {
            override fun onResponse(
                call: Call<GetSearchPost>,
                response: Response<GetSearchPost>
            ) {
                if(response.isSuccessful) {
                    val res = response.body()
                    if(res != null) {
                        responseBody.value = res
                        isLoading.value = false
                    }
                }
                else {
                    Toast.makeText(context, "데이터를 받지 못했어요", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetSearchPost>, t: Throwable) {
                Toast.makeText(context, "서버와 연결하지 못했어요", Toast.LENGTH_SHORT).show()
            }

        })

    if(isLoading.value){
        ShowProgressBar()
    }
    else {
        val posts = responseBody.value.content

        if(posts == null){
            DiscoverSearchNoResultScreen(category.category)
        }
        else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp)
            ) { items(
                items = posts,
                key = {post -> post.id }
            ) { item ->
                    DiscoverSearchPostCard(item, responseBody)
                }
            }
        }
    }




}

