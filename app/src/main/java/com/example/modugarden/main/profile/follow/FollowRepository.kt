package com.example.modugarden.main.profile.follow

import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.FollowListDtoRes
import com.example.modugarden.api.dto.FollowListDtoResContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class FollowRepository {

    companion object {
        private val sFollowRepository: FollowRepository = FollowRepository()
        fun getInstance(): FollowRepository {
            return sFollowRepository
        }
    }

    suspend fun getFollowingList(id: Int): List<FollowListDtoResContent>? =
        withContext(Dispatchers.IO) {
            Log.d("follow repository", "getFollowList")
            RetrofitBuilder.followAPI.otherFollowingList(id).execute().body()?.content
        }

    suspend fun getFollowerList(id: Int): List<FollowListDtoResContent>? =
        withContext(Dispatchers.IO) {
            Log.d("follow repository", "getFollowerList")
            RetrofitBuilder.followAPI.otherFollowerList(id).execute().body()?.content
        }
}