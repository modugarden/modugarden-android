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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
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
import com.example.modugarden.ui.theme.moduPoint
import kotlinx.coroutines.launch

@Composable
fun PostContentLocationScreen(navController: NavHostController) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .addFocusCleaner(focusManager)
    )
    {
        Column(modifier = Modifier
            .background(Color.White)) {

            Row(modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                // 뒤로가기 아이콘 (변경 필요)
                Icon(
                    modifier = Modifier
                        .bounceClick { navController.popBackStack() },
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "뒤로 가기", tint = moduBlack
                )
                Spacer(modifier = Modifier.size(18.dp))
                Text(text = "위치 태그", style = moduBold, fontSize = 16.sp)
                Spacer(modifier = Modifier.size(5.dp))
                // 위치 태그 갯수
                Text(text = "2", color = moduGray_strong, fontSize = 16.sp)
                Spacer(modifier = Modifier.weight(1f))

            }
            // 구분선
            Divider(color = moduGray_light, modifier = Modifier.fillMaxWidth().height(1.dp))
            Spacer(modifier = Modifier.size(18.dp))

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp))
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
}

@Preview
@Composable
fun PostContentLocaionPreview() {
    val navController = rememberNavController()
    PostContentLocationScreen(navController = navController)
}