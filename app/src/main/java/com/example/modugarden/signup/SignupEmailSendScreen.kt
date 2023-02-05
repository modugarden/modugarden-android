package com.example.modugarden.signup

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.SignupEmailAuthenticationDTO
import com.example.modugarden.data.Signup
import com.example.modugarden.main.upload.UploadCurationEx
import com.example.modugarden.main.upload.UploadPostEx
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.SignupViewModel
import com.google.gson.JsonObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SignupEmailSendScreen(navController: NavHostController, data: Signup, viewModel: SignupViewModel) {

    val mContext = LocalContext.current

    val scope = rememberCoroutineScope()
    val oneExecutor = remember { mutableStateOf(false) }

    val notificationVisibility = remember { mutableStateOf(false) }
    val notificationVisibility1 = remember { mutableStateOf(false) }
    val notificationVisibility2 = remember { mutableStateOf(false) }
    val notificationVisibility3 = remember { mutableStateOf(false) }

    val authJson = JsonObject().apply {
        addProperty("email", data.email)
    }

    if(!oneExecutor.value) {
        oneExecutor.value = true

        scope.launch {
            delay(500)
            notificationVisibility.value = true
            delay(300)
            notificationVisibility1.value = true
            delay(300)
            notificationVisibility2.value = true
            delay(300)
            notificationVisibility3.value = true
        }

        RetrofitBuilder.signupAPI.signupEmailAuthentication(authJson)
            .enqueue(object : Callback<SignupEmailAuthenticationDTO> {
                override fun onResponse(
                    call: Call<SignupEmailAuthenticationDTO>,
                    response: Response<SignupEmailAuthenticationDTO>
                ) {
                    Log.d("apires", response.body().toString())
                    if (response.isSuccessful) {
                        val res = response.body()
                        if (res != null) {
                            if (res.isSuccess) {
                                viewModel.saveCert(res.result.authCode)
                                    scope.launch {
                                        navController.navigate(NAV_ROUTE_SIGNUP.EMAIL_CERT.routeName) {
                                            popUpTo(NAV_ROUTE_SIGNUP.EMAIL_SEND.routeName) {
                                                inclusive = true
                                            }
                                        }
                                }
                            }
                        } else {
                            Toast.makeText(mContext, "인증번호를 보내지 못했어요", Toast.LENGTH_SHORT).show()
                        }
                    } else {
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
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
            Text("인증번호를 보내고 있어요", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = moduBlack)
            Spacer(Modifier.size(5.dp))
            Text("${data.email}에서 확인할 수 있어요", fontSize = 15.sp, color = moduGray_strong)
            Spacer(Modifier.size(40.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                shape = RoundedCornerShape(15.dp),
                backgroundColor = moduBackground,
                elevation = 0.dp
            ) {
                SignupEmailSendCard(notificationVisibility.value, notificationVisibility1.value, notificationVisibility2.value, notificationVisibility3.value)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SignupEmailSendCard(
    visible: Boolean,
    visible1: Boolean,
    visible2: Boolean,
    visible3: Boolean
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .padding(top = 20.dp)
            .height(300.dp),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        backgroundColor = Color.White,
        elevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(animationSpec = tween(500, easing = EaseOutExpo)) + fadeIn(animationSpec = tween(500)) + scaleIn(animationSpec = tween(500)),
            ) {
                Card(
                    modifier = Modifier.padding(0.dp),
                    shape = RoundedCornerShape(17.dp),
                    backgroundColor = moduBackground,
                    elevation = 0.dp
                ) {
                    Row(Modifier.padding(18.dp)) {
                        Card(
                            shape = CircleShape,
                            backgroundColor = moduRed,
                            elevation = 0.dp,
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterVertically)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_envelope),
                                colorFilter = ColorFilter.tint(Color.White),
                                contentDescription = null,
                                modifier = Modifier.padding(7.dp)
                            )
                        }
                        Spacer(Modifier.size(10.dp))
                        Column {
                            Text("modugarden2023", color = moduBlack, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.size(5.dp))
                            Text("모두의 정원 회원가입 인증번호입니다.", color = moduGray_strong, fontSize = 11.sp)
                        }
                    }
                }
            }
            Card(
                modifier = Modifier.padding(0.dp),
                elevation = 0.dp
            ) {
                Row(Modifier.padding(vertical = 18.dp)) {
                    AnimatedVisibility(
                        visible = visible1,
                        enter = scaleIn(animationSpec = tween(500, easing = EaseOutBack))
                    ) {
                        Card(
                            shape = CircleShape,
                            backgroundColor = moduGray_light,
                            elevation = 0.dp,
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterVertically)
                        ) { }
                    }
                    Spacer(Modifier.size(10.dp))
                    Column(Modifier.align(Alignment.CenterVertically)) {
                        AnimatedVisibility(
                            visible = visible1,
                            enter = slideInHorizontally(animationSpec = tween(500, easing = EaseOutBack)) + fadeIn(animationSpec = tween(500))
                        ) {
                            Card(
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(100.dp),
                                shape = RoundedCornerShape(6.dp),
                                backgroundColor = moduGray_light,
                                elevation = 0.dp,
                            ) { }
                        }
                    }
                }
            }
            Card(
                modifier = Modifier.padding(0.dp),
                elevation = 0.dp
            ) {
                Row(Modifier.padding(bottom = 18.dp)) {
                    AnimatedVisibility(
                        visible = visible2,
                        enter = scaleIn(animationSpec = tween(500, easing = EaseOutBack))
                    ) {
                        Card(
                            shape = CircleShape,
                            backgroundColor = moduGray_light,
                            elevation = 0.dp,
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterVertically)
                        ) { }
                    }
                    Spacer(Modifier.size(10.dp))
                    Column(Modifier.align(Alignment.CenterVertically)) {
                        AnimatedVisibility(
                            visible = visible2,
                            enter = slideInHorizontally(animationSpec = tween(500, easing = EaseOutBack)) + fadeIn(animationSpec = tween(500))
                        ) {
                            Card(
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(140.dp),
                                shape = RoundedCornerShape(6.dp),
                                backgroundColor = moduGray_light,
                                elevation = 0.dp,
                            ) { }
                        }
                    }
                }
            }
            Card(
                modifier = Modifier.padding(0.dp),
                elevation = 0.dp
            ) {
                Row(Modifier.padding(bottom = 18.dp)) {
                    AnimatedVisibility(
                        visible = visible3,
                        enter = scaleIn(animationSpec = tween(500, easing = EaseOutBack))
                    ) {
                        Card(
                            shape = CircleShape,
                            backgroundColor = moduGray_light,
                            elevation = 0.dp,
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterVertically)
                        ) { }
                    }
                    Spacer(Modifier.size(10.dp))
                    Column(Modifier.align(Alignment.CenterVertically)) {
                        AnimatedVisibility(
                            visible = visible3,
                            enter = slideInHorizontally(animationSpec = tween(500, easing = EaseOutBack)) + fadeIn(animationSpec = tween(500))
                        ) {
                            Card(
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(120.dp),
                                shape = RoundedCornerShape(6.dp),
                                backgroundColor = moduGray_light,
                                elevation = 0.dp,
                            ) { }
                        }
                    }
                }
            }
        }
    }
}