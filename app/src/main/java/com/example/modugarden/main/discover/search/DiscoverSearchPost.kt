package com.example.modugarden.main.discover.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.modugarden.data.PostCard
import com.example.modugarden.ui.theme.ShowProgressBar
import kotlinx.coroutines.delay


//탐색피드에서 탭 래이아웃 아래에 보여줄 검색결과 뜨는 Composable인데
//생각해보면 Post랑 Curation이랑 똑같이 생겨서 보내줄 인수만 바꿔줘도 되지 않을까???
@Composable
fun DiscoverSearchPost(postCards: List<PostCard>){
    val context = LocalContext.current
    val endProgress = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1000)
        endProgress.value = false
    }
    if (endProgress.value) ShowProgressBar()
    else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp)
        ) {
            itemsIndexed(postCards) { idx, item ->
                DiscoverSearchPostCard(item)
            }
        }
    }

}

