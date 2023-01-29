package com.example.modugarden.api

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CurationCreateAPI {
    @Multipart
    @POST("curations")
    fun createCuration(
        @Part curationCreateRequest: JsonObject,
        @Part imageFile: MultipartBody.Part
    ): Call<CurationUploadResponse>
}