package com.example.modugarden.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.modugarden.data.Comment

class CommentViewModel : ViewModel() {
    private var commentList = mutableStateListOf<Comment>()

    fun addComment(comment: Comment){
        commentList.add(comment)
    }
    fun removeComment(comment: Comment){
        commentList.remove(comment)
    }
    fun getAllComment(): List<Comment> {
        return commentList
    }
}

