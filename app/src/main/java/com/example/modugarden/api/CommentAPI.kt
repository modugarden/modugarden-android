package com.example.modugarden.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface CommentAPI {
    @POST("/boards/{board_id}/comments")
    fun sendComment(
        @Body boardId : Int ,
        @Body content : String,
        @Body parentId : Int
    ): Call<CommentDTO>

/*    @GET("/boards/{board_id}/comments")
    fun getComments(
        @Body boardId : Int
    ) : Call<GetCommentsResult>*/

    @PATCH("/boards/{board_id}/comments/{comment_id}")
    fun editComment(
        @Body commentId : Int
    ): Call <CommentDTO>

}