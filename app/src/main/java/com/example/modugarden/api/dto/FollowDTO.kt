package com.example.modugarden.api.dto

// 팔로우/언팔로우
data class FollowDtoRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: FollowDtoResResult
)

data class FollowDtoResResult(
    val follow: Boolean
)
// 팔로우/언팔로우 끝

// 유저 팔로잉/팔로워 목록
data class FollowListDtoRes(
    val content: List<FollowListDtoResContent>,
    val first: Boolean,
    val hasNext: Boolean,
    val last: Boolean
)

data class FollowListDtoResContent(
    val categories: List<String> = listOf(),
    val follow: Boolean = false,
    val nickname: String = "",
    val profileImg: String = "",
    val userId: Int = 0
)