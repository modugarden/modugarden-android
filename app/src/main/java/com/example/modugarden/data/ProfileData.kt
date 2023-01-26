package com.example.modugarden.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class User(
    val image: Uri,
    val name: String,
    val category: List<String>,
    val follower: Int,
    val following: Int,
    val state: Boolean,
    val post: @RawValue List<PostCard>?,
    val curation: @RawValue List<CurationCard>?
) : Parcelable

@Parcelize
data class PostCard(
    val image: Int = 0,
    val title: String = "Default Title",
    val category: String = "Default Category",
    val time: String = "No time",
    val user: String = "Default UserName"
) : Parcelable
@Parcelize
data class CurationCard(
    val thumbnail_image: Int,
    val title: String,
    val category: String,
    val time: String,
    val user: String
) : Parcelable
