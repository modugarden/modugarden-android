package com.example.modugarden.main.upload.curation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.route.NavigationGraphUploadCuration
import com.example.modugarden.viewmodel.UploadCurationViewModel

class UploadCurationActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uploadCurationViewModel: UploadCurationViewModel by viewModels()
        setContent {
            UploadCurationNavScreen(
                uploadCurationViewModel
            )
        }
    }
}

@Composable
fun UploadCurationNavScreen(
    uploadCurationViewModel: UploadCurationViewModel
) {
    val navController = rememberNavController()
    NavigationGraphUploadCuration(
        navController = navController,
        uploadCurationViewModel = uploadCurationViewModel
    )
}