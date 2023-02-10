package com.example.modugarden.api.dto

// likeCuration 리스폰스 바디
data class CurationLikeResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message :String,
    val result : CurationLikeResult
)
//likeCuration result
data class CurationLikeResult(
    val id: Int,
    val like_num: Int
)
data class CurationStoreResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message :String,
    val result : CurationStoreResult
)

data class CurationStoreResult(
    val curation_id: Int,
    val user_id: Int
)

data class GetSearchCuration(
    val content: List<GetSearchCurationContent> ?= null,
    val first: Boolean? = null,
    val hasNext: Boolean? = null,
    val last: Boolean? = null
)
data class GetCuration(
    val content: List<GetCurationContent> ?= null,
    val first: Boolean ?= null,
    val hasNext: Boolean? = null,
    val last: Boolean? = null
)

data class GetSearchCurationContent(
    val id: Int,
    val title: String,
    val link: String,
    val preview_image: String,
    val likeNum: Int,
    val created_Date: String,
    val user_id: Int,
    val user_nickname: String,
    val user_profile_image: String,
    val category_category: String
)

data class GetCurationContent(
    val id: Int,
    val isliked :Boolean,
    val isSaved : Boolean,
    val title: String,
    val link: String,
    val preview_image: String?,
    val like_num: Int,
    val created_Date: String,
    val user_id: Int,
    val user_nickname: String,
    val user_profile_image: String?,
    val category_category: String
) // 게시물 상세보기 - 큐레이션 조회
//createCuration request바디
data class CreateCurationRequest(
    val link: String,
    val title: String,
    val category: String
)

//createCuration 리스폰스 바디
data class CurationUploadResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: CurationId
)

data class CurationId(
    val id: Int
)
data class GetCurationResponse(
    val code: Int? = null,
    val isSuccess: Boolean? = null,
    val message :String? = null,
    val result : GetCurationContent?=null
)

data class  DeleteCurationResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: CurationId
)
data class GetCurationLikeStateResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message :String,
    val result : CurationLikeState
)
data class CurationLikeState(
    val check : Boolean,
    val curation_id : Int,
    val user_id: Int
)

data class GetCurationStoreStateResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message :String,
    val result : GetCurationStoreState
)

data class GetCurationStoreState(
    val check : Boolean,
    val curation_id :Int,
    val user_id : Int
)

data class GetFollowFeedCuration(
    val content: List<GetFollowFeedCurationContent> ?,
    val first: Boolean =false,
    val hasNext: Boolean =false,
    val last: Boolean =false
)

data class GetFollowFeedCurationContent(
    val curation_id: Int,
    val title: String,
    val liked : Boolean,
    val saved : Boolean,
    val image:String,
    val link : String,
    val created_Date: String,
    val user_id: Int,
    val user_nickname: String,
    val user_profile_image: String,
    val category_category: String
)

data class GetStoredCurationsResponse(
    val content: List<GetStoredCurationsResponseContent>,
    val first: Boolean,
    val hasNext: Boolean,
    val last: Boolean
)

data class GetStoredCurationsResponseContent(
    val category_category: String,
    val id: Int,
    val likeNum: Int,
    val link: String,
    val localDateTime: String,
    val preview_image: String,
    val title: String,
    val user_id: Int,
    val user_nickname: String,
    val user_profile_image: String
)

data class GetUserCurationsResponse(
    val content: List<GetUserCurationsResponseContent>,
    val first: Boolean,
    val hasNext: Boolean,
    val last: Boolean
)

data class GetUserCurationsResponseContent(
    val category: String,
    val created_date: String,
    val id: Int,
    val image: String,
    val title: String
)