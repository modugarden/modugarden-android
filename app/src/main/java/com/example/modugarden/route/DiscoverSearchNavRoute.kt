package com.example.modugarden.route

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.modugarden.main.discover.search.DiscoverSearchResultScreen
import com.example.modugarden.main.discover.search.DiscoverSearchScreen
import com.example.modugarden.main.discover.search.DiscoverSearchingScreen
import com.example.modugarden.main.profile.ProfileScreen
import com.example.modugarden.viewmodel.UserViewModel

enum class NAV_ROUTE_DISCOVER_SEARCH(val routeName: String, val description: String) { //upload 패키지 루트.
    DISCOVERMAIN("DISCVOER_SEARCH_MAIN", "탐색창 메인 창"),
    DISCOVERSEARCHING("DISCOVER_SEARCHING", "탐색 검색 창"),
    DISCOVERSEARCHRESULT("DISCOVER_SEARCH_RESULT", "탐색 결과 창"),
    DISCOVERSEARCHUSERPROFILE("DISCOVER_SEARCH_USER_PROFILE", "탐색 유저 프로필")
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun NavigationGraphDiscoverSearch(
    navController: NavHostController,
    userViewModel: UserViewModel = viewModel()
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
            NAV_ROUTE_DISCOVER_SEARCH.DISCOVERSEARCHING.routeName,
        ) { backStackEntry ->
            DiscoverSearchingScreen(navController)
        }
        composable(
                NAV_ROUTE_DISCOVER_SEARCH.DISCOVERSEARCHRESULT.routeName + "/{searchText}",
        ) { backStackEntry ->
            val searchText = backStackEntry.arguments?.getString("searchText") ?: ""
            DiscoverSearchResultScreen(navController, searchText, userViewModel)
        }
        composable(
            NAV_ROUTE_DISCOVER_SEARCH.DISCOVERSEARCHUSERPROFILE.routeName
        ) {
            ProfileScreen(userId = userViewModel.getUserId())
        }
    }
}