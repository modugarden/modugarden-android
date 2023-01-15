package com.example.modugarden.viewmodel

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.modugarden.data.Category
import com.example.modugarden.data.UploadCuration

class UploadCurationViewModel: ViewModel() {
    private var inTitle = mutableStateOf("")
    private var inCategory = mutableStateOf(Category.GARDENING)
    private var inUri = mutableStateOf("")
    private var inImage = mutableStateOf(listOf<Uri>())

    fun saveTitle(title: String) {
        inTitle.value = title
    }
    fun saveCategory(category: Category) {
        inCategory.value = category
    }
    fun saveUri(uri: String) {
        inUri.value = uri
    }
    fun saveImage(image: List<Uri>) {
        inImage.value = image
    }
    fun getAllData(): UploadCuration {
        return UploadCuration(inTitle.value, inCategory.value, inImage.value, inUri.value)
    }
}