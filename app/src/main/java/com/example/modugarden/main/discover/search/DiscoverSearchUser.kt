package com.example.modugarden.main.discover.search

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modugarden.api.RetrofitBuilder.userAPI
import com.example.modugarden.api.dto.FindByNicknameRes
import com.example.modugarden.ui.theme.ShowProgressBar
import com.example.modugarden.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun DiscoverSearchUser(
    searchStr : String,
    coroutineScope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    navController: NavController,
    userViewModel: UserViewModel
){

    var responseBody  by remember { mutableStateOf((FindByNicknameRes())) }

    var isLoading by remember { mutableStateOf(true) }

    userAPI
        .findByNickname(searchStr)
        .enqueue(object: Callback<FindByNicknameRes> {
            override fun onResponse(
                call: Call<FindByNicknameRes>,
                response: Response<FindByNicknameRes>
            ) {
                if(response.isSuccessful) {
                    val res = response.body()
                    if(res != null) {
                        responseBody = res
                        Log.d("upload-result123", responseBody.toString())
                        isLoading = false

                    }
                }
                else {
                    Log.d("upload-result", response.toString())
                }
            }

            override fun onFailure(call: Call<FindByNicknameRes>, t: Throwable) {
                Log.d("upload-result", "왜안됍")
            }

        })

    if(isLoading){
        ShowProgressBar()
    }
    else {
        val users = responseBody.content

        if(users!!.isEmpty()){
            DiscoverSearchNoResultScreen(searchStr)
        }
        else {
//            LazyColumn(
//                modifier = Modifier.fillMaxSize(),
//                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp)
//            ) {
//                itemsIndexed(users) { idx, item ->
//                    DiscoverSearchUserCard(item, coroutineScope, snackBarHostState, navController, userViewModel)
//                }
//            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp)
            ) { items(
                items = users,
                key = {users -> users.userId }
            ) { item ->
                DiscoverSearchUserCard(item, coroutineScope, snackBarHostState, navController, userViewModel)
            }
            }
        }
    }


}