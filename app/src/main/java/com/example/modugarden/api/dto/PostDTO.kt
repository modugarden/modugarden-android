package com.example.modugarden.api.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
    data class GetPostResponse(
        val code: Int ?= null,
        val isSuccess: Boolean ?= null,
        val message :String ?= null,
        val result : GetPostContent  ?= null
    )
    data class GetPostContent(
        val id: Int,
        val title: String,
        val image : List<PostImageContent>,
        val like_num: Int,
        val created_Date: String,
        val user_id: Int,
        val user_nickname: String,
        val user_profile_image: String,
        val isFollowed:Boolean,
        val category_category: String
    )
    data class PostImageContent(
        val content:String,
        val id: Int,
        val image : String,
        val location : String,
        val userId:Int
    )

    data class DeletePostResponse(
        val code: Int,
        val isSuccess: Boolean,
        val message :String,
        val result : PostId
    )
    data class PostId(
        val id: Int
    )

    data class PostLikeResponse(
        val code: Int,
        val isSuccess: Boolean,
        val message :String,
        val result : PostLikeResult
    )
    data class PostLikeResult(
        val id: Int,
        val like_num: Int
    )
    data class PostStoreResponse(
        val code: Int,
        val isSuccess: Boolean,
        val message :String,
        val result : PostStoreResult
    )

    data class PostStoreResult(
        val curation_id: Int,
        val user_id: Int
    )
    data class GetPostLikeStateResponse(
        val code: Int,
        val isSuccess: Boolean,
        val message :String,
        val result : PostLikeState
    )
    data class PostLikeState(
        val check : Boolean,
        val board_id : Int,
        val user_id: Int
    )
    data class GetPostSaveStateResponse(
        val code: Int,
        val isSuccess: Boolean,
        val message :String,
        val result : PostSaveState
    )
    data class PostSaveState(
        val check : Boolean,
        val board_id : Int,
        val user_id: Int
    )


    data class GetPost(
        val content: List<GetPostContent> ?= null,
        val first: Boolean ?= null,
        val hasNext: Boolean? = null,
        val last: Boolean? = null
    )

    data class GetFollowFeedPost(
        var content: List<GetFollowFeedPostContent> = listOf(),
        val first: Boolean = false,
        val hasNext: Boolean = false,
        val last: Boolean = false
    )

    data class GetFollowFeedPostContent(
        val board_id: Int=0,
        val title: String="",
        val image : ArrayList<PostFollowFeedImageContent> = arrayListOf(),
        val liked : Boolean=false,
        val saved : Boolean=false,
        val created_Date: String="",
        val user_id: Int=0,
        val user_nickname: String="",
        val user_profile_image: String?="",
        val category_category: String=""
    )

    data class PostFollowFeedImageContent(
        val content:String,
        val id: Int,
        val image : String,
        val location : String,
        val userid:Int
    )

    data class GetUserPostResponse(
        val content: List<GetUserPostResponseContent>,
        val first: Boolean,
        val hasNext: Boolean,
        val last: Boolean
    )

    data class GetUserPostResponseContent(
        val id: Int,
        val image: String
    )

    data class GetStoredPostResponse(
        val content: List<GetStoredPostResponseContent>,
        val first: Boolean,
        val hasNext: Boolean,
        val last: Boolean
    )

    data class GetStoredPostResponseContent(
        val board_id: Int = 0,
        val category_category: String = "",
        val like_num: Int = 0,
        val preview_img: String = "",
        val title: String = "",
        val user_id: Int = 0,
        val user_nickname: String = "",
        val user_profile_image: String = ""
    )
}