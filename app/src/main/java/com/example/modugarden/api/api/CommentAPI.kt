package com.example.modugarden.api.api

import com.example.modugarden.api.dto.CommentDTO
import com.example.modugarden.api.dto.GetCommentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentAPI {
    @POST("/boards/{board_id}/comments")
    fun sendComment(
        @Path ("board_id") board_id : Int,
        @Body content : String,
        @Body parentId : Int
    ): Call<CommentDTO>

   @GET("/boards/{board_id}/comments")
    fun getComments(
        @Body boardId : Int
    ) : Call<GetCommentResponse>

    @PATCH("/boards/{board_id}/comments/{comment_id}")
    fun editComment(
        @Body commentId : Int
    ): Call <CommentDTO>

}