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

        var response = chain.proceed(finalRequest)

        when (response.code) {
            400 -> {
                //Show Bad Request Error Message
            }
            401 -> {
//                Log.d("TokenIssue", "액세스 토큰 만료")
//                RetrofitBuilder.userAPI
//                    .getNewToken(GetNewTokenRequest(ACCESS_TOKEN, REFRESH_TOKEN))
//                    .enqueue(object : Callback<GetNewTokenResponse>{
//                        override fun onResponse(
//                            call: Call<GetNewTokenResponse>,
//                            res: retrofit2.Response<GetNewTokenResponse>
//                        ) {
//                            if(res.body() != null) {
//                                val tokenEditor = sharedPreferences.edit()
//
//                                val newAccessToken = res.body()?.result?.accessToken
//                                val newRefreshToken = res.body()?.result?.refreshToken
//                                tokenEditor.putString(accessToken, newAccessToken)
//                                tokenEditor.putString(refreshToken, newRefreshToken)
//
//                                tokenEditor.apply()
//
//                                val NEW_ACCESS_TOKEN = sharedPreferences.getString(accessToken, "")
//
//                                val newTokenRequest = request.newBuilder()
//                                    .addHeader("authorization", "Bearer $NEW_ACCESS_TOKEN")
//                                    .build()
//
//                                response = chain.proceed(newTokenRequest)
//                            }
//                        }
//
//                        override fun onFailure(call: Call<GetNewTokenResponse>, t: Throwable) {
//                            Log.d("onFailure", t.stackTraceToString())
//                        }
//                    })
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