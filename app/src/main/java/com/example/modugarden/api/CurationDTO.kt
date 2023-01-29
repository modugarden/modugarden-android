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

data class CreateCurationRequest(
    val content: CreateCurationContent,
    val link: String,
    val title: String
)

data class CreateCurationContent(
    val category: String,
    val createdDate: String,
    val id: String,
    val modifiedDate: String
)

data class CurationUploadResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val curationId: CurationId
)

data class CurationId(
    val id: Int
)