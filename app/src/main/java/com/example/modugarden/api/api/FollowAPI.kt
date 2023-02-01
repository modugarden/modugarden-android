package com.example.modugarden.api.api;

import com.example.modugarden.api.dto.FollowDtoRes
import com.example.modugarden.api.dto.FollowListDtoRes
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST;
import retrofit2.http.Path;

interface FollowAPI {

    // 팔로우
    @POST("/follow/{following_id}")
    fun follow (
        @Path("following_id") id: Int
    ): Call<FollowDtoRes>

    // 언팔로우
    @DELETE("/follow/{following_id}")
    fun unFollow (
        @Path("following_id") id: Int
    ): Call<FollowDtoRes>

    // 타 유저 팔로워 리스트
    @GET("/follow/{other_id}/follower")
    fun otherFollowerList(
        @Path("other_id") id: Int
    ): Call<FollowListDtoRes>

    // 타 유저 팔로잉 리스트
    @GET("/follow/{other_id}/following")
    fun otherFollowingList(
        @Path("other_id") id: Int
    ): Call<FollowListDtoRes>

    // 내 팔로워 리스트
    @GET("/follow/me/follower")
    fun myFollowerList(): Call<FollowListDtoRes>

    // 내 팔로잉 리스트
    @GET("/follow/me/following")
    fun myFollowingList(): Call<FollowListDtoRes>

    // 팔로우중인지 확인
    @GET("/follow/isFollowed/{id}")
    fun isFollowed(
        @Path("id") id: Int
    ): Call<FollowDtoRes>
}
