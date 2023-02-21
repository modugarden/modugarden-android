package com.example.modugarden.main.profile.follow

import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.modugarden.api.dto.FollowListDtoResContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object FollowService {

    fun getFollowingList(id: Int, list: MutableState<List<FollowListDtoResContent>>) {
        CoroutineScope(Dispatchers.IO).launch {
            list.value = FollowRepository.getFollowingList(id)
        }
    }

    fun getFollowerList(id: Int, list: MutableState<List<FollowListDtoResContent>>) {
        CoroutineScope(Dispatchers.IO).launch {
            list.value = FollowRepository.getFollowerList(id)
        }
    }
}