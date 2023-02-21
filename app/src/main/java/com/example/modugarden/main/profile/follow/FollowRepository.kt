package com.example.modugarden.main.profile.follow

import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.FollowListDtoRes
import com.example.modugarden.api.dto.FollowListDtoResContent
import retrofit2.Response

object FollowRepository {

    fun getFollowingList(id: Int): List<FollowListDtoResContent> {
        return RetrofitBuilder.followAPI.otherFollowingList(id).execute().body()?.content ?: listOf()
    }

    fun getFollowerList(id: Int): List<FollowListDtoResContent> {
        return RetrofitBuilder.followAPI.otherFollowerList(id).execute().body()?.content ?: listOf()
    }
}