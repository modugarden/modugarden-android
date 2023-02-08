package com.example.modugarden.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.modugarden.ApplicationClass.Companion.clientId
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import java.util.*
import kotlin.collections.ArrayList

class UserViewModel : ViewModel() {
    private val userState = mutableStateOf(sharedPreferences.getInt(clientId, 0))
    private val nextUserState = mutableStateOf(0)
    private val onBNB = mutableStateOf(false)

    fun setUserId(newId: Int) {
        userState.value = newId
    }

    fun setNextUserId(nextId: Int) {
        nextUserState.value = nextId
    }

    fun setOnBNB(state: Boolean) {
        onBNB.value = state
    }

    fun getUserId() : Int {
        return userState.value
    }

    fun getNextUserId() : Int {
        return nextUserState.value
    }

    fun getOnBNB() : Boolean {
        return onBNB.value
    }
}