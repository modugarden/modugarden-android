package com.example.modugarden.api.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*object PostGoogleMapPhotoAPIRetrofitBuilder {
    val api: PostGoogleMapPhotoAPI

    val gson = GsonBuilder().setLenient().create()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(PostGoogleMapPhotoAPI::class.java)
    }
}*/
