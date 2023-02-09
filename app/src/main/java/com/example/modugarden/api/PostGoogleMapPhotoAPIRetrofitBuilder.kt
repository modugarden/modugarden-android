package com.example.modugarden.api
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*object PostGoogleMapPhotoAPIRetrofitBuilder {
    val photoAPI: PlacePhotoAPI

    val gson = GsonBuilder().setLenient().create()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        photoAPI = retrofit.create(PlacePhotoAPI::class.java)
    }
}*/
