package com.example.modugarden.api.api

import com.example.modugarden.api.dto.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface CurationAPI {

    // 탐색 피드 - 카테고리 별 큐레이션 검색
    @GET("/curations")
    fun getCategorySearchCuration(
        @Query("category") category :String
    ):Call<GetSearchCuration>

    // 탐색 피드 - 제목으로 큐레이션 검색
    @GET("/curations/search")
    fun getTitleSearchCuration(
        @Query("title") title: String
    ) : Call<GetSearchCuration>


    //큐레이션 피드 올리기
    @Multipart
    @POST("/curations")
    fun curationCreate(
        @Part("curationCreateRequest") curationCreateRequest: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<CurationUploadResponse>

    // 큐레이션 좋아요 추가
    @POST("/curations/{curation_id}/like")
    fun likeCuration(
        @Path ("curation_id") curation_id: Int
    ): Call<CurationLikeResponse>

    //큐레이션 좋아요 제거
    @DELETE("/curations/{curation_id}/unlike")
    fun unlikeCuration(
        @Path("curation_id") curation_id: Int
    ): Call<CurationLikeResponse>

    //큐레이션 좋아요 수
    @GET("/curations/like/{curation_id}")
    fun getCurationLikeNum(
        @Path("curation_id") curation_id: Int
    ):Call<CurationLikeResponse>

    //큐레이션 보관하기
    @POST("/curations/{curation_id}/storage")
    fun storeCuration(
        @Path ("curation_id") curation_id: Int
    ): Call <CurationStoreResponse>

    //큐레이션 보관 취소
    @DELETE("/curations/{curation_id}/storage")
    fun storeCancelCuration(
        @Path("curation_id") curation_id: Int
    ): Call <CurationStoreResponse>

    // 내 프로필- 큐레이션 조회
    @GET("/curations/me")
    fun getMyCuration():Call<GetCuration>

    // 내 프로필 - 큐레이션 좋아요 여부 조회
    @GET("/curations/me/like/{curation_id}")
    fun getStateCurationLike(
        @Path("curation_id") curation_id: Int
    ): Call <GetCurationLikeStateResponse>

    //프로필 페이지 - 저장한 큐레이션 조회
    @GET("/curations/me/storage")
    fun getMyCurationStorage():Call<GetCuration>

    // 큐레이션 보관 여부 조회
    @GET("/curations/me/storage/{curation_id}")
    fun getCurationStoreState(
        @Path ("curation_id") curation_id: Int
    ): Call <GetCurationLikeStateResponse>

    // 회원 - 큐레이션 조회
    @GET("/curations/users/{user_id}")
    fun getUserCuration(
        @Path ("user_id") user_id: Int
    ): Call <GetCuration>


    // 게시물 상세보기 - 큐레이션 하나 조회
    @GET("/curations/{curation_id}")
    fun getCuraionContent(
        @Query("curation_id") curation_id: Int
    ): Call <GetCurationResponse>

    //게시물 상세보기 - 큐레이션 삭제
    @DELETE ("/curations/{curation_id}")
    fun deleteCuration (
        @Body curation_id: Int
    ): Call <DeleteCurationResponse>

    @GET ("/curations/followfeed")
    fun getFollowFeedCuration() : Call<GetFollowFeedCuration>
}