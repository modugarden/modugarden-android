package com.example.modugarden.api.dto

import com.google.gson.annotations.SerializedName

data class CommentDTO(
    val code: Int,
    val isSuccess: Boolean,
    val message :String,
    val result : GetCommentContent
    )


data class GetCommentResponse(
    val content : List<GetCommentContent>? = null,
    val first : Boolean? = null,
    val hasNext : Boolean? = null,
    val last : Boolean? = null
    )
data class GetCommentContent(
    var comment: String,
    val commentId : Int,
    val nickname : String,
    val parentId : Int?,
    val profileImage : String?,
    val userId : Int,
    val localDateTime: String
)

data class SendCommentBody(
    @SerializedName("content") val content: String,
    @SerializedName("parentId")val parentId: Int?
)