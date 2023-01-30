package com.example.modugarden.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface SignupEmailIsDuplicatedAPI {
    @POST("users/sign-up/email/isDuplicated")
    fun getSignupEmailIsDuplicatedAPI(
        @Body json: JsonObject
    ): Call<SignupEmailIsDuplicatedDTO>
}