package com.example.modugarden.api.dto

data class GetBlockedListResponse(
    val content: List<GetBlockedListResponseContent>,
    val first: Boolean,
    val hasNext: Boolean,
    val last: Boolean
)

data class GetBlockedListResponseContent(
    val categories: List<String>,
    val id: Int,
    val nickname: String,
    val profileImage: String
)

data class BlockUserResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<BlockUserResponseResult>
)

data class BlockUserResponseResult(
    val blockUserId: Int,
    val userId: Int
)

data class UnBlockUserResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<UnBlockUserResponseResult>
)

data class UnBlockUserResponseResult(
    val unBlockUserId: Int,
    val userId: Int
)