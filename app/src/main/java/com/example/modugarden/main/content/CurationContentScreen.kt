package com.example.modugarden.main.content

import android.app.Activity
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.R
import com.example.modugarden.main.follow.moduBold
import com.example.modugarden.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun CurationContent( url :String) {
    val focusManager = LocalFocusManager.current
    //액티비티 종료할 때 사용할 변수
    val activity = (LocalContext.current as? Activity)
    val isButtonClickedLike = remember { mutableStateOf(false) }
    val isButtonClickedSave = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .addFocusCleaner(focusManager)
    )
    {
        // 상단
        Column(
            modifier = Modifier
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(text = " 큐레이션 제목", style = moduBold, fontSize = 16.sp)
                Spacer(modifier = Modifier.weight(1f))

                CurationHeartCard(
                    curationId = 0,
                    modifier = Modifier,
                    heartState = isButtonClickedLike
                )

                // 스크랩
                Icon(modifier = Modifier
                    .padding(end = 18.dp)
                    .bounceClick {
                        isButtonClickedSave.value = !isButtonClickedSave.value

                        if (isButtonClickedSave.value){
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "게시물을 저장하였습니다.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
                    ,painter = painterResource
                        (id =
                    if (isButtonClickedSave.value)
                        R.drawable.ic_star_solid
                    else
                        R.drawable.ic_star_line
                    ),
                    contentDescription = "스크랩",
                    tint = moduBlack
                )

                Icon(painter = painterResource(id = R.drawable.ic_xmark),
                    contentDescription = "창 닫기",
                    modifier = Modifier.bounceClick { activity?.finish() })
            }
            // 구분선
            Divider(
                color = moduGray_light, modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
            // 컨텐츠
            AndroidView(factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClient()
                    loadUrl(url)
                }
            }, update = {it.loadUrl(url)}
            )
        }
    }
}

@Preview
@Composable
fun CurationContentPreview() {

    CurationContent("")
}