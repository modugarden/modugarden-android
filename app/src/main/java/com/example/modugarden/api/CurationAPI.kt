package com.example.modugarden.api

import retrofit2.Call
import retrofit2.http.*

interface CurationAPI {
    // 탐색 피드 - 카테고리 별 큐레이션 검색
    @GET("/curations")
    fun getCategorySearchCuration(
        @Query("category") category :String
    ):Call<GetCuration>

    // 탐색 피드 - 제목으로 큐레이션 검색
    @GET("/curations")
    fun getTitleSearchCuration(
        @Query("title") title: String
    ) : Call<GetCuration>

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
    fun getCurationLike(
        @Query("curation_id") curation_id: Long
    ):Call<CurationLikeResponse>

    //큐레이션 보관하기
    @POST("/curations/{curation_id}/storage")
    fun storeCuration(
        @Body curation_id: Long
    ): Call <CurationStoreResponse>

    //큐레이션 보관 취소
    @DELETE("/curations/{curation_id}/storage")
    fun storeCancelCuration(
        @Body curation_id: Long
    ): Call <CurationStoreResponse>

    // 내 프로필- 큐레이션 조회
    @GET("/curations/me")
    fun getMyCuration():Call<GetCuration>

    // 내 프로필 - 큐레이션 좋아요 여부 조회
    @GET("/curations/me/like/{curation_id}")
    fun getStateCurationLike(  @Body curation_id: Long
    ): Call <GetCurationLikeStateResponse>

    //프로필 페이지 - 저장한 큐레이션 조회
    @GET("/curations/me/storage")
    fun getMyCurationStorage():Call<GetCuration>

    // 큐레이션 보관 여부 조회
    @GET("/curations/me/storage/{curation_id}")
    fun getCurationStoreState(
        @Query ("curation_id") curation_id: Long
    ): Call <GetCurationLikeStateResponse>

    // 회원 - 큐레이션 조회
    @GET("/curations/users/{user_id}")
    fun getUserCuration(
        @Query ("curation_id") user_id: Long
    ): Call <GetCuration>


    // 게시물 상세보기 - 큐레이션 하나 조회
    @GET("/curations/{curation_id}")
    fun getCuraionContent(
        @Query("curation_id") curation_id: Long
    ): Call <GetCurationResponse>

    //게시물 상세보기 - 큐레이션 삭제
    @DELETE ("/curations/{curation_id}")
    fun deleteCurationContent (
        @Body curation_id: Long
    ): Call <DeleteCuartionResponse>

}