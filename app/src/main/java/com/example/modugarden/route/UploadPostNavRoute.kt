package com.example.modugarden.route

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.modugarden.main.upload.curation.UploadCurationInfoScreen
import com.example.modugarden.main.upload.curation.UploadCurationWebScreen
import com.example.modugarden.main.upload.post.UploadPostImageEditScreen
import com.example.modugarden.main.upload.post.UploadPostImageListScreen
import com.example.modugarden.main.upload.post.UploadPostInfoScreen
import com.example.modugarden.viewmodel.UploadCurationViewModel
import com.example.modugarden.viewmodel.UploadPostViewModel

enum class NAV_ROUTE_UPLOAD_POST(val routeName: String, val description: String) { //upload 패키지 루트.
    IMAGELIST("UPLOAD_POST_IMAGELIST", "포스트 업로드 사진 리스트 창"),
    IMAGEEDIT("UPLOAD_POST_IMAGEEDIT", "포스트 업로드 사진 편집창"),
    INFO("UPLOAD_POST_INFO", "포스트 업로드 정보 입력창"),
    TAGLOCATION("UPLOAD_POST_TAGLOCATION", "포스트 업로드 위치 태그 추가 창"),
}
@Composable
fun NavigationGraphUploadPost(
    navController: NavHostController,
    uploadPostViewModel: UploadPostViewModel = viewModel()
) {
    val data = uploadPostViewModel.getAllData()
    NavHost(navController, startDestination = NAV_ROUTE_UPLOAD_POST.INFO.routeName,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(
            NAV_ROUTE_UPLOAD_POST.IMAGELIST.routeName
        ) {
            UploadPostImageListScreen(navController = navController, data = data, uploadPostViewModel = uploadPostViewModel)
        }
        composable(
            NAV_ROUTE_UPLOAD_POST.IMAGEEDIT.routeName,
        ) {
            UploadPostImageEditScreen(navController = navController, data = data, uploadPostViewModel = uploadPostViewModel)
        }
        composable(
            NAV_ROUTE_UPLOAD_POST.INFO.routeName,
        ) { UploadPostInfoScreen(navController = navController, uploadPostViewModel = uploadPostViewModel, data = data) }
    }
}