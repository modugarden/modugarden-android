package com.example.modugarden.main.content

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.data.FollowPost
import com.example.modugarden.route.NavigationGraphPostContent

class PostContentActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val data = intent.getParcelableExtra<FollowPost>("post_data")
            PostContentNavScreen(data!!)
        }
    }
}

@Composable
fun PostContentNavScreen(data:FollowPost) {
    val navController = rememberNavController()
    NavigationGraphPostContent(navController, data = data)
}