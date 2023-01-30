package com.example.modugarden.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FollowCuration(
    val user : User,
    val title: String,
    val previewImage: Uri,
    val category: List<String>,
    val time: String,
    val link : String,
    val liKeNum: Int
    ) :Parcelable