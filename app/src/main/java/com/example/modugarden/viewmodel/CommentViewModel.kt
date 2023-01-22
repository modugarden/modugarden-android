package com.example.modugarden.viewmodel

import androidx.lifecycle.ViewModel
import com.example.modugarden.data.Comment

class CommentViewModel : ViewModel() {
    fun addComment(comment: Comment,commentList: MutableList<Comment>){
        commentList.add(comment)
    }
    fun addReply(reply: Comment, commentList: MutableList<Comment>){
        val comment = commentList.find { it.id==reply.parentId }
        if (comment != null) {
            comment.reply.add(reply)
        }
    }
    fun removeComment(comment: Comment,commentList: MutableList<Comment>){
        commentList.remove(comment)
    }
}

