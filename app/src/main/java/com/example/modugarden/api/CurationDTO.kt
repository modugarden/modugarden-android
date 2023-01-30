package com.example.modugarden.api

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