package com.example.modugarden.api.dto

data class GetBlockedListResponse(
    val content: List<GetBlockedListResponseContent> = listOf(),
    val first: Boolean=false,
    val hasNext: Boolean=false,
    val last: Boolean=false
)

data class GetBlockedListResponseContent(
    val categories: List<String>,
    val id: Int,
    val nickname: String,
    val profileImage: String?
)

data class BlockUserResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: BlockUserResponseResult
)

data class BlockUserResponseResult(
    val userId: Int,
    val blockUserId: Int
)

data class UnBlockUserResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UnBlockUserResponseResult
)

data class UnBlockUserResponseResult(
    val unBlockUserId: Int,
    val userId: Int
)