package com.example.modugarden.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

object TokenInterceptor: Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request();

        val finalRequest = request.newBuilder()
            .addHeader("authorization", "Bearer ${TokenStore.accessToken}")
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