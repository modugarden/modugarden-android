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
    val fcmTokens: List<String> = listOf(),
    val follow: Boolean = false,
    val nickname: String = "",
    val profileImage: String? = "",
    val userId: Int = 0
)

data class FollowRecommendationRes(
    val content: List<FollowRecommendationResContent> = listOf(),
    val first: Boolean = false,
    val hasNext: Boolean =false,
    val last: Boolean=false
)
data class FollowRecommendationResContent(
    val categories: List<String> = listOf(),
    val isFollow: Boolean = false,
    val nickname: String = "",
    val profileImage: String = "",
    val userId: Int = 0
)


//{
//    "content": [
//    {
//        "categories": [
//        "string"
//        ],
//        "follow": true,
//        "nickname": "string",
//        "profileImage": "string",
//        "userId": 0
//    }
//    ],
//    "first": true,
//    "hasNext": true,
//    "last": true
//}
// 유저 팔로잉/팔로워 목록 끝