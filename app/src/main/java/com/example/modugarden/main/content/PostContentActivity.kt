package com.example.modugarden.main.content

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.api.dto.PostDTO
import com.example.modugarden.data.FollowPost
import com.example.modugarden.route.NavigationGraphPostContent
import com.example.modugarden.viewmodel.CommentViewModel

class PostContentActivity: ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                val board_id = intent.getIntExtra("board_id",0)
                val run = intent.getBooleanExtra("run",true)
                PostContentNavScreen(board_id,run)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostContentNavScreen(board_id:Int, run :Boolean) {
    val navController = rememberNavController()
    val commentViewModel :CommentViewModel = viewModel()
    if (run) NavigationGraphPostContent(navController, board_id = board_id)
    else PostContentCommentScreen(navController = navController,boardId = board_id, commentViewModel = commentViewModel, run =run)
}