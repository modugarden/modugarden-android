package com.example.modugarden.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupEmailAuthenticationAPI {
    @POST("users/sign-up/email/authentication")
    fun signupEmailAuthentication(
        @Body jsonData: JsonObject
    ): Call<SignupEmailAuthenticationDTO>
}