package com.example.modugarden

import android.app.Application
import android.content.SharedPreferences
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.TokenInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApplicationClass : Application() {
    private val baseUrl = "http://3.38.50.190:8080"

    val gson: Gson = GsonBuilder().setLenient().create()

    companion object {
        lateinit var sharedPreferences: SharedPreferences

        val accessToken = "Access Token"
        val refreshToken = "Refresh Token"
        val clientId = "Client ID"

        lateinit var retrofit: Retrofit
        lateinit var retrofitWithNoInterceptor: Retrofit
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences =
            applicationContext.getSharedPreferences("ModuPrefs", MODE_PRIVATE)
        // 레트로핏 인스턴스 생성
        initRetrofitInstance()
    }

    private fun initRetrofitInstance() {
        val client = OkHttpClient
            .Builder()
            .addNetworkInterceptor(TokenInterceptor)

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client.build())
            .build()

        retrofitWithNoInterceptor = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}