package com.example.modugarden.api.dto

data class ReportUserResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: ReportUserResponseResult
)

data class ReportUserResponseResult(
    val userId: Int,
    val reportUserId: Int
)

data class ReportPostResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: ReportPostResponseResult
)

data class ReportPostResponseResult(
    val userId: Int,
    val reportBoardId: Int
)

data class ReportCurationResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: ReportCurationResponseResult
)

data class ReportCurationResponseResult(
    val userId: Int,
    val reportCurationId: Int
)

data class ReportCommentResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: ReportCommentResponseResult
)

data class ReportCommentResponseResult(
    val userId: Int,
    val reportCommentId: Int
)
