package com.example.modugarden.api.api

import com.example.modugarden.api.dto.CurationLikeResponse
import com.example.modugarden.api.dto.PostDTO.*
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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

    // 게시물 상세보기 페이지- 포스트 하나 조회
    @GET("/boards/{board_id}")
    fun getPostContent(
        @Path("board_id") board_id:Int
    ):Call<GetPostResponse>

    // 게시물 상세보기 페이지- 포스트 삭제
    @DELETE("boards/{board_id}")
    fun deletePost(
        @Path("board_id") board_id: Int
    ):Call<DeletePostResponse>

    // 게시물 상세보기 페이지 - 포스트 좋아요 달기
    @POST("/boards/{board_id}/like")
    fun likePost(
        @Path ("board_id") board_id: Int
    ): Call<PostLikeResponse>

    // 게시물 상세보기 페이지 - 포스트 좋아요 취소
    @DELETE("/boards/{board_id}/unlike")
    fun unlikePost(
        @Path ("board_id") board_id: Int
    ): Call<PostLikeResponse>

    //게시물 상세보기 페이지 - 포스트 좋아요 조회
    @GET ("/boards/like/{board_id}")
    fun getPostLikeNum(
        @Path("board_id") board_id: Int
    ):Call<CurationLikeResponse>

    // 게시물 상세보기 페이지 - 포스트 보관
    @POST("/boards/{board_id}/storage")
    fun storePost(
        @Path ("board_id") board_id: Int
    ): Call<PostStoreResponse>

    // 게시물 상세보기 페이지 - 포스트 보관 취소
    @DELETE("/boards/{board_id}/storage")
    fun storeCancelPost(
        @Path ("board_id") board_id: Int
    ): Call<PostStoreResponse>

    // 내 프로필- 큐레이션 조회
    @GET("/boards/me")
    fun getMyPost():Call<GetPost>

    //내 프로필- 포스트 좋아요 여부 조회
    @GET("/boards/me/like/{board_id}")
    fun getPostLikeState(
        @Path  ("board_id") board_id: Int
    ): Call<GetPostLikeStateResponse>

    //프로필 페이지- 저장한 포스트 조회
    @GET("/boards/me/storage")
    fun getMyPostStorage() :Call<GetPost>

    //게시물 상세보기 - 회원 포스트 조회
    @GET ("/boards/users/{user_id}")
    fun getUserPost(
        @Path ("user_id") user_id :Int
    ): Call<GetPost>

    //팔로우 피드 포스트 불러오기
    @GET ("/boards/followfeed")
    fun getFollowFeedPost() :Call <GetFollowFeedPost>

}