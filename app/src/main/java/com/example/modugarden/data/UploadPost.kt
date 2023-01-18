package com.example.modugarden.data

import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateList

data class UploadPost(
    val title: String,
    val category: Category,
    val image: List<Uri>,
    val location: List<String>,
    val description: SnapshotStateList<String>
)
