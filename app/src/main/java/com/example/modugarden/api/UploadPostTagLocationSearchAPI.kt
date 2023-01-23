package com.example.modugarden.api

import com.example.modugarden.BuildConfig.google_maps_key
import com.example.modugarden.data.MapsGeocoding
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UploadPostTagLocationSearchAPI {
    @GET("geocode/json")
    fun getUploadPostTagLocationSearchAPI(
        @Query("address") address: String,
        @Query("key") key: String = google_maps_key,
        @Query("language") language: String = "korean"
    ): Call<MapsGeocoding>
}