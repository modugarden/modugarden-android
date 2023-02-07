package com.example.modugarden.main.content

import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.modugarden.viewmodel.UserViewModel

class PostContentActivity: ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val extras = intent.extras
            if(extras != null) {
                val board_id = extras.getInt("board_id",0)
                val run = extras.getBoolean("run",true)
                Log.d("post-result", " = $board_id")
                PostContentNavScreen(board_id,run)
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostContentNavScreen(board_id:Int, run :Boolean) {
    val navController = rememberNavController()
    val commentViewModel :CommentViewModel = viewModel()
    val userViewModel:UserViewModel = viewModel()
    if (run) NavigationGraphPostContent(navController, board_id = board_id)
    else PostContentCommentScreen(
        navController = navController,
        boardId = board_id,
        commentViewModel = commentViewModel,
        run =run,
        userViewModel = userViewModel)
    Log.i("run/board_id",run.toString()+board_id.toString())
}