package com.example.modugarden.viewmodel

import android.net.Uri
import androidx.compose.runtime.MutableState
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
    private var inDescription = mutableStateListOf<String>()
    private var inPage = mutableStateOf(0)

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
    fun saveAllDescription(description: String, index: Int) {
        inDescription[index] = description
    }
    fun saveDescription(description: String, index: Int) {
        inDescription[index] = description
    }
    fun removeDescription(index: Int) {
        inDescription.removeAt(index)
    }
    fun savePage(page: Int) {
        for(i in 0..page) {
            inDescription.add("")
        }
    }
    fun getAllData(): UploadPost {
        return UploadPost(inTitle.value, inCategory.value, inImage, inLocation.value, inDescription)
    }
}