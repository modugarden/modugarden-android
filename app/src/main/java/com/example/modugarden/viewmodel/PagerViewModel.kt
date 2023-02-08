package com.example.modugarden.viewmodel

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
class PagerViewModel :ViewModel() {

    val pagerState = PagerState()

    init {
        viewModelScope.launch {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                // Page is the index of the page being swiped.
            }
        }
    }
}
