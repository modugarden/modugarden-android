package com.example.modugarden.main.discover.search

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.GetSearchCuration
import com.example.modugarden.api.dto.GetSearchCurationContent
import com.example.modugarden.data.Category
import com.example.modugarden.main.profile.follow.ProfileCard
import com.example.modugarden.ui.theme.ShowProgressBar
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun DiscoverCategorySearchCuration(
    responseBody:  MutableState<GetSearchCuration>,
    searchCategory: Category
){

    val context = LocalContext.current
    val isLoading = remember{ mutableStateOf(true) }

    RetrofitBuilder.curationAPI
        .getCategorySearchCuration(searchCategory.category)
        .enqueue(object: Callback<GetSearchCuration> {
            override fun onResponse(
                call: Call<GetSearchCuration>,
                response: Response<GetSearchCuration>
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
                    Log.d("upload-result", response.toString())
                }
            }

            override fun onFailure(call: Call<GetSearchCuration>, t: Throwable) {
                Toast.makeText(context, "서버와 연결하지 못했어요", Toast.LENGTH_SHORT).show()

                Log.d("upload-result", "왜안됍")
            }

        })
    if(isLoading.value){
        ShowProgressBar()
    }
    else {
        val curations = responseBody.value.content
        if(curations == null){
            DiscoverSearchNoResultScreen(searchCategory.category)
        }
        else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp)
            ) {
                items(
                    items = curations,
                    key = {curation -> curation.id }
                ) { item ->
                    DiscoverSearchCurationCard(item)
                }
            }

        }
    }

}