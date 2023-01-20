package com.example.modugarden.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

data class Comment(
    var userID: String, // 댓글 작성자
    var description: String,
    var time: Int, // 1시간 전, 1일 전, 1주 전, 1년
    var isButtonReplyClicked: MutableState<Boolean>  = mutableStateOf(false), // 답글 버튼
    var isTexting :MutableState<Boolean>  = mutableStateOf(false)
)
