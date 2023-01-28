package com.example.modugarden.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface LoginAPI {
    @POST("/users/log-in")
    fun login(
        @Body json: JsonObject
    ): Call<LoginDTO>
}