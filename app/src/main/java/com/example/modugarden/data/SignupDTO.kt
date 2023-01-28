package com.example.modugarden.data

data class SignupDTO(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: SignupResult
)
data class SignupResult(
    val userId: Int
)