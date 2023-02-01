package com.example.modugarden.api.api

import com.example.modugarden.api.dto.LoginDTO
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface LoginAPI {
    //일반 로그인
    @POST("/users/log-in")
    fun login(
        @Body json: JsonObject
    ): Call<LoginDTO>

    //소셜 로그인
    @POST("users/log-in/social")
    fun loginSocialAPI(
        @Body jsonData: JsonObject
    ): Call<LoginDTO>
}