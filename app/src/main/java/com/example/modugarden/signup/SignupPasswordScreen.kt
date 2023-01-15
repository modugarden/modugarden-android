package com.example.modugarden.signup

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.modugarden.data.Signup
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.SignupViewModel

@Composable
fun SignupPasswordScreen(navController: NavHostController, data: Signup, signupViewModel: SignupViewModel) {
    val textFieldPw = remember { mutableStateOf(data.password) } //비밀번호 입력 데이터
    val isTextFieldPwFocused = remember { mutableStateOf(false) }
    val isTextFieldError = remember { mutableStateOf(false) } //비밀번호 조건이 틀린지 여부.
    val textFieldPwCheck = remember { mutableStateOf(data.password) } //비밀번호 확인 입력 데이터
    val isTextFieldPwCheckFocused = remember { mutableStateOf(false) }
    val textFieldDescription = remember { mutableStateOf("8자 이상 입력해야 해요") }
    val focusManager = LocalFocusManager.current
    val keyboard by keyboardAsState()
    val dpScale = animateDpAsState(if(keyboard.toString() == "Closed") 18.dp else 0.dp)
    val shapeScale = animateDpAsState(if(keyboard.toString() == "Closed") 10.dp else 0.dp)
    val mContext = LocalContext.current
    Log.d("certnumber", data.email)
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
                Text("로그인에 사용할\n비밀번호를 입력하세요", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = moduBlack)
                Spacer(modifier = Modifier.height(40.dp))
                EditText(title = "비밀번호", data = textFieldPw, isTextFieldFocused = isTextFieldPwFocused, singleLine = true, keyboardType = KeyboardType.Password, errorListener = isTextFieldError, description = textFieldDescription.value)
                Spacer(modifier = Modifier.height(20.dp))
                EditText(title = "비밀번호 확인", data = textFieldPwCheck, isTextFieldFocused = isTextFieldPwCheckFocused, singleLine = true, keyboardType = KeyboardType.Password, errorListener = isTextFieldError)
            }
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier
                    .bounceClick {
                        if (textFieldPw.value.length >= 8 && textFieldPwCheck.value.length >= 8) { //비밀번호와 비밀번호 확인이 빈칸이 아니고,
                            if (textFieldPw.value == textFieldPwCheck.value) { //서로 같을때,
                                signupViewModel.savePassword(textFieldPw.value)
                                navController.navigate(NAV_ROUTE_SIGNUP.TERMS.routeName+"/${data.email}/${textFieldPw.value}") //이용 약관으로 넘어감.
                            }
                            else {
                                isTextFieldError.value = true
                                textFieldDescription.value = "비밀번호와 비밀번호 확인이 달라요"
                            }
                        }
                    }
                    .padding(dpScale.value)
                    .fillMaxWidth()
                    .alpha(if (textFieldPw.value.length >= 8 && textFieldPwCheck.value.length >= 8) 1f else 0.4f),
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