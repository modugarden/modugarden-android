package com.example.modugarden.api

import com.example.modugarden.ApplicationClass.Companion.accessToken
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

object TokenInterceptor: Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {

        val ACCESS_TOKEN = sharedPreferences.getString(accessToken, null)

        val finalRequest = chain.request().newBuilder()
            .addHeader("authorization", "Bearer $ACCESS_TOKEN")
            .build()

        val response = chain.proceed(finalRequest)

        when (response.code) {
            400 -> {
                //Show Bad Request Error Message
            }
            401 -> {
                // 리프레쉬로 바꿔서 다시 보냄 -> 따라서 401이 뜬 경우엔 리프레쉬도 만료된 경우
            }

            403 -> {
                //Show Forbidden Message
            }

            404 -> {
                //Show NotFound Message
            }

            // ... and so on
        }

        return response
    }
}