package com.example.modugarden.api.api

import com.example.modugarden.BuildConfig.google_maps_key
import com.example.modugarden.data.MapsGeocoding
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UploadPostTagLocationSearchAPI {
    @GET("place/textsearch/json")
    fun getUploadPostTagLocationSearchAPI(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("query") query: String,
        @Query("key") key: String = google_maps_key,
        @Query("language") language: String = "ko"
    ): Call<MapsGeocoding>
}