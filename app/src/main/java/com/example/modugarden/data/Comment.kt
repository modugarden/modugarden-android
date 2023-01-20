package com.example.modugarden.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Comment(
    var userID: String, // 댓글 작성자
    var description: String,
    var time: Int, // 1시간 전, 1일 전, 1주 전, 1년
    var isReplying: MutableState<Boolean>  = mutableStateOf(false)
)
