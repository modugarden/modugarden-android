package com.example.modugarden.main.content

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.route.NavigationGraphPostContent
import com.example.modugarden.viewmodel.CommentViewModel
import com.example.modugarden.viewmodel.UserViewModel

class PostContentActivity: ComponentActivity() {
    var keyboardManager : InputMethodManager?=null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val extras = intent.extras
            var keyboardManager =getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            if(extras != null) {
                val board_id = extras.getInt("board_id",0)
                val run = extras.getBoolean("run",true)
                val fcmTokens = extras.getStringArrayList("fcm_tokens")
                PostContentNavScreen(board_id,run,fcmTokens)
            }

        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostContentNavScreen(board_id: Int, run: Boolean, fcmTokens: ArrayList<String>?) {
    val navController = rememberNavController()
    val commentViewModel :CommentViewModel = viewModel()
    val userViewModel:UserViewModel = viewModel()
    if (run) NavigationGraphPostContent(navController, board_id = board_id)
    else PostContentCommentScreen(
        navController = navController,
        boardId = board_id,
        commentViewModel = commentViewModel,
        run =run,
        userViewModel = userViewModel,
        fcmToken = fcmTokens)
    Log.i("run/board_id",run.toString()+board_id.toString())
}