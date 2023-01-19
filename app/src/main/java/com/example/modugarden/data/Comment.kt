package com.example.modugarden.data

data class Comment (
    val userId : String, // 댓글 작성자
    val description : String,
    val time : Int // 1시간 전, 1일 전, 1주 전, 1년 전
)
