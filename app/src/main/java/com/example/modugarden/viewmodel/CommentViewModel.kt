package com.example.modugarden.viewmodel

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
    private val inComment = mutableStateOf("")
    private val inCommentId = mutableStateOf(0)
    private val inNickname = mutableStateOf("")
    private val inParentID : MutableState<Int?> = mutableStateOf(null)
    private val inProfileImage : MutableState<String?> = mutableStateOf("")
    private val inLocalDateTime = mutableStateOf("")
    private val inUserId = mutableStateOf(0)

    fun addComment(
        content: String,
        parentId: Int?,
        board_id:Int,
        commentList : SnapshotStateList<GetCommentContent>,
        context: Context
    ){
        val jsonData = JsonObject()
        jsonData.apply {
            addProperty("content", content)
            addProperty("parentId", parentId)
        }
        RetrofitBuilder.commentAPI.sendComment(board_id, jsonData )
            .enqueue(object :Callback<CommentDTO>{
                override fun onResponse(call: Call<CommentDTO>,
                                        response: Response<CommentDTO>) {
                    if(response.isSuccessful){
                        val res =  response.body()
                        if (res != null) {
                            Log.i("댓글 성공", response.body()?.result.toString())
                            Log.i("게시글", board_id.toString())
                            inComment.value = res.result.comment
                            inCommentId.value = res.result.commentId
                            inNickname.value = res.result.nickname
                            inParentID.value = res.result.parentId
                            inProfileImage.value = res.result.profileImage
                            inUserId.value = res.result.userId
                            inLocalDateTime.value = res.result.localDateTime
                        }
                    }
                    else Log.i("댓글","${response.body()}")
                }

                override fun onFailure(call: Call<CommentDTO>, t: Throwable) {
                    Toast.makeText(context, "데이터를 받지 못했어요", Toast.LENGTH_SHORT).show()
                    Log.d("comment-result", t.message.toString())
                }
            })
        val comment = GetCommentContent(inComment.value, inCommentId.value,inNickname.value,inParentID.value,inProfileImage.value,inUserId.value,inLocalDateTime.value)
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

