package com.example.modugarden.api.dto

// likeCuration 리스폰스 바디
data class CurationLikeResponse(
    val code: Int,
    val isSuccess: Boolean,
    val messege :String,
    val result : CurationLikeResult
)
//likeCuration result
data class CurationLikeResult(
    val id: Int,
    val likeNum: Int
)
data class CurationStoreResponse(
    val code: Int,
    val isSuccess: Boolean,
    val messege :String,
    val result : CurationStoreResult
)

data class CurationStoreResult(
    val curationId: Int,
    val userId: Int
)

data class GetFeedCuration(
    val content: List<GetFeedCurationContent>? = null,
    val first: Boolean? = null,
    val hasNext: Boolean? = null,
    val last: Boolean? = null
)
data class GetFeedCurationContent(
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
    val curationId: CurationId
)

data class CurationId(
    val id: Int
)