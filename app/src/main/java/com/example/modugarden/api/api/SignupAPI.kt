package com.example.modugarden.api.api

import com.example.modugarden.api.dto.SignupDTO
import com.example.modugarden.api.dto.SignupEmailAuthenticationDTO
import com.example.modugarden.api.dto.SignupEmailIsDuplicatedDTO
import com.example.modugarden.api.dto.SignupNicknameIsDuplicatedDTO
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupAPI {
    @POST("users/sign-up")
    fun signup(
        @Body jsonBody: JsonObject
    ): Call<SignupDTO>

    //이메일 중복 체크
    @POST("users/sign-up/email/isDuplicated")
    fun getSignupEmailIsDuplicatedAPI(
        @Body json: JsonObject
    ): Call<SignupEmailIsDuplicatedDTO>

    //이메일 인증
    @POST("users/sign-up/email/authentication")
    fun signupEmailAuthentication(
        @Body jsonData: JsonObject
    ): Call<SignupEmailAuthenticationDTO>

    //닉네임 중복 체크
    @POST("users/nickname/isDuplicated")
    fun signupNicknameIsDuplicatedAPI(
        @Body jsonBody: JsonObject
    ): Call<SignupNicknameIsDuplicatedDTO>
}

