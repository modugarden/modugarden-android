package com.example.modugarden.api.api

import android.media.Image
import com.example.modugarden.BuildConfig.google_maps_key
import com.example.modugarden.data.MapsDetailRes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PostLocationPhotoAPI {
    @GET("place/details/json")
    fun getPhotoReference(
        @Query("place_id") place_id:String,
        @Query("key") key: String = google_maps_key
    ): Call<MapsDetailRes>

    @GET("place/photo")
    fun getPhotos(
        @Query("photo_reference") photo_reference: String,
        @Query("key") key: String = google_maps_key,
        @Query("maxwidth") maxwidth:Int = 400
    ):Call<Image>
}

