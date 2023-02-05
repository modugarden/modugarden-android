package com.example.modugarden.api.api

import com.example.modugarden.api.dto.ReportCommentResponse
import com.example.modugarden.api.dto.ReportCurationResponse
import com.example.modugarden.api.dto.ReportPostResponse
import com.example.modugarden.api.dto.ReportUserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReportAPI {
    @POST("/reports/boards/{boardId}")
    fun reportPost(
        @Path("boardId") boardId: Int,
        @Query("type") type:String
    ) : Call<ReportPostResponse>

    @POST("/reports/curations/{curationId}")
    fun reportCuration(
        @Path("curationId") curationId: Int,
        @Query("type") type:String
    ) : Call<ReportCurationResponse>

    @POST("/reports/users/{userId}")
    fun reportUser(
        @Path("userId") userId: Int
    ) : Call<ReportUserResponse>

    @POST("/reports/comments/{commentId}")
    fun reportComment(
        @Path("commentId") commentId: Int,
        @Query("type") type:String
    ) : Call<ReportCommentResponse>
}