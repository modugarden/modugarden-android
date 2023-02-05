package com.example.modugarden.api.api

import com.example.modugarden.api.dto.FcmCheckDTO
import com.example.modugarden.api.dto.FcmSaveDTO
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FcmSaveAPI {
    @POST("fcm")
    fun fcmSaveAPI(
        @Body jsonData: JsonObject
    ): Call<FcmSaveDTO>
}

interface FcmCheckAPI {
    @GET("fcm")
    fun fcmCheckAPI(): Call<FcmCheckDTO>
}