package com.example.modugarden.main.settings.upload.post

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.route.NavigationGraphUploadPost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class UploadPostActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UploadPostNavScreen()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UploadPostNavScreen() {
    val navController = rememberAnimatedNavController()
    NavigationGraphUploadPost(navController = navController)
}