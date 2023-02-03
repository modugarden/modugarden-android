package com.example.modugarden.api.dto

data class CommentDTO(
    val code: Int,
    val isSuccess: Boolean,
    val message :String,
    val result : CommentId //commentId
    )

data class CommentId(
    val commentId: Int
)

data class GetCommentResponse(
    val content : GetCommentContent, //commentId
    val first : Boolean,
    val hasNext : Boolean,
    val last : Boolean
    )
data class GetCommentContent(
    val comment: String,
    val commentId : Int,
    val nickname : String,
    val parentId : Int?,
    val profileImage : String,
    val userId : Int
)