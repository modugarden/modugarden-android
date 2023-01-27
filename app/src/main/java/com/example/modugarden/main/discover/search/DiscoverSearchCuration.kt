package com.example.modugarden.main.discover.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.modugarden.data.CurationCard


@Composable
fun DiscoverSearchCuration(curationCards: List<CurationCard>){

    LazyColumn(modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp)
    ) {
        itemsIndexed(curationCards) { idx, item ->
            DiscoverSearchCurationCard(item)
        }
    }
}