package com.example.modugarden.api


data class SignupNicknameIsDuplicatedDTO(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: SignupNicknameIsDuplicatedResult
)
data class SignupNicknameIsDuplicatedResult(
    val isDuplicated: Boolean,
    val nickname: String
)