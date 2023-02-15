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
    private val baseUrlGoogleMap = "https://maps.googleapis.com/maps/api/"
    private val baseUrlFcmSend = "https://fcm.googleapis.com/"

    val gson: Gson = GsonBuilder().setLenient().create()

    companion object {
        lateinit var sharedPreferences: SharedPreferences

        val accessToken = "Access Token"
        val refreshToken = "Refresh Token"
        val fcmToken = "fcmToken"

        val clientId = "Client ID"
        val profileImage = "Profile Image"
        val clientNickname = "Client Nickname"
        val clientAuthority = "Client Authority"
        val categorySetting = "Client Category"

        val commentNotification = "Comment Notification Setting"
        val commentChildNotification = "Comment Child Notification Setting"
        val followNotification = "Follow Notification Setting"
        val serviceNotification = "Service Notification Setting"
        val marketingNotification = "Marketing Notification Setting"

        val autoLoginSetting = "Auto Login Setting"
        val autoLoginId = "Auto Login ID"
        val autoLoginPw = "Auto Login Password"
        val autoLoginOption = "Auto Login Option"
        val normalLogin = "Option : Normal"
        val googleLogin = "Option : Google"

        val refresh = "refresh"

        lateinit var retrofit: Retrofit
        lateinit var retrofitWithNoInterceptor: Retrofit
        lateinit var retrofitGoogleMap: Retrofit
        lateinit var retrofitFcmSend: Retrofit
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
            .addInterceptor(TokenInterceptor)

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client.build())
            .build()

        retrofitWithNoInterceptor = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        retrofitGoogleMap = Retrofit.Builder()
            .baseUrl(baseUrlGoogleMap)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        retrofitFcmSend = Retrofit.Builder()
            .baseUrl(baseUrlFcmSend)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}