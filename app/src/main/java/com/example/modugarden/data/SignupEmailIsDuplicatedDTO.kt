package com.example.modugarden.data

data class SignupEmailIsDuplicatedDTO(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: SignupEmailIsDuplicatedResult
)
data class SignupEmailIsDuplicatedResult(
    val duplicate: Boolean
)
