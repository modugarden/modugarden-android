package com.example.modugarden.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.FollowRecommendationRes
import com.example.modugarden.api.dto.FollowRecommendationResContent
import com.example.modugarden.api.dto.GetFollowFeedCuration
import com.example.modugarden.api.dto.PostDTO
import com.example.modugarden.api.dto.*
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
    private val refreshPage = mutableStateOf(0)

    private val beforeRecommendList = mutableStateOf(FollowRecommendationRes().content)

    fun getBeforeRecommendList(): MutableState<List<FollowRecommendationResContent>> {
        return beforeRecommendList
    }

    fun setBeforeRecommendList(recommendList :List<FollowRecommendationResContent>) {
        beforeRecommendList.value = recommendList
    }

    fun getRefreshPage(): Int {
        return refreshPage.value
    }
    fun addRefreshPage() {
        refreshPage.value += 1
        if(refreshPage.value > 10) refreshPage.value = 0
    }
    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            delay(1000)
            _isRefreshing.emit(false)
        }
    }

    fun  getRecommend(
        recommendList: MutableState<List<FollowRecommendationResContent>>,
        page:Int=0
    ){
        RetrofitBuilder.followAPI.getRecommendFollowList(refreshPage.value)
            .enqueue(object : Callback<FollowRecommendationRes> {
                override fun onResponse(
                    call: Call<FollowRecommendationRes>,
                    response: Response<FollowRecommendationRes>
                ) {
                    if (response.isSuccessful) {
                        val res = response.body()
                        if (res != null) {
                            beforeRecommendList.value = res.content
                            recommendList.value = res.content
//                            recommendRes.value = res
                        }
                    }
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

    fun getCurations(curationRes:MutableState<GetFollowFeedCuration>,context: Context?){
        RetrofitBuilder.curationAPI
            .getFollowFeedCuration()
            .enqueue(object : Callback<GetFollowFeedCuration> {
                override fun onResponse(
                    call: Call<GetFollowFeedCuration>,
                    response: Response<GetFollowFeedCuration>
                ) {
                    if (response.isSuccessful) {
                        val res = response.body()
                        Log.i("res",res.toString())
                        if (res != null) {
                            curationRes.value = res
                        }
                        Log.d("follow-curation-result", res.toString())
                        }
                     else {
                        Toast.makeText(context, "데이터를 받지 못했어요", Toast.LENGTH_SHORT).show()
                        Log.d("follow-curation-result", response.toString())
                    }
                }

                override fun onFailure(call: Call<GetFollowFeedCuration>, t: Throwable) {
                    Toast.makeText(context, "서버와 연결하지 못했어요", Toast.LENGTH_SHORT).show()

                    Log.d("follow-curation", "실패")
                }

            })
    }

    fun getUserInfo(userInfoResResult:MutableState<UserInfoResResult>, context: Context?){
        RetrofitBuilder.userAPI
            .readUserInfo(userInfoResResult.value.id)
            .enqueue(object : Callback<UserInfoRes> {
                override fun onResponse(
                    call: Call<UserInfoRes>,
                    response: Response<UserInfoRes>
                ) {
                    if (response.isSuccessful) {
                        val res = response.body()
                        Log.i("res",res.toString())
                        if (res != null) {
                            userInfoResResult.value = res.result
                        }
                    }
                }
                override fun onFailure(call: Call<UserInfoRes>, t: Throwable) {
                    Toast.makeText(context, "서버와 연결하지 못했어요", Toast.LENGTH_SHORT).show()
                }

            })
    }
}

