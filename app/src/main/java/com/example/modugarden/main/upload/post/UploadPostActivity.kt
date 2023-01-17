package com.example.modugarden.main.upload.post

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.route.NavigationGraphUploadPost

class UploadPostActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UploadPostNavScreen()
        }
    }
}

@Composable
fun UploadPostNavScreen() {
    val navController = rememberNavController()
    NavigationGraphUploadPost(navController = navController)
}