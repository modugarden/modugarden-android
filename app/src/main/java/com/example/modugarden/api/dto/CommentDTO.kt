package com.example.modugarden.api.dto

import com.google.gson.annotations.SerializedName

data class CommentDTO(
    val code: Int,
    val isSuccess: Boolean,
    val message :String,
    val result : GetCommentContent
    )


data class GetCommentResponse(
    val content : List<GetCommentContent> = listOf(),
    val first : Boolean? = null,
    val hasNext : Boolean? = null,
    val last : Boolean? = null
    )
data class GetCommentContent(
    val block:Boolean = false,
    val isblocked:Boolean = false,
    var comment: String = "",
    val commentId : Int=0,
    val nickname : String="",
    val parentId : Int?=null,
    val profileImage : String?=null,
    val userId : Int=0,
    val localDateTime: String=""
)

data class DeleteCommentResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message :String,
    val result : CommentId
)

data class CommentId(
    val commentId:Int
)