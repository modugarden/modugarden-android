package com.example.modugarden.data

import android.net.Uri
import android.os.Parcelable
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
@Parcelize
data class FollowPost(
    val writer : User, // 글 작성자
    val title: String,
    val category: List<String>,
    val time : String = "",
    val image: List<Uri>,
    val location : @RawValue SnapshotStateList<String>?,
    val description: String,
    /*val commentData :  @RawValue CommentDataBase, // 댓글 데이터 배이스,*/
    val likesCount : Int, // 좋아요 개수
    val likesList:  MutableList<User>? // 좋아요 누른 유저들,
):Parcelable
