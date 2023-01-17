package com.example.modugarden.main.content

import android.app.Activity
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
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.R
import com.example.modugarden.main.follow.moduBold
import com.example.modugarden.ui.theme.addFocusCleaner
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_light
import com.example.modugarden.ui.theme.moduGray_strong

@Composable
fun CurationContent(navController: NavHostController) {
    val focusManager = LocalFocusManager.current
    //액티비티 종료할 때 사용할 변수
    val activity = (LocalContext.current as? Activity)
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
            Box(){}
        }
    }
}

@Preview
@Composable
fun CurationContentPreview() {
    val navController = rememberNavController()
    CurationContent(navController = navController)
}