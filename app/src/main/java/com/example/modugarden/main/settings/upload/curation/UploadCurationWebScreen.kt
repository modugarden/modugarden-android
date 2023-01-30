package com.example.modugarden.main.settings.upload.curation

import android.app.Activity
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UploadCurationViewModel
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@Composable
fun UploadCurationWebScreen(
    navController: NavHostController,
    uploadCurationViewModel: UploadCurationViewModel,
    url: String
) {
    val mContext = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            TopBar(
                title = "큐레이션 미리보기",
                titleIcon = R.drawable.ic_arrow_left_bold,
                titleIconOnClick = {
                    navController.popBackStack()
                },
                titleIconSize = 20.dp,
                bottomLine = true
            )
            AndroidView(factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams (
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    loadUrl(url)
                }
            }, update = {
                it.loadUrl(url)
            })
        }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomButton(title = "다음", onClick = {
                scope.launch {
                    snackbarHostState.showSnackbar("큐레이션을 게시했어요", duration = SnackbarDuration.Short)
                }
                (mContext as Activity).finish()
            })
        }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter).padding(30.dp)
        ) {
            SnackBar(snackbarHostState = snackbarHostState)
        }
    }
}