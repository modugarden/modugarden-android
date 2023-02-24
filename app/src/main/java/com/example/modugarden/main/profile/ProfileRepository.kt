package com.example.modugarden.main.profile

import android.util.Log
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.FollowListDtoResContent
import com.example.modugarden.api.dto.GetUserCurationsResponseContent
import com.example.modugarden.api.dto.PostDTO.GetUserPostResponseContent
import com.example.modugarden.api.dto.UserInfoResResult
import com.example.modugarden.main.profile.follow.FollowRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository {

    companion object {
        private val sProfileRepository: ProfileRepository = ProfileRepository()
        fun getInstance(): ProfileRepository {
            return sProfileRepository
        }
    }

    suspend fun getProfileInfo(id: Int): UserInfoResResult =
        withContext(Dispatchers.IO) {
            RetrofitBuilder.userAPI.readUserInfo(id).execute().body()?.result ?: UserInfoResResult()
        }

    suspend fun getProfilePosts(id: Int): List<GetUserPostResponseContent>? =
        withContext(Dispatchers.IO) {
            RetrofitBuilder.postAPI.getUserPost(id).execute().body()?.content
        }

    suspend fun getProfileCurations(id: Int): List<GetUserCurationsResponseContent>? =
        withContext(Dispatchers.IO) {
            RetrofitBuilder.curationAPI.getUserCuration(id).execute().body()?.content
        }
}