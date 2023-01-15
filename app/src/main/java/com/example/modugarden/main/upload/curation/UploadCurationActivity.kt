package com.example.modugarden.main.upload.curation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.route.NavigationGraphUpload

class UploadCurationActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extra = intent.extras
        val title = extra!!["title"] as String
        val category = extra!!["category"] as String
        setContent {
            UploadCurationNavScreen(title, category)
        }
    }
}

@Composable
fun UploadCurationNavScreen(
    title: String,
    category: String,
) {
    val navController = rememberNavController()
    NavigationGraphUpload(
        navController = navController,
        title = title,
        category = category,
    )
}