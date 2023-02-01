package com.example.modugarden.api

import com.example.modugarden.api.api.*
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    val userAPI: UserAPI
    val loginAPI: LoginAPI
    val signupAPI: SignupAPI
    val followAPI: FollowAPI
    val curationAPI: CurationAPI
    val fcmSaveAPI: FcmSaveAPI
    val postCreateAPI: PostCreateAPI

    val gson = GsonBuilder().setLenient().create()

    init {
        val client = OkHttpClient
            .Builder()
            .addNetworkInterceptor(TokenInterceptor)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.38.50.190:8080")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client.build())
            .build()

        val retrofitWithNoInterceptor = Retrofit.Builder()
            .baseUrl("http://3.38.50.190:8080")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        userAPI = retrofit.create(UserAPI::class.java)
        followAPI = retrofit.create(FollowAPI::class.java)
        loginAPI = retrofitWithNoInterceptor.create(LoginAPI::class.java)
        signupAPI = retrofitWithNoInterceptor.create(SignupAPI::class.java)
        fcmSaveAPI = retrofit.create(FcmSaveAPI::class.java)
        postCreateAPI = retrofit.create(PostCreateAPI::class.java)
        curationAPI = retrofit.create(CurationAPI::class.java)
    }
}