package com.example.modugarden.data

import android.net.Uri

data class UploadCuration(
    val title: String,
    val category: Category,
    val image: List<Uri>,
    val uri: String,
)