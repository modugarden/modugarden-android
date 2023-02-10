package com.example.modugarden.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SettingViewModel : ViewModel() {

    private val nicknameState = mutableStateOf("")
    private val birthState = mutableStateOf("")
    private val emailState = mutableStateOf("")
    private val categoriesState = mutableStateOf<List<String>>(listOf())
    private val imageState = mutableStateOf<Uri?>(null)

    fun setImage(image: Uri?) {
        imageState.value = image
    }

    fun getNickname() : String {
        return nicknameState.value
    }

    fun getBirth() : String {
        return birthState.value
    }

    fun getEmail() : String {
        return emailState.value
    }

    fun getCategories() : List<String> {
        return categoriesState.value
    }

    fun getImage() : Uri? {
        return imageState.value
    }

    fun setSettingInfo(
        nickname: String,
        birth: String,
        email: String,
        categories: List<String>,
        image: Uri?
    ) {
        nicknameState.value = nickname
        birthState.value = birth
        emailState.value = email
        categoriesState.value = categories
        imageState.value = image
    }
}