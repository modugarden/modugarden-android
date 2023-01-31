package com.example.modugarden.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    val userAPI: UserAPI
    val loginAPI: LoginAPI
    val signupAPI: SignupAPI
    val signupEmailAuthenticationAPI: SignupEmailAuthenticationAPI
    val signupEmailIsDuplicatedAPI: SignupEmailIsDuplicatedAPI
    val curationCreateAPI: CurationCreateAPI
    val followAPI: FollowAPI
    val curationAPI: CurationAPI

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
        curationCreateAPI = retrofit.create(CurationCreateAPI::class.java)
        followAPI = retrofit.create(FollowAPI::class.java)
        loginAPI = retrofitWithNoInterceptor.create(LoginAPI::class.java)
        signupAPI = retrofitWithNoInterceptor.create(SignupAPI::class.java)
        signupEmailAuthenticationAPI = retrofitWithNoInterceptor.create(SignupEmailAuthenticationAPI::class.java)
        signupEmailIsDuplicatedAPI = retrofitWithNoInterceptor.create(SignupEmailIsDuplicatedAPI::class.java)
        curationAPI = retrofit.create(CurationAPI::class.java)
    }
}