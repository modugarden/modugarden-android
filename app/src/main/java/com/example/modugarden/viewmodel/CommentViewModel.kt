package com.example.modugarden.viewmodel

import androidx.compose.ui.res.colorResource
import androidx.lifecycle.ViewModel
import com.example.modugarden.data.Comment

class CommentViewModel : ViewModel() {
    fun addComment(comment: Comment,commentList: MutableList<Comment>){
        commentList.add(comment)
    }

    fun removeComment(comment: Comment,commentList: MutableList<Comment>){
        commentList.remove(comment)
    }
}

