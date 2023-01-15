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
import com.example.modugarden.viewmodel.UploadCurationViewModel

enum class NAV_ROUTE_UPLOAD(val routeName: String, val description: String) { //upload 패키지 루트.
    CURATION_INFO("UPLOAD_CURATION_INFO", "큐레이션 업로드 정보 입력창"),
    CURATION_WEB("UPLOAD_CURATION_WEB", "큐레이션 업로드 미리보기 창")
}
@Composable
fun NavigationGraphUpload(
    navController: NavHostController,
    title: String,
    category: String,
    uploadCurationViewModel: UploadCurationViewModel = viewModel()
) {
    NavHost(navController, startDestination = NAV_ROUTE_UPLOAD.CURATION_INFO.routeName,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(
            NAV_ROUTE_UPLOAD.CURATION_INFO.routeName
        ) {
            val data = uploadCurationViewModel.getAllData()
            UploadCurationInfoScreen(
                navController,
                title,
                category,
                uploadCurationViewModel,
                data
            )
        }
        composable(
            NAV_ROUTE_UPLOAD.CURATION_WEB.routeName+"/{url}",
            arguments = listOf(
                navArgument("url") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            UploadCurationWebScreen(navController = navController, url = url)
        }
    }
}

