package com.example.modugarden.data

import android.net.Uri
import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class FollowCuration(
    val writer : User,
    val title: String,
    val thumbnail_image: Uri,
    val category: List<String>,
    val time: String,
    val url : String,
    /*val likesCount : Int, // 좋아요 개수
    val likesList: @RawValue MutableList<User>? // 좋아요 누른 유저들,*/
    ) :Parcelable{
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    }
