package com.example.modugarden.api

import com.example.modugarden.data.SignupDTO
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupAPI {
    @POST("users/sign-up")
    fun signup(
        @Body jsonBody: JsonObject
    ): Call<SignupDTO>
}