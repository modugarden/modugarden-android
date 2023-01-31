package com.example.modugarden.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserAPI {

    // 닉네임으로 유저 검색
    @GET("/users")
    fun findByNickname(
        @Query("nickname") nickname: String
    ): Call<FindByNicknameRes>

    @GET("/users/{userId}/info")
    fun readUserInfo(
        @Path("userId") id: Int
    ): Call<UserInfoRes>

    @PATCH("/users/me/category")
    fun updateUserCategory(
        @Body categories: UpdateUserCategoryReq
    ): Call<UpdateUserCategoryRes>

    @GET("/users/me/info")
    fun currentUserInfo(): Call<MyUserInfoRes>

    @PATCH("/users/me/nickname")
    fun updateUserNickname(
        @Body nickname: UpdateUserNicknameReq
    ): Call<UpdateUserNicknameRes>

    @PATCH("/users/me/profileImg")
    fun updateProfileImg(

    )

    @GET("/users/me/setting-info")
    fun readUserSettingInfo(): Call<UserSettingInfoRes>

    @POST("/users/nickname/isDuplicated")
    fun isNicknameDuplicated(
        @Body nickname: NicknameDuplicatedCheckReq
    ): Call<NicknameDuplicatedCheckRes>
}