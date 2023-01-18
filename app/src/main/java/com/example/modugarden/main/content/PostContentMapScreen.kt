package com.example.modugarden.main.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
fun PostContentMapScreen(navController: NavHostController) {
    val focusManager = LocalFocusManager.current
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
                // 뒤로가기
                Icon(
                    modifier = Modifier
                        .bounceClick { navController.popBackStack() },
                    painter = painterResource(id = R.drawable.ic_arrow_left_bold),
                    contentDescription = "뒤로 가기", tint = moduBlack
                )
                Spacer(modifier = Modifier.size(18.dp))
                Text(text = "위치 태그", style = moduBold, fontSize = 16.sp)
                Spacer(modifier = Modifier.size(5.dp))
                // 위치 태그 갯수
                Text(text = "1", color = moduGray_strong, fontSize = 16.sp)
                Spacer(modifier = Modifier.weight(1f))

            }
            // 구분선
            Divider(color = moduGray_light, modifier = Modifier
                .fillMaxWidth()
                .height(1.dp))
        }

        //하단
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(18.dp)
            .align(Alignment.BottomCenter))
        { // 위치 사진
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
                    .border(0.5.dp, Color(0xFFCCCCCC), CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(18.dp))
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                // 위치
                Text(text = "location", style = moduBold, fontSize = 12.sp,)
                // 상세 주소
                Text(text = "adress", fontSize = 14.sp, color = Color.Gray)
            }

        }

    }

    }

@Preview
@Composable
fun PostContentMapPreview() {
    val navController = rememberNavController()
    PostContentMapScreen(navController = navController)
}