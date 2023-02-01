package com.example.modugarden.api.dto

class PostDTO {

    data class GetSearchPost(
        val content: List<GetSearchCurationPost> ?= null,
        val first: Boolean? = null,
        val hasNext: Boolean? = null,
        val last: Boolean? = null
    )

    data class GetSearchCurationPost(
        val id: Int,
        val title: String,
        val link: String,
        val preview_img: String,
        val likeNum: Int,
        val created_Date: String,
        val user_id: Int,
        val user_nickname: String,
        val user_profile_image: String,
        val category_category: String
    )
}