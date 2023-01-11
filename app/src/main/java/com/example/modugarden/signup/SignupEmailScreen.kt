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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.ui.theme.*

@Composable
fun SignupEmailScreen(navController: NavHostController) {
    val textFieldMail = remember { mutableStateOf("") }
    val isTextFieldMailFocused = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboard by keyboardAsState()
    val dpScale = animateDpAsState(if(keyboard.toString() == "Closed") 18.dp else 0.dp)
    val shapeScale = animateDpAsState(if(keyboard.toString() == "Closed") 10.dp else 0.dp)
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
                Text("본인 인증을 위해\n이메일을 입력해주세요", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = moduBlack)
                Spacer(modifier = Modifier.height(40.dp))
                EditText(title = "메일 주소", data = textFieldMail, isTextFieldFocused = isTextFieldMailFocused, singleLine = true)
            }
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier
                    .bounceClick {
                        if ("@" in textFieldMail.value) {
                            if ( //이메일 주소 조건을 만족하면
                                textFieldMail.value.split("@")[0].isNotEmpty()
                                && textFieldMail.value.split("@")[1].isNotEmpty()
                                && ".." !in textFieldMail.value
                                && "++" !in textFieldMail.value
                                && "--" !in textFieldMail.value
                            ) {
                                //이메일 중복 확인 API 연결
                                //if (이메일이 중복되지 않았다면)
                                //해당 이메일에 인증번호를 전송하는 API 연결, certNumber에 인증번호 반환 값 저장.
                                    certNumber = "123456"
                                    navController.navigate(NAV_ROUTE_SIGNUP.EMAIL_CERT.routeName+"/"+certNumber+"/${textFieldMail.value}")
                            }
                            else {
                                Toast.makeText(mContext, "이메일 형식에 맞게 입력해요", Toast.LENGTH_SHORT).show()
                            }
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