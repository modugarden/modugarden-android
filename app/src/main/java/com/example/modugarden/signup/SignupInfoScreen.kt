package com.example.modugarden.signup

import android.app.Activity
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
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder.signupNicknameIsDuplicatedAPI
import com.example.modugarden.api.SignupNicknameIsDuplicatedDTO
import com.example.modugarden.data.Signup
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.SignupViewModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

@Composable
fun SignupInfoScreen(navController: NavHostController, data: Signup, signupViewModel: SignupViewModel) {
    val textFieldName = remember { mutableStateOf(data.name) } //비밀번호 입력 데이터
    val isTextFieldNameFocused = remember { mutableStateOf(false) }
    val textFieldBirthday = remember { mutableStateOf(data.birthday) }
    val isTextFieldBirthdayFocused = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboard by keyboardAsState()
    val dpScale = animateDpAsState(if(keyboard.toString() == "Closed") 18.dp else 0.dp)
    val shapeScale = animateDpAsState(if(keyboard.toString() == "Closed") 15.dp else 0.dp)
    val mContext = LocalContext.current
    Log.d("certnumber", "${data.email}/${data.password}")
    val newData = signupViewModel.getAllData()

    val mCalendar = Calendar.getInstance()
    val mYear = mCalendar.get(Calendar.YEAR)
    val mMonth = mCalendar.get(Calendar.MONTH)
    val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val mDate = remember { mutableStateOf(data.birthday) }
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mYear/${mMonth+1}/$mDayOfMonth"
        }, mYear, mMonth, mDay
    )

    textFieldBirthday.value = mDate.value
    Log.d("apires", data.birthday)

    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .addFocusCleaner(focusManager)
    ) {
        Column(
            modifier = Modifier
        ) {
            TopBar(
                title = "",
                titleIcon = R.drawable.ic_arrow_left_bold,
                titleIconSize = 24.dp,
                titleIconOnClick = {
                    navController.popBackStack()
                },
                bottomLine = false
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .padding(top = 20.dp)
            ) {
                Text("똑똑, 누구세요?☺️", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = moduBlack)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "닉네임과 생년월일을 알려주세요.", fontSize = 15.sp, color = moduGray_strong)
                Spacer(modifier = Modifier.height(40.dp))
                EditText(title = "닉네임", data = textFieldName, isTextFieldFocused = isTextFieldNameFocused, singleLine = true)
                Spacer(modifier = Modifier.height(20.dp))
                Column {
                    Text("생년월일", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (isTextFieldBirthdayFocused.value) moduPoint else moduGray_strong)
                    Spacer(modifier = Modifier.height(5.dp))
                    Card(
                        modifier = Modifier
                            .bounceClick {
                                mDatePickerDialog.show()
                            }
                            .focusRequester(focusRequester)
                            .fillMaxWidth(),
                        elevation = 0.dp,
                        backgroundColor = if(isTextFieldBirthdayFocused.value) moduTextFieldPoint else moduBackground,
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Text(
                            "${textFieldBirthday.value.split("/")[0]}년 ${textFieldBirthday.value.split("/")[1]}월 ${textFieldBirthday.value.split("/")[2]}일",
                            color = moduBlack,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(15.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            //다음 버튼
            Card(
                modifier = Modifier
                    .bounceClick {
                        if (textFieldName.value != "" && textFieldBirthday.value != "") {
                            val jsonObject = JsonObject()
                            jsonObject.addProperty("nickname", textFieldName.value)
                            signupNicknameIsDuplicatedAPI
                                .signupNicknameIsDuplicatedAPI(jsonObject)
                                .enqueue(object :
                                    Callback<SignupNicknameIsDuplicatedDTO> {
                                    override fun onResponse(
                                        call: Call<SignupNicknameIsDuplicatedDTO>,
                                        response: Response<SignupNicknameIsDuplicatedDTO>
                                    ) {
                                        if (response.isSuccessful) {
                                            val res = response.body()
                                            if (res != null) {
                                                if (res.isSuccess) {
                                                    if (!(res.result.isDuplicated)) {
                                                        signupViewModel.saveName(textFieldName.value)
                                                        signupViewModel.saveBirthday(textFieldBirthday.value.split("/")[0]+"/"+ textFieldBirthday.value.split("/")[1]+"/"+ textFieldBirthday.value.split("/")[2])
                                                        Toast
                                                            .makeText(
                                                                mContext,
                                                                data.birthday,
                                                                Toast.LENGTH_SHORT
                                                            )
                                                            .show()
                                                        navController.navigate(NAV_ROUTE_SIGNUP.CATEGORY.routeName)
                                                    } else {
                                                        Toast
                                                            .makeText(
                                                                mContext,
                                                                "다른 사람이 이미 닉네임을 사용중이에요",
                                                                Toast.LENGTH_SHORT
                                                            )
                                                            .show()
                                                    }
                                                } else {
                                                    Toast
                                                        .makeText(
                                                            mContext,
                                                            "서버가 응답하지 않아요",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                }
                                            } else {
                                                Toast
                                                    .makeText(
                                                        mContext,
                                                        "서버가 응답하지 않아요",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                            }
                                        } else {
                                            Toast
                                                .makeText(
                                                    mContext,
                                                    "서버가 응답하지 않아요",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<SignupNicknameIsDuplicatedDTO>,
                                        t: Throwable
                                    ) {
                                        Toast
                                            .makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                })
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
    viewModelData: Signup,
    viewModel: SignupViewModel
) {


}