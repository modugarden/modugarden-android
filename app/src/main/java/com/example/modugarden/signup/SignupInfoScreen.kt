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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.ui.theme.*

@Composable
fun SignupInfoScreen(navController: NavHostController, email: String, password: String) {
    val textFieldName = remember { mutableStateOf("") } //비밀번호 입력 데이터
    val isTextFieldNameFocused = remember { mutableStateOf(false) }
    val textFieldBirthday = remember { mutableStateOf("") } //비밀번호 확인 입력 데이터
    val isTextFieldBirthdayFocused = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboard by keyboardAsState()
    val dpScale = animateDpAsState(if(keyboard.toString() == "Closed") 18.dp else 0.dp)
    val shapeScale = animateDpAsState(if(keyboard.toString() == "Closed") 10.dp else 0.dp)
    val mContext = LocalContext.current
    Log.d("certnumber", "${email}/${password}")
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
                Text("똑똑, 누구세요?☺️", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = moduBlack)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "닉네임과 생년월일을 알려주세요.", fontSize = 15.sp, color = moduGray_strong)
                Spacer(modifier = Modifier.height(40.dp))
                EditText(title = "닉네임", data = textFieldName, isTextFieldFocused = isTextFieldNameFocused, singleLine = true)
                Spacer(modifier = Modifier.height(20.dp))
                EditText(title = "생년월일", data = textFieldBirthday, isTextFieldFocused = isTextFieldBirthdayFocused, singleLine = true)
            }
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier
                    .bounceClick {
                        if(textFieldName.value != "" && textFieldBirthday.value != "") {
                            if(textFieldBirthday.value.count { it == '.' } == 2) {
                                if(textFieldBirthday.value.split(".")[0].length == 4
                                    && ((textFieldBirthday.value.split(".")[1].toInt() >= 1) && (textFieldBirthday.value.split(".")[1].toInt() <= 12))
                                    && (textFieldBirthday.value.split(".")[2].toInt() >= 1 && (textFieldBirthday.value.split(".")[2].toInt() <= 31))) {
                                    navController.navigate(NAV_ROUTE_SIGNUP.CATEGORY.routeName+"/${email}/${password}/${textFieldName.value}/${textFieldBirthday.value}")
                                }
                                else {
                                    Toast.makeText(mContext, "생년월일을 조건에 맞게 입력해야 해요", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else {
                                Toast.makeText(mContext, "생년월일을 조건에 맞게 입력해야 해요", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .padding(dpScale.value)
                    .fillMaxWidth()
                    .alpha(if (textFieldName.value != "" && textFieldBirthday.value != "") 1f else 0.4f),
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