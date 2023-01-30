package com.example.modugarden.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupNicknameIsDuplicatedAPI {
    @POST("users/nickname/isDuplicated")
    fun signupNicknameIsDuplicatedAPI(
        @Body jsonBody: JsonObject
    ): Call<SignupNicknameIsDuplicatedDTO>
}