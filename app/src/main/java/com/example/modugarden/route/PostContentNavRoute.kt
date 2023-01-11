package com.example.modugarden.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.modugarden.main.content.PostContentCommentScreen
import com.example.modugarden.main.content.PostContentLocationScreen
import com.example.modugarden.main.content.PostContentMapScreen
import com.example.modugarden.main.content.PostContentScreen

//PostContentScreen, PostContentCommentScreen, PostContentMapScreen
enum class NAV_ROUTE_POSTCONTENT(val routeName: String, val description: String) {
    MAIN("MAIN", "게시물"),
    COMMENT("COMMENT", "댓글 창"),
    LOCATION("LOCATION", "위치 태그 창"),
    MAP("MAP", "지도 창")
}
@Composable
fun NavigationGraphPostContent(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NAV_ROUTE_POSTCONTENT.MAIN.routeName) {
        composable(NAV_ROUTE_POSTCONTENT.MAIN.routeName) { PostContentScreen() }
        composable(NAV_ROUTE_POSTCONTENT.COMMENT.routeName) { PostContentCommentScreen() }
        composable(NAV_ROUTE_POSTCONTENT.LOCATION.routeName) { PostContentLocationScreen() }
        composable(NAV_ROUTE_POSTCONTENT.MAP.routeName) { PostContentMapScreen() }
    }
}