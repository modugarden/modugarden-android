package com.example.modugarden.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_strong

@Composable
fun SignupEndScreen(navController: NavHostController, name: String = "") {
    val mcontext = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_waving_hand),
                contentDescription = null,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text("회원가입 완료", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduGray_strong, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(5.dp))
            Text("${name}님, 환영해요!", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = moduBlack)
        }
    }
}