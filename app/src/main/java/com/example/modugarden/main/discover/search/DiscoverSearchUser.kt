package com.example.modugarden.main.discover.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope


@Composable
fun DiscoverSearchUser(searchStr : String, coroutineScope: CoroutineScope, snackbarHostState: SnackbarHostState){

    LazyColumn(modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp)
    ) {
        items(
            count = 15,
            itemContent = { DiscoverSearchUserCard(searchStr, coroutineScope, snackbarHostState) }
        )
    }
}