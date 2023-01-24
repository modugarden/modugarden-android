package com.example.modugarden.main.profile

import android.net.Uri

data class User(
    val image: Uri,
    val name: String,
    val category: List<String>,
    val follower: Int,
    val following: Int,
    val state: Boolean,
    val post: List<PostCard>,
    val curation: List<CurationCard>
)

data class PostCard(
    val image: Int = 0,
    val title: String = "Default Title",
    val category: String = "Default Category",
    val time: String = "No time",
    val user: String = "Default UserName"
)

data class CurationCard(
    val thumbnail_image: Int,
    val title: String,
    val category: String,
    val time: String,
    val user: String
)
