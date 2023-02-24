package com.example.modugarden.main.profile.follow

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modugarden.api.dto.FollowListDtoResContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FollowViewModel: ViewModel() {

    private val followRepository = FollowRepository.getInstance()

    var followingList = mutableStateListOf<FollowListDtoResContent>()
    private set

    var followerList = mutableStateListOf<FollowListDtoResContent>()
    private set

    fun getFollowingList(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        followRepository.getFollowingList(id)?.let { list ->
            followingList.clear()
            followingList.addAll(list)
        }
    }

    fun getFollowerList(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        followRepository.getFollowerList(id)?.let { list ->
            followerList.clear()
            followerList.addAll(list)
        }
    }
}