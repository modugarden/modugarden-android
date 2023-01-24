package com.example.modugarden.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.ViewModel
import com.example.modugarden.data.Comment
import com.example.modugarden.data.CommentDataBase

class CommentViewModel : ViewModel() {
    fun addComment(comment: Comment,commentList: MutableState<MutableList<Comment>>,commentDataBase: CommentDataBase){
        commentList.value.add(comment)
        commentDataBase.commentDao().insert(comment)
    }

    fun removeComment(comment: Comment,commentList: MutableList<Comment>){
        commentList.remove(comment)
    }
}

