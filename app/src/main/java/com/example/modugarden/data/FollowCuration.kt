package com.example.modugarden.data

import android.net.Uri
import android.os.Parcelable
import androidx.room.PrimaryKey
import com.example.modugarden.main.content.modalReportCuration
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class FollowCuration(
    val writer : User,
    val title: String,
    val thumbnail_image: Uri,
    val category: List<String>,
    val time: String,
    val url : String
    ) :Parcelable