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
import com.example.modugarden.main.upload.curation.UploadCurationImageInfoScreen
import com.example.modugarden.main.upload.curation.UploadCurationInfoScreen
import com.example.modugarden.main.upload.curation.UploadCurationUploadSuccessfully
import com.example.modugarden.main.upload.curation.UploadCurationWebScreen
import com.example.modugarden.viewmodel.UploadCurationViewModel

enum class NAV_ROUTE_UPLOAD_CURATION(val routeName: String, val description: String) { //upload 패키지 루트.
    INFO("UPLOAD_CURATION_INFO", "큐레이션 업로드 제목, 카테고리 입력창"),
    IMAGEINFO("UPLOAD_CURATION_IMAGEINFO", "큐레이션 업로드 정보 입력창"),
    WEB("UPLOAD_CURATION_WEB", "큐레이션 업로드 미리보기 창"),
    UPLOADSUCCESSFULLY("UPLOAD_CURATION_UPLOADSUCCESSFULLY", "큐레이션 업로드 성공")

}
@Composable
fun NavigationGraphUploadCuration(
    navController: NavHostController,
    uploadCurationViewModel: UploadCurationViewModel = viewModel()
) {
    val data = uploadCurationViewModel.getAllData()

    NavHost(navController, startDestination = NAV_ROUTE_UPLOAD_CURATION.INFO.routeName,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(
            NAV_ROUTE_UPLOAD_CURATION.INFO.routeName
        ) {
            UploadCurationInfoScreen(
                navController = navController,
                uploadCurationViewModel = uploadCurationViewModel,
                data = data
            )
        }
        composable(
            NAV_ROUTE_UPLOAD_CURATION.IMAGEINFO.routeName
        ) {
            UploadCurationImageInfoScreen(
                navController,
                uploadCurationViewModel,
                data
            )
        }
        composable(
            NAV_ROUTE_UPLOAD_CURATION.WEB.routeName+"/{url}",
            arguments = listOf(
                navArgument("url") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            UploadCurationWebScreen(navController = navController, uploadCurationViewModel, url = url)
        }
        composable(
            NAV_ROUTE_UPLOAD_CURATION.UPLOADSUCCESSFULLY.routeName
        ) {
            UploadCurationUploadSuccessfully(navController)
        }
    }
}

