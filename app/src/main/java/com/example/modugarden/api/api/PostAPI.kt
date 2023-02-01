package com.example.modugarden.api.api

import com.example.modugarden.api.dto.GetSearchCuration
import com.example.modugarden.api.dto.PostDTO
import com.example.modugarden.api.dto.PostDTO.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PostAPI {

    // 탐색 피드 - 카테고리 별 포스트 검색
    @GET("/boards")
    fun getCategorySearchPost(
        @Query("category") category :String
    ): Call<GetSearchPost>

    // 탐색 피드 - 제목으로 포스트 검색
    @GET("/boards/search")
    fun getTitleSearchPost(
        @Query("title") title: String
    ) : Call<GetSearchPost>
}