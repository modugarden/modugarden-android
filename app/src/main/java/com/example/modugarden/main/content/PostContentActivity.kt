package com.example.modugarden.main.content

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.data.FollowPost
import com.example.modugarden.route.NavigationGraphPostContent
import com.example.modugarden.viewmodel.CommentViewModel

class PostContentActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                val data = intent.getParcelableExtra<FollowPost>("post_data")
                val run = intent.getBooleanExtra("run",true)
                PostContentNavScreen(data!!,run)
        }
    }
}

@Composable
fun PostContentNavScreen(data:FollowPost, run :Boolean) {
    val navController = rememberNavController()
    val commentViewModel : CommentViewModel = viewModel()
    if (run) NavigationGraphPostContent(navController, data = data)
    else PostContentCommentScreen(navController = navController, commentViewModel = commentViewModel, boardId = data.boardId, run)
}