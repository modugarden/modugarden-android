package com.example.modugarden.api.dto

data class FcmSaveDTO(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: FcmSaveResult
)
data class FcmSaveResult(
    val fcmToken: String,
    val userId: Long
)

data class FcmCheckDTO(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: FcmCheckResult,
)
data class FcmCheckResult(
    val fcmTokens: List<String>
)