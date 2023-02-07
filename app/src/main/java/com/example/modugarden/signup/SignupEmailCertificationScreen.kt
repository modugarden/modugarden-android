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
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder.signupAPI
import com.example.modugarden.api.api.SignupAPI
import com.example.modugarden.api.dto.SignupEmailAuthenticationDTO
import com.example.modugarden.data.Signup
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.SignupViewModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun SignupEmailCertificationScreen(navController: NavHostController, data: Signup, signupViewModel: SignupViewModel) {
    val certNumberNow = remember { mutableStateOf(data.cert) }//현재 스크린의 certNumber.
    val textFieldCert = remember { mutableStateOf("") }
    val isTextFieldCertFocused = remember { mutableStateOf(false) }
    val isTextFieldError = remember { mutableStateOf(false) } //에러 저장.
    val textFieldDescription = remember { mutableStateOf("") } //설명 저장.
    val mContext = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboard by keyboardAsState()
    val dpScale = animateDpAsState(if(keyboard.toString() == "Closed") 18.dp else 0.dp)
    val shapeScale = animateDpAsState(if(keyboard.toString() == "Closed") 15.dp else 0.dp)
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
                Text("인증번호를 입력해주세요", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = moduBlack)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "입력한 이메일로 인증번호를 전송했어요.", fontSize = 15.sp, color = moduGray_strong)
                Spacer(modifier = Modifier.height(40.dp))
                EditText(
                    title = "인증번호",
                    data = textFieldCert,
                    isTextFieldFocused = isTextFieldCertFocused,
                    keyboardType = KeyboardType.Text,
                    description = textFieldDescription.value,
                    errorListener = isTextFieldError,
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier
                        .bounceClick {
                            //이메일로 인증번호 다시 전송하는 API 연결
                            //API 결괏값으로 인증번호 저장.
                            val emailJson = JsonObject().apply {
                                addProperty("email", data.email)
                            }
                            signupAPI.signupEmailAuthentication(emailJson)
                                .enqueue(object: Callback<SignupEmailAuthenticationDTO> {
                                    override fun onResponse(
                                        call: Call<SignupEmailAuthenticationDTO>,
                                        response: Response<SignupEmailAuthenticationDTO>
                                    ) {
                                        Log.d("apires", response.body().toString())
                                        if(response.isSuccessful) {
                                            val res = response.body()
                                            if(res != null) {
                                                if(res.isSuccess) {
                                                    signupViewModel.saveCert(res.result.authCode)
                                                    Toast.makeText(mContext, "인증번호를 다시 보냈어요", Toast.LENGTH_SHORT).show()
                                                    certNumberNow.value = res.result.authCode
                                                }
                                            }
                                            else {
                                                Toast.makeText(mContext, "인증번호를 보내지 못했어요", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        else {
                                            Toast.makeText(mContext, "인증번호를 받지 못했어요", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<SignupEmailAuthenticationDTO>,
                                        t: Throwable
                                    ) {
                                        Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                                    }

                                })
                            Toast.makeText(mContext, "${data.email}로 인증번호를 재전송했어요", Toast.LENGTH_SHORT).show()
                        },
                    backgroundColor = moduBackground,
                    elevation = 0.dp,
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("메일 다시 받기", fontSize = 12.sp, color = moduGray_strong, modifier = Modifier.padding(10.dp))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier
                    .bounceClick {
                        Log.d("certnumber", certNumberNow.value)
                        if (textFieldCert.value != "") {
                            if (textFieldCert.value == certNumberNow.value) {//인증번호와 입력한 값이 맞다면
                                if ("!" !in textFieldCert.value) {
                                    navController.navigate(NAV_ROUTE_SIGNUP.PASSWORD.routeName + "/${data.email}")
                                }
                                else {
                                    Toast.makeText(mContext, "느낌표가 들어가면 안돼요", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                isTextFieldError.value = true
                                textFieldDescription.value = "인증번호가 틀렸어요"
                            }
                        }
                    }
                    .padding(dpScale.value)
                    .fillMaxWidth()
                    .alpha(if (textFieldCert.value != "") 1f else 0.4f),
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
