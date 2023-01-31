package com.example.modugarden.api.dto


data class CommentDTO(
    val code: Int,
    val isSuccess: Boolean,
    val messege :String,
    val result : Int //commentId
    )

data class GetCommentsResult(
    val content : Int, //commentId
    val first : Boolean,
    val hasNext : Boolean,
    val last : Boolean
    )
