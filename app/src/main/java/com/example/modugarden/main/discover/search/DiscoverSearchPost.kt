package com.example.modugarden.main.discover.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


//탐색피드에서 탭 래이아웃 아래에 보여줄 검색결과 뜨는 Composable인데
//생각해보면 Post랑 Curation이랑 똑같이 생겨서 보내줄 인수만 바꿔줘도 되지 않을까???
@Composable
fun DiscoverSearchPost(searchStr: String){
    val context = LocalContext.current

    LazyColumn(modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp)) {
        items(
            count = 8,
            itemContent = { DiscoverSearchCard(searchStr) }
        )
    }
}