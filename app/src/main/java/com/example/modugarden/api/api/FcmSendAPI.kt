package com.example.modugarden.api.api

import com.example.modugarden.BuildConfig.GOOGLE_FCM_KEY
import com.example.modugarden.api.dto.FcmSendDTO
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FcmSendAPI {
    @POST("fcm/send")
    fun fcmSendAPI(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") authorization: String = "key=$GOOGLE_FCM_KEY",
        @Body jsonBody: JsonObject
    ): Call<FcmSendDTO>
}