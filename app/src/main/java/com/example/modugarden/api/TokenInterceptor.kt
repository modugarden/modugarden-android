package com.example.modugarden.api

import com.example.modugarden.ApplicationClass.Companion.accessToken
import com.example.modugarden.ApplicationClass.Companion.refreshToken
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

object TokenInterceptor: Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {

        val ACCESS_TOKEN = sharedPreferences.getString(accessToken, null)
        val REFRESH_TOKEN = sharedPreferences.getString(refreshToken, null)

        val request = chain.request()

        val finalRequest = request.newBuilder()
            .addHeader("authorization", "Bearer $ACCESS_TOKEN")
            .build()

        var response = chain.proceed(finalRequest)

        when (response.code) {
            400 -> {
                //Show Bad Request Error Message
            }
            401 -> {
                val refreshRequest = request.newBuilder()
                    .addHeader("authorization", "Bearer $REFRESH_TOKEN")
                    .build()

                response = chain.proceed(refreshRequest)
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