package com.example.modugarden.route

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.modugarden.main.discover.search.DiscoverSearchScreen
import com.example.modugarden.main.discover.search.DiscoverSearchingScreen
import com.example.modugarden.main.upload.post.UploadPostImageEditScreen
import com.example.modugarden.main.upload.post.UploadPostImageListScreen
import com.example.modugarden.main.upload.post.UploadPostInfoScreen
import com.example.modugarden.viewmodel.UploadPostViewModel


enum class NAV_ROUTE_DISCOVER_SEARCH(val routeName: String, val description: String) { //upload 패키지 루트.
    DISCOVERMAIN("DISCVOER_SEARCH_MAIN", "탐색창 메인 창"),
    DISCOVERSEARCHING("DISCOVER_SEARCHING", "탐색 결과 창"),
}
@Composable
fun NavigationGraphDiscoverSearch(
    navController: NavHostController
) {

    NavHost(navController, startDestination = NAV_ROUTE_DISCOVER_SEARCH.DISCOVERMAIN.routeName,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(
            NAV_ROUTE_DISCOVER_SEARCH.DISCOVERMAIN.routeName
        ) {
            DiscoverSearchScreen(navController)
        }
        composable(
            NAV_ROUTE_DISCOVER_SEARCH.DISCOVERSEARCHING.routeName + "/{searchText}",
        ) { backStackEntry ->
            val searchText = backStackEntry.arguments?.getString("searchText") ?: ""
            DiscoverSearchingScreen(navController, searchText)
        }
    }
}