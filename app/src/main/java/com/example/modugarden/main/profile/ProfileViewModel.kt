package com.example.modugarden.main.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modugarden.api.dto.GetUserCurationsResponseContent
import com.example.modugarden.api.dto.PostDTO.GetUserPostResponseContent
import com.example.modugarden.api.dto.UserInfoResResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {

    private val profileRepository = ProfileRepository.getInstance()

    var profileInfo = mutableStateOf(UserInfoResResult())
    var profilePosts = mutableStateOf(listOf<GetUserPostResponseContent>() ?: null)
    var profileCurations = mutableStateOf(listOf<GetUserCurationsResponseContent>() ?: null)

    fun getProfileInfo(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        profileInfo.value = profileRepository.getProfileInfo(id)
    }

    fun getProfilePosts(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        profilePosts.value = profileRepository.getProfilePosts(id)
    }

    fun getProfileCurations(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        profileCurations.value = profileRepository.getProfileCurations(id)
    }
}