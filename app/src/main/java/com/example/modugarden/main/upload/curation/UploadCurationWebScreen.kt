package com.example.modugarden.main.upload.curation

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.modugarden.route.NAV_ROUTE_UPLOAD
import com.example.modugarden.ui.theme.TopBar
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduPoint
import com.example.modugarden.R

@Composable
fun UploadCurationWebScreen(navController: NavHostController, url: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            TopBar(
                title = "큐레이션 미리보기",
                titleIcon = R.drawable.ic_arrow_left,
                titleIconOnClick = {
                    navController.popBackStack()
                },
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
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White.copy(alpha = 0f), Color.White),
                        startY = 0f,
                        endY = 50f
                    )
                )
        ) {
            Card(
                modifier = Modifier
                    .bounceClick {

                    }
                    .padding(18.dp)
                    .fillMaxWidth()
                    .alpha(1f),
                shape = RoundedCornerShape(10.dp),
                backgroundColor = moduPoint,
                elevation = 0.dp
            ) {
                Text(
                    "다음",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(18.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}