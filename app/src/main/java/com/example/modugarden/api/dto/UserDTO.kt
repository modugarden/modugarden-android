package com.example.modugarden.api.dto

enum class UserAuthority(title: String) {
    ROLE_BLOCKED("ROLE_BLOCKED"),
    ROLE_CURATOR("ROLE_CURATOR"),
    ROLE_GENERAL("ROLE_GENERAL")
}

// 닉네임으로 찾기
data class FindByNicknameRes(
    val content: List<FindByNicknameResContent> ?= null,
    val first: Boolean ?= null,
    val hasNext: Boolean ?= null,
    val last: Boolean ?= null
)
data class FindByNicknameResContent(
    val block: Boolean,
    val blocked: Boolean,
    val categories: List<String>,
    val follow: Boolean,
    val nickname: String,
    val profileImage: String,
    val userId: Int
)
// 닉네임으로 찾기 끝

// 아이디로 유저 정보 가져오기
data class UserInfoRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UserInfoResResult
)

data class UserInfoResResult(
    val block: Boolean = false,
    val blocked: Boolean = false,
    val categories: List<String> = listOf(),
    val follow: Boolean = false,
    val followerCount: Int = 0,
    val id: Int = 0,
    val nickname: String = "",
    val postCount: Int = 0,
    val profileImage: String? = "",
    val userAuthority: String = UserAuthority.ROLE_GENERAL.name
)
// 아이디로 유저 정보 가져오기 끝

// 유저 카테고리 업데이트
data class UpdateUserCategoryReq(
    val categories: List<String>
)

data class UpdateUserCategoryRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UpdateUserCategoryResResult
)

data class UpdateUserCategoryResResult(
    val categories: List<String>,
    val id: Int
)
// 유저 카테고리 업데이트 끝

// 내 정보 불러오기
data class MyUserInfoRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UserInfoResResult
)

data class MyUserInfoResult(
    val categories: List<String>,
    val follow: Boolean,
    val followerCount: Int,
    val id: Int,
    val nickname: String,
    val postCount: Int,
    val profileImage: String,
    val userAuthority: String
)
// 내 정보 불러오기 끝

// 닉네임 변경
data class UpdateUserNicknameReq(
    val nickname: String
)

data class UpdateUserNicknameRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UpdateUserNicknameResResult
)

data class UpdateUserNicknameResResult(
    val nickname: String
)
// 닉네임 변경

// 유저 정보 변경
data class UpdateUserSettingInfoRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UpdateUserSettingInfoResResult
)

data class UpdateUserSettingInfoResResult(
    val categories: List<String>,
    val id: Int,
    val nickname: String,
    val profileImage: String
)
// 유저 정보 변경 끝

// 유저 설정 불러오기
data class UserSettingInfoRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UserSettingInfoResResult
)

data class UserSettingInfoResResult(
    val birth: String,
    val categories: List<String>,
    val email: String,
    val id: Int,
    val nickname: String,
    val profileImage: String,
    val userAuthority: String
)
// 유저 설정 불러오기 끝

// 닉네임 중복 조회
data class NicknameDuplicatedCheckReq(
    val nickname: String
)

data class NicknameDuplicatedCheckRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: NicknameDuplicatedCheckResResult
)

data class NicknameDuplicatedCheckResResult(
    val isDuplicated: Boolean,
    val nickname: String
)
// 닉네임 중복 조회 끝

// 토큰 재발급
data class GetNewTokenRequest(
    val accessToken: String,
    val refreshToken: String
)

data class GetNewTokenResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: GetNewTokenResponseResult
)

data class GetNewTokenResponseResult(
    val accessToken: String,
    val accessToken_expiredDate: String,
    val refreshToken: String,
    val userId: Int
)
// 토큰 재발급 끝

// 회원 탈퇴
data class WithdrawResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: WithdrawResponseResult
)

data class WithdrawResponseResult(
    val userId: String
)
// 회원 탈퇴 끝