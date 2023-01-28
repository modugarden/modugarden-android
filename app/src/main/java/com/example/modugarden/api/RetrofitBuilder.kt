package com.example.modugarden.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    val userAPI: UserAPI
    val loginAPI: LoginAPI
    val signupAPI: SignupAPI
    val signupEmailAuthenticationAPI: SignupEmailAuthenticationAPI
    val signupEmailIsDuplicatedAPI: SignupEmailIsDuplicatedAPI

    val gson = GsonBuilder().setLenient().create()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.38.50.190:8080")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        userAPI = retrofit.create(UserAPI::class.java)
        loginAPI = retrofit.create(LoginAPI::class.java)
        signupAPI = retrofit.create(SignupAPI::class.java)
        signupEmailAuthenticationAPI = retrofit.create(SignupEmailAuthenticationAPI::class.java)
        signupEmailIsDuplicatedAPI = retrofit.create(SignupEmailIsDuplicatedAPI::class.java)
    }
}