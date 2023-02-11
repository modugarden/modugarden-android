package com.example.modugarden.api.api

import com.example.modugarden.api.dto.CommentDTO
import com.example.modugarden.api.dto.DeleteCommentResponse
import com.example.modugarden.api.dto.GetCommentResponse
import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentAPI {
    @POST("/boards/{board_id}/comments")
    fun sendComment(
        @Path ("board_id") board_id : Int,
        @Body json: JsonObject
    ): Call<CommentDTO>

   @GET("/boards/{board_id}/comments")
    fun getComments(
       @Path ("board_id") board_id : Int
    ) : Call<GetCommentResponse>

    @HTTP(method = "DELETE", path = "/boards/{board_id}/comments",hasBody = true)
    fun deleteComment(
        @Path ("board_id") board_id : Int,
        @Body commentId: Int
    ): Call <DeleteCommentResponse>

}