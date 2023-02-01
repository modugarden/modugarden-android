package com.example.modugarden.api.dto

// likeCuration 리스폰스 바디
data class CurationLikeResponse(
    val code: Int,
    val isSuccess: Boolean,
    val messege :String,
    val result : CurationLikeResult
)
//likeCuration result
data class CurationLikeResult(
    val id: Int,
    val likeNum: Int
)
data class CurationStoreResponse(
    val code: Int,
    val isSuccess: Boolean,
    val messege :String,
    val result : CurationStoreResult
)

data class CurationStoreResult(
    val curationId: Int,
    val userId: Int
)

data class GetCuration(
    val content: List<GetCurationContent>? = null,
    val first: Boolean? = null,
    val hasNext: Boolean? = null,
    val last: Boolean? = null
)
data class GetCurationContent(
    val id: Int,
    val title: String,
    val link: String,
    val preview_image: String,
    val likeNum: Int,
    val created_Date: String,
    val user_id: Int,
    val user_nickname: String,
    val user_profile_image: String,
    val category_category: String
)

//createCuration request바디
data class CreateCurationRequest(
    val link: String,
    val title: String,
    val category: String
)

//createCuration 리스폰스 바디
data class CurationUploadResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val curationId: CurationId
)

data class CurationId(
    val id: Int
)
data class GetCurationResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message :String,
    val result : List<GetCurationContent>
)

data class  DeleteCuartionResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val curationId: CurationId
)
data class GetCurationLikeStateResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message :String,
    val result : CurationLikeState
)
data class CurationLikeState(
    val check : Boolean,
    val curation_id : Long,
    val user_id: Long
)

data class GetCurationStoreStateResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message :String,
    val result : GetCurationStoreState
)

data class GetCurationStoreState(
    val check : Boolean,
    val curation_id :Long,
    val user_id : Long
)

