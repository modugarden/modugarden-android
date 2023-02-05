package com.example.modugarden.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.CommentDTO
import com.example.modugarden.api.dto.GetCommentContent
import com.example.modugarden.api.dto.GetCommentResponse
import com.example.modugarden.data.Comment
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentViewModel() : ViewModel() {


    fun addComment(comment : GetCommentContent, commentList : SnapshotStateList<GetCommentContent>,
    ){
        commentList.add(comment)
    }

    fun deleteComment(commentId:Int, commentList : SnapshotStateList<GetCommentContent>,
    ){
        val deleteComment= commentList.filter { it.commentId==commentId || it.parentId==commentId}
        commentList.removeAll(deleteComment)
    }

}


