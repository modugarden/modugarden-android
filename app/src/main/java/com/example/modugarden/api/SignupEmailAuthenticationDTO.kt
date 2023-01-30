package com.example.modugarden.api
data class SignupEmailAuthenticationDTO(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: SignupEmailAuthenticationResult
)
data class SignupEmailAuthenticationResult(
    val authCode: String
)