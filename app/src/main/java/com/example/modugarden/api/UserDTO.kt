package com.example.modugarden.api

data class FindByNicknameRes(
    val content: List<FindByNicknameResContent>,
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