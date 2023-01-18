package com.example.modugarden.main.upload.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.modugarden.data.UploadPost
import com.example.modugarden.viewmodel.UploadPostViewModel

@Composable
fun UploadPostTagScreen(
    navController: NavHostController,
    uploadPostViewModel: UploadPostViewModel,
    data: UploadPost,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            for(i in data.description) {
                Text(i)
            }
        }
    }
}