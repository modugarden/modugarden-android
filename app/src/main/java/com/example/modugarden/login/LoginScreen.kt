package com.example.modugarden.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.MainActivity
import com.example.modugarden.signup.SignupActivity
import com.example.modugarden.ui.theme.*

class Login: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainLoginScreen()
        }
    }
}

@Composable
fun MainLoginScreen() {
    val textFieldId = remember { mutableStateOf("") }
    val isTextFieldFocusedId = remember { mutableStateOf(false) }
    val textFieldPw = remember { mutableStateOf("") }
    val isTextFieldFocusedPw = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val mContext = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .addFocusCleaner(focusManager)
            .padding(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(top = 50.dp)
        ) {
            Text(text = "로그인", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = moduBlack)
            Spacer(modifier = Modifier.height(40.dp))
            //아이디 입력 textField
            EditText(title = "아이디", data = textFieldId, isTextFieldFocused = isTextFieldFocusedId)
            Spacer(modifier = Modifier.height(20.dp))
            //비밀번호 입력 textField
            EditText(title = "비밀번호", data = textFieldPw, isTextFieldFocused = isTextFieldFocusedPw)
            Spacer(modifier = Modifier.height(30.dp))
            //로그인 버튼
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .bounceClick {
                        mContext.startActivity(
                            Intent(mContext, MainActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                    },
                backgroundColor = moduPoint,
                shape = RoundedCornerShape(10.dp),
                elevation = 0.dp,
            ) {
                Text(
                    "로그인",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
            Text("소셜 로그인", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduBlack, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.weight(1f))
            //계정 새로 만들기 버튼
            Card(
                modifier = Modifier
                    .bounceClick {
                        mContext.startActivity(Intent(mContext, SignupActivity::class.java))
                    }
                    .align(Alignment.CenterHorizontally),
                backgroundColor = Color.White,
                elevation = 0.dp,
            ) {
                Text("계정 새로 만들기", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduBlack, modifier = Modifier.padding(10.dp), textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}