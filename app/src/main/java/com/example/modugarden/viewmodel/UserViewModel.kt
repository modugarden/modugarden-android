package com.example.modugarden.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.modugarden.ApplicationClass.Companion.clientId
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences

class UserViewModel : ViewModel() {
    private val userState = mutableStateOf(sharedPreferences.getInt(clientId, 0))
    val userId = userState.value

    fun setUserId(newId: Int) {
        userState.value = newId
    }
}