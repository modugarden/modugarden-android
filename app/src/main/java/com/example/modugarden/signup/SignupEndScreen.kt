package com.example.modugarden.signup

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.navigation.NavHostController
import com.example.modugarden.ApplicationClass
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import com.example.modugarden.MainActivity
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.RetrofitBuilder.loginAPI
import com.example.modugarden.api.RetrofitBuilder.signupAPI
import com.example.modugarden.api.dto.FcmCheckDTO
import com.example.modugarden.api.dto.FcmSaveDTO
import com.example.modugarden.api.dto.LoginDTO
import com.example.modugarden.data.Signup
import com.example.modugarden.login.LoginActivity
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.ui.theme.bounceClick
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_strong
import com.example.modugarden.ui.theme.moduPoint
import com.example.modugarden.viewmodel.SignupViewModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun SignupEndScreen(navController: NavHostController, data: Signup, signupViewModel: SignupViewModel) {
    val mContext = LocalContext.current
    var currentRotation by remember { mutableStateOf(-5f) }
    val rotation = remember { Animatable(currentRotation) }
    var state = remember { mutableStateOf(true) }

    LaunchedEffect(state) {
        rotation.animateTo(
            targetValue = currentRotation + 30f,
            animationSpec = infiniteRepeatable(
                animation = tween(400),
                repeatMode = RepeatMode.Reverse
            )
        ) {
            currentRotation = value
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_waving_hand),
                contentDescription = null,
                modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)
                    .align(Alignment.CenterHorizontally)
                    .rotate(currentRotation),
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text("회원가입 완료", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduGray_strong, textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(5.dp))
            Text("${data.name}님, 환영해요!", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = moduBlack, textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier
                    .bounceClick {
                        //바로 회원가입 시키는게 아니라 로그인 하는 창으로 넘어가줌 (sharedPref에 문제가 생기는듯해서)
                        val intent = Intent(mContext, LoginActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        mContext.startActivity(intent)
                        //로그인 API 불러와서 팔로우 피드로 넘어감.
                        if(data.social) {
                            val jsonData = JsonObject().apply {
                                addProperty("email", data.email)
                            }
                            loginAPI.loginSocialAPI(jsonData).enqueue(object: Callback<LoginDTO> {
                                override fun onResponse(
                                    call: Call<LoginDTO>,
                                    response: Response<LoginDTO>
                                ) {
                                    if(response.isSuccessful) {
                                        val res = response.body()
                                        if(res != null) {
                                            if(res.isSuccess) {
                                                Toast.makeText(mContext, res.result.accessToken, Toast.LENGTH_SHORT).show()
                                                mContext.startActivity(
                                                    Intent(mContext, MainActivity::class.java)
                                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                )
                                            }
                                            else {
                                                Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        else {
                                            Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    else {
                                        Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                                    Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                                }
                            })
                        } else {
                            val jsonData = JsonObject().apply {
                                addProperty("email", data.email)
                                addProperty("password", data.password)
                            }
                            val editor = sharedPreferences.edit()
                            loginAPI.login(jsonData).enqueue(object: Callback<LoginDTO> {
                                override fun onResponse(
                                    call: Call<LoginDTO>,
                                    response: Response<LoginDTO>
                                ) {
                                    if(response.isSuccessful) {
                                        val res = response.body()
                                        if(res != null) {
                                            if(res.isSuccess) {
                                                editor.putString(ApplicationClass.accessToken, res.result.accessToken)
                                                editor.putString(ApplicationClass.refreshToken, res.result.refreshToken)
                                                editor.putInt(ApplicationClass.clientId, res.result.userId)
                                                editor.apply()
                                                RetrofitBuilder.fcmCheckAPI.fcmCheckAPI().enqueue(object: Callback<FcmCheckDTO> {
                                                    override fun onResponse(
                                                        call: Call<FcmCheckDTO>,
                                                        response: Response<FcmCheckDTO>
                                                    ) {
                                                        val fcmToken = sharedPreferences.getString("fcmToken", "")
                                                        val jsonDataFcmToken = JsonObject()
                                                        jsonDataFcmToken.apply {
                                                            addProperty("fcmToken", fcmToken)
                                                        }
                                                        if(response.isSuccessful) {
                                                            val res = response.body()
                                                            if(res != null) {
                                                                if(res.isSuccess) {
                                                                    if(fcmToken !in res.result.fcmTokens) {
                                                                        Log.d("apires", "새로운 토큰을 저장했어요")
                                                                        RetrofitBuilder.fcmSaveAPI.fcmSaveAPI(jsonDataFcmToken).enqueue(object: Callback<FcmSaveDTO> {
                                                                            override fun onResponse(call: Call<FcmSaveDTO>, response: Response<FcmSaveDTO>) {
                                                                                if(response.isSuccessful) {
                                                                                    val res = response.body()
                                                                                    if(res != null) {
                                                                                        if(res.isSuccess) {
                                                                                            Log.d("apires", "토큰을 정상적으로 서버에 저장했어요")
                                                                                        }
                                                                                        else {
                                                                                            Log.e("apires", res.message)
                                                                                        }
                                                                                    }
                                                                                    else {
                                                                                        Log.e("apires", "res == null")
                                                                                    }
                                                                                }
                                                                                else {
                                                                                    Log.e("apires", "response == not Successful")
                                                                                }
                                                                            }

                                                                            override fun onFailure(call: Call<FcmSaveDTO>, t: Throwable) {
                                                                                Log.e("apires", "토큰 전송 실패!")
                                                                            }
                                                                        })
                                                                    }
                                                                    Log.d("apires", "토큰 저장 API 작업 완료.")
                                                                }
                                                            }
                                                        }
                                                    }

                                                    override fun onFailure(
                                                        call: Call<FcmCheckDTO>,
                                                        t: Throwable
                                                    ) {

                                                    }

                                                })
                                                mContext.startActivity(
                                                    Intent(mContext, MainActivity::class.java)
                                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                )
                                            }
                                            else {
                                                Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        else {
                                            Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    else {
                                        Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                                    Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                    .padding(18.dp)
                    .fillMaxWidth()
                    .alpha(1f),
                shape = RoundedCornerShape(15.dp),
                backgroundColor = moduPoint,
                elevation = 0.dp
            ) {
                Text(
                    "시작하기",
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