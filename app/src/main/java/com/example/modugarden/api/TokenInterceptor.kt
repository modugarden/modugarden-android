package com.example.modugarden.api

import android.util.Log
import com.example.modugarden.ApplicationClass.Companion.accessToken
import com.example.modugarden.ApplicationClass.Companion.refreshToken
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import com.example.modugarden.api.dto.GetNewTokenRequest
import com.example.modugarden.api.dto.GetNewTokenResponse
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback

object TokenInterceptor: Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {

        val ACCESS_TOKEN = sharedPreferences.getString(accessToken, "").toString()
        val REFRESH_TOKEN = sharedPreferences.getString(refreshToken, "").toString()

        val request = chain.request()

        val finalRequest = request.newBuilder()
            .addHeader("authorization", "Bearer $ACCESS_TOKEN")
            .build()

        val response = chain.proceed(finalRequest)

        when (response.code) {
            400 -> {
                //Show Bad Request Error Message
            }
            401 -> {
                Log.d("TokenIssue", "액세스 토큰 만료")
                val newTokenResponse = RetrofitBuilder.loginAPI
                    .getNewToken(GetNewTokenRequest(ACCESS_TOKEN, REFRESH_TOKEN))
                    .execute()

                if(newTokenResponse.body() != null) {
                    val tokenEditor = sharedPreferences.edit()
                    val newAccessToken = newTokenResponse.body()?.result?.accessToken
                    val newRefreshToken = newTokenResponse.body()?.result?.refreshToken
                    tokenEditor.putString(accessToken, newAccessToken)
                    tokenEditor.putString(refreshToken, newRefreshToken)
                    tokenEditor.apply()
                    Log.d("onResponse", "토큰 재발급 성공!")
                    Log.d("onResponse", "${newTokenResponse.code()}\n" +
                            "${newTokenResponse.body()!!.result}")

                    val newTokenRequest = chain.request().newBuilder()
                        .addHeader("authorization", "Bearer $newAccessToken")
                        .build()

                    response.close()
                    return chain.proceed(newTokenRequest)
                }
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