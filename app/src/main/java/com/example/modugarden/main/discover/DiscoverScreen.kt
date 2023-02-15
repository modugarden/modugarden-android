package com.example.modugarden.main.discover

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.route.NavigationGraphDiscoverSearch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiscoverScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        //탐색 메인화면 부름
        DiscoverSearchNavScreen()
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiscoverSearchNavScreen() {
    val navController = rememberNavController()
    NavigationGraphDiscoverSearch(navController = navController)
}