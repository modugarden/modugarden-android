package com.example.modugarden.api.api

import com.example.modugarden.api.dto.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
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

    @GET("/users/me/info")
    fun currentUserInfo(): Call<MyUserInfoRes>

    @GET("/users/me/setting-info")
    fun readUserSettingInfo(): Call<UserSettingInfoRes>

    @Multipart
    @PATCH("/users/me/setting-info")
    fun updateUserInfo(
        @Part file: MultipartBody.Part?,
        @Part("updateProfileRequestDto") updateProfileRequestDto: RequestBody
    ) : Call<UpdateUserSettingInfoRes>

    @Multipart
    @PATCH("/users/me/setting-profile")
    fun updateUserProfile(
        @Part("updateProfileRequestDto") updateProfileRequestDto: RequestBody
    ) : Call<UpdateUserSettingInfoRes>

    @DELETE("/users/me")
    fun withdraw() : Call<WithdrawResponse>
}