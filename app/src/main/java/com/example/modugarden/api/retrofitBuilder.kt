package com.example.modugarden.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object retrofitBuilder {
    val api: UploadPostTagLocationSearchAPI

    val gson = GsonBuilder().setLenient().create()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(UploadPostTagLocationSearchAPI::class.java)
    }
}