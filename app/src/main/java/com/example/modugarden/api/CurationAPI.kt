package com.example.modugarden.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface CurationAPI {

    // 큐레이션 피드 불러오가
    @GET("/curations")
    fun getFeedCuration(
        @Body category :String
    ):Call<GetFeedCuration>

    // 큐레이션 좋아요 추가
    @POST("/curations/{curation_id}/like")
    fun likeCuration(
        @Body curation_id: Int
    ): Call<CurationLikeResponse>

    //큐레이션 좋아요 제거
    @DELETE("/curations/{curation_id}/unlike")
    fun unlikeCuration(
        @Body curation_id: Int
    ): Call<CurationLikeResponse>
}