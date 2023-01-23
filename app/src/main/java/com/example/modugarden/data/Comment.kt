package com.example.modugarden.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.util.UUID

data class Comment(
    var id: UUID? = UUID.randomUUID(), //댓글 id
    val userID: String? = null, // 댓글 작성자
    var description: String? = null, //댓글 내용
    var time: Int? = null, //  댓글 작성 및 수정 일시
    var isReplying: MutableState<Boolean>  = mutableStateOf(false), //답글 작성중인지 // true면 작성중
    val parentId:UUID?, // 답글을 달 댓글의 id
    val mode: Boolean = false// 댓글인지 답글인지 // true면 답글, false면 댓글
    // /*var userProfile,*/
)
