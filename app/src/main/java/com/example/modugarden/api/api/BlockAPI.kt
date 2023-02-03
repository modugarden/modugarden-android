package com.example.modugarden.api.api

import com.example.modugarden.api.dto.BlockUserResponse
import com.example.modugarden.api.dto.GetBlockedListResponse
import com.example.modugarden.api.dto.UnBlockUserResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BlockAPI {
    @GET("/blocked-list")
    fun getBlockedList() : Call<GetBlockedListResponse>

    @POST("/blocked-list/{userId}")
    fun blockUser(
        @Path("userId") userId: Int
    ) : Call<BlockUserResponse>

    @DELETE("/blocked-list/{userId}")
    fun unBlockUser(
        @Path("userId") userId: Int
    ) : Call<UnBlockUserResponse>
}