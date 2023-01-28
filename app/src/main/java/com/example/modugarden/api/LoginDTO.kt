package com.example.modugarden.api

data class LoginDTO(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: Result
)
data class Result(
    val accessToken: String,
    val refreshToken: String
)