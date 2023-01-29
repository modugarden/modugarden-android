package com.example.modugarden.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface UserAPI {
    @GET("/users")
    fun getUserByNickname(
        @Query("nickname") nickname: String
    ): Call<FindByNicknameRes>
}