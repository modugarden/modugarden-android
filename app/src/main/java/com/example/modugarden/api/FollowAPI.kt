package com.example.modugarden.api;

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST;
import retrofit2.http.Path;

interface FollowAPI {

    @POST("/follow/{following_id}")
    fun follow (
        @Path("following_id") id: String
    ): Call<FollowDtoRes>

    @DELETE("/follow/{following_id}")
    fun unFollow (
        @Path("following_id") id: String
    ): Call<FollowDtoRes>

    @GET("/follow/{user_id}/follower")
    fun otherFollowerList(
        @Path("user_id") id: String
    ): Call<FollowListDtoRes>

    @GET("/follow/{user_id}/following")
    fun otherFollowingList(
        @Path("user_id") id: String
    ): Call<FollowListDtoRes>

    @GET("/follow/me/follower")
    fun myFollowerList(): Call<FollowListDtoRes>

    @GET("/follow/me/following")
    fun myFollowingList(): Call<FollowListDtoRes>

    @GET("/follow/isFollowed/{id}")
    fun isFollowed(
        @Path("id") id: String
    ): Call<FollowDtoRes>
}
