package com.example.modugarden.api

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CurationCreateAPI {
    @Multipart
    @POST("curations")
    fun curationCreate(
        @Part("curationCreateRequest") CreateCurationRequest: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<CurationUploadResponse>
}