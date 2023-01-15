package com.example.modugarden.signup

import android.graphics.Rect
import android.view.ViewTreeObserver
import android.view.WindowInsets
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.ui.theme.*

@Composable
fun SignupEmailScreen(navController: NavHostController) {
    val textFieldMail = remember { mutableStateOf("") } //textField 데이터 값.
    val isTextFieldMailFocused = remember { mutableStateOf(false) } //textField가 포커싱 되어 있는지 여부.
    val isTextFieldError = remember { mutableStateOf(false) } //textField에 조건이 틀린 값이 들어갔는지 여부.
    var textFieldDescription = remember { mutableStateOf("") } //textField 설명.
    val focusManager = LocalFocusManager.current
    val keyboard by keyboardAsState()
    val dpScale = animateDpAsState(if(keyboard.toString() == "Closed") 18.dp else 0.dp) //버튼 애니메이션 처리.
    val shapeScale = animateDpAsState(if(keyboard.toString() == "Closed") 10.dp else 0.dp) //버튼 애니메이션 처리.
    var certNumber = "" //API에서 받아온 이메일 인증번호를 저장
    val mContext = LocalContext.current
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .addFocusCleaner(focusManager)
    ) {
        Column(
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp).padding(top = 50.dp)
            ) {
                Text("본인 인증을 위해\n이메일을 입력해주세요", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = moduBlack)
                Spacer(modifier = Modifier.height(40.dp))
                EditText(title = "메일 주소", data = textFieldMail, isTextFieldFocused = isTextFieldMailFocused, singleLine = true, keyboardType = KeyboardType.Email, errorListener = isTextFieldError, description = textFieldDescription.value)
            }
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier
                    .bounceClick {
                        if (android.util.Patterns.EMAIL_ADDRESS.matcher(textFieldMail.value).matches()) {
                                certNumber = "123456" //인증번호 생성 API에서 반환된 값을 저장하여 '이메일 인증' 화면으로 넘긴다.
                                navController.navigate(NAV_ROUTE_SIGNUP.EMAIL_CERT.routeName+"/"+certNumber+"/${textFieldMail.value}")
                        }
                        else {
                            isTextFieldError.value = true
                            textFieldDescription.value = "이메일 형식에 맞게 입력해야 해요"
                        }
                    }
                    .padding(dpScale.value)
                    .fillMaxWidth()
                    .alpha(if (textFieldMail.value != "") 1f else 0.4f),
                shape = RoundedCornerShape(shapeScale.value),
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