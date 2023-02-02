package com.example.modugarden.route

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.modugarden.data.FollowPost
import com.example.modugarden.main.content.PostContentCommentScreen
import com.example.modugarden.main.content.PostContentLocationScreen
import com.example.modugarden.main.content.PostContentMapScreen
import com.example.modugarden.main.content.PostContentScreen
import com.example.modugarden.main.profile.MyProfileScreen
import com.example.modugarden.viewmodel.CommentViewModel
import com.example.modugarden.viewmodel.UserViewModel

//PostContentScreen, PostContentCommentScreen, PostContentMapScreen
enum class NAV_ROUTE_POSTCONTENT(val routeName: String, val description: String) {
    MAIN("MAIN", "게시물"),
    COMMENT("COMMENT", "댓글 창"),
    LOCATION("LOCATION", "위치 태그 창"),
    MAP("MAP", "지도 창"),
    WRITER("WRITER","작성자 프로필")
}
@Composable
fun NavigationGraphPostContent(navController: NavHostController,
                               commentViewModel: CommentViewModel= viewModel(),
                               userViewModel: UserViewModel = viewModel(),
                               data:FollowPost) {
    NavHost(navController = navController, startDestination = NAV_ROUTE_POSTCONTENT.MAIN.routeName) {
        composable(NAV_ROUTE_POSTCONTENT.MAIN.routeName) { PostContentScreen(navController,data, userViewModel) }
        composable("${ NAV_ROUTE_POSTCONTENT.COMMENT.routeName }/{comment_data}"
            , arguments = listOf(navArgument(name="comment_data"){type= NavType.IntType})) {
                backStackEntry ->
            PostContentCommentScreen(navController,commentViewModel,
                backStackEntry.arguments!!.getInt("comment_data"),true) }
        composable(NAV_ROUTE_POSTCONTENT.WRITER.routeName){ MyProfileScreen(userViewModel.getUserId())}
       composable("${NAV_ROUTE_POSTCONTENT.LOCATION.routeName}/{location_data}",
           arguments = listOf(navArgument(name="location_data"){type=NavType.StringType}))
       {backStackEntry ->
           PostContentLocationScreen(navController) }
        composable("${NAV_ROUTE_POSTCONTENT.MAP.routeName}/{map_data}",
            arguments = listOf(navArgument(name="map_data"){type=NavType.StringType}))
        {backStackEntry ->PostContentMapScreen(navController,backStackEntry.arguments!!.getString("map_data")!! ) }
    }
}