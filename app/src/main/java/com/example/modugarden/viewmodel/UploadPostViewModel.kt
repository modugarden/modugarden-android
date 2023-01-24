package com.example.modugarden.viewmodel

import android.net.Uri
import android.util.Log
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
    private var inLocation = mutableStateListOf<String>()
    private var inDescription = mutableStateListOf<String>()
    private var nextState = mutableStateOf(0)
    private var inPage = mutableStateOf(0)
    private var prevPage = mutableStateOf(0) //초기화 전의 페이지 수 저장.

    fun saveTitle(title: String) {
        inTitle.value = title
    }
    fun saveCategory(category: Category) {
        inCategory.value = category
    }
    fun addImage(image: Uri) {
        inImage.add(image)
        inDescription.add("")
        inLocation.add("")
    }
    fun removeImage(index: Int) {
        inImage.removeAt(index)
        inDescription.removeAt(index)
        inLocation.removeAt(index)
    }
    fun removeRangeImage(lastIndex: Int) {
        inImage.removeRange(0, lastIndex)
        inDescription.removeRange(0, lastIndex)
        inLocation.removeRange(0, lastIndex)
    }
    fun addLocation(location: String, index: Int) {
        inLocation[index] = location
    }
    fun removeOnlyLocation(index: Int) {
        inLocation[index] = ""
    }
    fun addDescription(description: String, index: Int) {
        inDescription[index] = description
    }
    fun getAllData(): UploadPost {
        return UploadPost(inTitle.value, inCategory.value, inImage, inLocation, inDescription)
    }
}