package com.example.modugarden.api

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
    val content: GetFeedCurationContent,
    val first: Boolean,
    val hasNext: Boolean,
    val last: Boolean
)

data class GetFeedCurationContent(
    val category: List<String>,
    val id: Int,
    val liKeNum: Int,
    val link: String,
    val previewImage: String,
    val title: String,
    val userId: Int,
    val userNickname: String,
    val userProfileImage: String
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