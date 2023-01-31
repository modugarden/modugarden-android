package com.example.modugarden.api.api

import com.example.modugarden.api.dto.CurationLikeResponse
import com.example.modugarden.api.dto.CurationStoreResponse
import com.example.modugarden.api.dto.GetFeedCuration
import retrofit2.Call
import retrofit2.http.*

interface CurationAPI {
    // 큐레이션 피드 불러오기
    @GET("/curations")
    fun getFeedCuration(
        @Query("category") category :String
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

    //큐레이션 좋아요 수
    @GET("/curations/like/{curation_id}")
    fun getLikeCuration(
        @Body curation_id: Int
    ):Call<CurationLikeResponse>

    //큐레이션 보관
    @POST("/curations/{curation_id}/storage")
    fun storeCuration(
        @Body curation_id: Int
    ): Call <CurationStoreResponse>

    //큐레이션 보관 취소
    @DELETE("/curations/{curation_id}/storage")
    fun storeCancelCuration(
        @Body curation_id: Int
    ): Call <CurationStoreResponse>


}