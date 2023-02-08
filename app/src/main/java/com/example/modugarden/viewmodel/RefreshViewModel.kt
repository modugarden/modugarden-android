package com.example.modugarden.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.FollowRecommendationRes
import com.example.modugarden.api.dto.GetFollowFeedCuration
import com.example.modugarden.api.dto.GetFollowFeedCurationContent
import com.example.modugarden.api.dto.PostDTO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RefreshViewModel: ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            delay(1000)
            _isRefreshing.emit(false)
        }
    }

    fun  getRecommend(recommendRes: MutableState<FollowRecommendationRes>){
        RetrofitBuilder.followAPI.getRecommendFollowList(0)
            .enqueue(object : Callback<FollowRecommendationRes> {
                override fun onResponse(
                    call: Call<FollowRecommendationRes>,
                    response: Response<FollowRecommendationRes>
                ) {
                    if (response.isSuccessful) {
                        val res = response.body()
                        if (res != null) {
                            recommendRes.value = res
                            Log.i("추천", recommendRes.toString())
                        }
                    } else Log.i("추천", "실패")
                }

                override fun onFailure(call: Call<FollowRecommendationRes>, t: Throwable) {

                }
            })
    }

    fun getPosts(postRes: MutableState<PostDTO.GetFollowFeedPost>, context:Context?) {
        RetrofitBuilder.postAPI
            .getFollowFeedPost()
            .enqueue(object : Callback<PostDTO.GetFollowFeedPost> {
                override fun onResponse(
                    call: Call<PostDTO.GetFollowFeedPost>,
                    response: Response<PostDTO.GetFollowFeedPost>
                ) {
                    if (response.isSuccessful) {
                        val res = response.body()
                        Log.i("res",res.toString())
                        if (res != null) {
                            postRes.value = res
                        }
                            Log.d("follow-post-result", res.toString())
                    }
                }

                override fun onFailure(call: Call<PostDTO.GetFollowFeedPost>, t: Throwable) {
                    Toast.makeText(context, t.message + t.cause, Toast.LENGTH_SHORT).show()

                    Log.d("follow-post", t.message + t.cause)
                }
            })

    }

    @SuppressLint("RememberReturnType")
    fun getCurations(context: Context?):List<GetFollowFeedCurationContent>{
        var curations = mutableStateOf(emptyList<GetFollowFeedCurationContent>())
        RetrofitBuilder.curationAPI
            .getFollowFeedCuration()
            .enqueue(object : Callback<GetFollowFeedCuration> {
                override fun onResponse(
                    call: Call<GetFollowFeedCuration>,
                    response: Response<GetFollowFeedCuration>
                ) {
                    if (response.isSuccessful) {
                        val res = response.body()
                        if (res?.content != null) {
                            curations.value = res.content
                            Log.d("follow-post-result", res.toString())
                        }
                    } else {
                        Toast.makeText(context, "데이터를 받지 못했어요", Toast.LENGTH_SHORT).show()
                        Log.d("follow-curation-result", response.toString())
                    }
                }

                override fun onFailure(call: Call<GetFollowFeedCuration>, t: Throwable) {
                    Toast.makeText(context, "서버와 연결하지 못했어요", Toast.LENGTH_SHORT).show()

                    Log.d("follow-curation", "실패")
                }

            })
        return curations.value
    }

}

