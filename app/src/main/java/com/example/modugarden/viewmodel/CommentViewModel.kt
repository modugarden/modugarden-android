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

    fun removeComment(comment: Comment,commentList: MutableState<MutableList<Comment>>,board_id: Int){
        commentList.value.remove(comment)

    }

}

/*
class CommentViewModelFactory(private val board_id: Int) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CommentViewModel::class.java)) {
            return CommentViewModel(board_id) as T
        }
        throw IllegalArgumentException("unknown viewmodel")
    }

}
*/

