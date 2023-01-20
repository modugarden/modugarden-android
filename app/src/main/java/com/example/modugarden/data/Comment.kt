package com.example.modugarden.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Comment(
    var userId: String, // 댓글 작성자
    var description: String,
    var time: Int, // 1시간 전, 1일 전, 1주 전, 1년

    var texting : MutableState<Boolean>  = mutableStateOf(false),
    var isButtonReplyClicked: MutableState<Boolean>  = mutableStateOf(false), // 답글 버튼
    var isButtonCloseReplyClicked: MutableState<Boolean> = mutableStateOf(false)// 답글 모드 닫기
)
