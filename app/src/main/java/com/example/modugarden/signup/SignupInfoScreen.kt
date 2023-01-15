package com.example.modugarden.signup

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.ui.theme.*
import java.util.*

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
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .padding(top = 50.dp)
            ) {
                Text("똑똑, 누구세요?☺️", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = moduBlack)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "닉네임과 생년월일을 알려주세요.", fontSize = 15.sp, color = moduGray_strong)
                Spacer(modifier = Modifier.height(40.dp))
                EditText(title = "닉네임", data = textFieldName, isTextFieldFocused = isTextFieldNameFocused, singleLine = true)
                Spacer(modifier = Modifier.height(20.dp))
                EditTextLikeButtonDatePicker(title = "생년월일", data = textFieldBirthday, isTextFieldFocused = isTextFieldBirthdayFocused)
            }
            Spacer(modifier = Modifier.weight(1f))

            //다음 버튼
            Card(
                modifier = Modifier
                    .bounceClick {
                        if (textFieldName.value != "" && textFieldBirthday.value != "") {
                                    navController.navigate(NAV_ROUTE_SIGNUP.CATEGORY.routeName + "/${email}/${password}/${textFieldName.value}/${textFieldBirthday.value}")
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

//DatePicker
@Composable
fun EditTextLikeButtonDatePicker(
    title: String,
    data: MutableState<String>,
    isTextFieldFocused: MutableState<Boolean>,
) {
    val mContext = LocalContext.current
    
    val mCalendar = Calendar.getInstance()
    val mYear = mCalendar.get(Calendar.YEAR)
    val mMonth = mCalendar.get(Calendar.MONTH)
    val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val mDate = remember { mutableStateOf("20/12/2003") }
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
        }, mYear, mMonth, mDay
    )

    data.value = "${mDate.value.split("/")[2]}.${mDate.value.split("/")[1]}.${mDate.value.split("/")[0]}"

    val focusRequester = remember { FocusRequester() }
    Column {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (isTextFieldFocused.value) moduPoint else moduGray_strong)
        Spacer(modifier = Modifier.height(5.dp))
        Card(
            modifier = Modifier
                .bounceClick {
                           mDatePickerDialog.show()
                }
                .focusRequester(focusRequester)
                .fillMaxWidth(),
            elevation = 0.dp,
            backgroundColor = if(isTextFieldFocused.value) moduTextFieldPoint else moduBackground,
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(
                "${data.value.split(".")[0]}년 ${data.value.split(".")[1]}월 ${data.value.split(".")[2]}일",
                color = moduBlack,
                fontSize = 20.sp,
                modifier = Modifier.padding(15.dp)
            )
        }
    }
}