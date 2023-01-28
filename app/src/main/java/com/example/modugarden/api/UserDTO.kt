package com.example.modugarden.api

data class FindByNicknameRes(
    val content: FindByNicknameResContent,
    val first: Boolean,
    val hasNext: Boolean,
    val last: Boolean
)

data class FindByNicknameResContent(
    val category: List<String>,
    val follow: Boolean,
    val nickname: String,
    val profileImage: String,
    val userId: Int
)

data class TestReq1(
    val email: String
)

data class TestRes1(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: TestRes1Result
)

data class TestRes1Result(
    val duplicate: Boolean
)