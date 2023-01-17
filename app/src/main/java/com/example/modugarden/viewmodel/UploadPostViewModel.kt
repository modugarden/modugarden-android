package com.example.modugarden.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.modugarden.data.Category
import com.example.modugarden.data.UploadPost

class UploadPostViewModel: ViewModel() {
    private val inTitle = mutableStateOf("")
    private val inCategory = mutableStateOf(Category.GARDENING)
    private var inImage = mutableStateListOf<Uri>()
    private val inLocation = mutableStateOf(listOf<String>())
    private val inDescription = mutableStateOf(listOf<String>())

    fun saveTitle(title: String) {
        inTitle.value = title
    }
    fun saveCategory(category: Category) {
        inCategory.value = category
    }
    fun addImage(image: Uri) {
        inImage.add(image)
    }
    fun removeImage(index: Int) {
        inImage.removeAt(index)
    }
    fun removeRangeImage(lastIndex: Int) {
        inImage.removeRange(0, lastIndex)
    }
    fun saveLocation(location: List<String>) {
        inLocation.value = location
    }
    fun saveDescription(description: List<String>) {
        inDescription.value = description
    }
    fun getAllData(): UploadPost {
        return UploadPost(inTitle.value, inCategory.value, inImage, inLocation.value, inDescription.value)
    }
}