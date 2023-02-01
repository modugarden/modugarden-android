package com.example.modugarden.api

import com.example.modugarden.ApplicationClass.Companion.retrofit
import com.example.modugarden.ApplicationClass.Companion.retrofitWithNoInterceptor
import com.example.modugarden.api.api.*
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

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
    val userAPI: UserAPI = retrofit.create(UserAPI::class.java)
    val followAPI: FollowAPI = retrofit.create(FollowAPI::class.java)
    val curationAPI: CurationAPI = retrofit.create(CurationAPI::class.java)
    val curationCreateAPI: CurationCreateAPI = retrofit.create(CurationCreateAPI::class.java)
    val fcmSaveAPI: FcmSaveAPI = retrofit.create(FcmSaveAPI::class.java)
    val postCreateAPI: PostCreateAPI = retrofit.create(PostCreateAPI::class.java)
    val blockAPI: BlockAPI = retrofit.create(BlockAPI::class.java)
    val postAPI: PostAPI = retrofit.create(PostAPI::class.java)
    val reportAPI: ReportAPI = retrofit.create(ReportAPI::class.java)

    val loginAPI: LoginAPI = retrofitWithNoInterceptor.create(LoginAPI::class.java)
    val signupAPI: SignupAPI = retrofitWithNoInterceptor.create(SignupAPI::class.java)
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