package com.example.modugarden.main.discover.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.ui.theme.moduBlack


@Composable
fun DiscoverSearchNoResultScreen(searchStr : String) {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = searchStr,
                style = TextStyle(color = moduBlack,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(text = "검색 결과를 찾을 수 없어요",
                style = TextStyle(color = moduBlack,
                    fontWeight = FontWeight(400),
                    fontSize = 16.sp)
            )

        }
    }
}