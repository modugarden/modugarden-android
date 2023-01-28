package com.example.modugarden.signup

import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder.signupAPI
import com.example.modugarden.api.SignupAPI
import com.example.modugarden.data.Category
import com.example.modugarden.data.Signup
import com.example.modugarden.data.SignupDTO
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.SignupViewModel
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable //회원가입 할 때 카테고리를 묻는 화면.
fun SignupCategoryScreen(navController: NavHostController, data: Signup, signupViewModel: SignupViewModel) {
    val mContext = LocalContext.current
    var categoryCheck = remember { mutableStateListOf(false, false, false) }
    val categories = remember { mutableStateListOf<String>() }
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
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
                Text("무엇에 관심이 있나요? \uD83D\uDCAD", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = moduBlack)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "김태윤 님이 필요한 콘텐츠를 제공해 드려요.", fontSize = 15.sp, color = moduGray_strong)
                Spacer(modifier = Modifier.height(40.dp))
                Card(
                    modifier = Modifier
                        .bounceClick {
                            categoryCheck[0] = !(categoryCheck[0])
                            signupViewModel.saveCategory(categoryCheck)
                        }
                        .fillMaxWidth(),
                    backgroundColor = Color.White,
                    elevation = 0.dp,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_potted_plant),
                            contentDescription = null,
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Text("가드닝", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduBlack, modifier = Modifier.align(Alignment.CenterVertically).padding(start = 18.dp))
                        Spacer(modifier = Modifier.weight(1f))
                        Image(
                            painter = painterResource(if(categoryCheck[0]) R.drawable.ic_check_solid else R.drawable.ic_check_line),
                            contentDescription = null,
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                Card(
                    modifier = Modifier
                        .bounceClick {
                            categoryCheck[1] = !(categoryCheck[1])
                            signupViewModel.saveCategory(categoryCheck)
                        }
                        .fillMaxWidth(),
                    backgroundColor = Color.White,
                    elevation = 0.dp,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_house_with_garden),
                            contentDescription = null,
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Text("플랜테리어", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduBlack, modifier = Modifier.align(Alignment.CenterVertically).padding(start = 18.dp))
                        Spacer(modifier = Modifier.weight(1f))
                        Image(
                            painter = painterResource(if(categoryCheck[1]) R.drawable.ic_check_solid else R.drawable.ic_check_line),
                            contentDescription = null,
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                Card(
                    modifier = Modifier
                        .bounceClick {
                            categoryCheck[2] = !(categoryCheck[2])
                            signupViewModel.saveCategory(categoryCheck)
                        }
                        .fillMaxWidth(),
                    backgroundColor = Color.White,
                    elevation = 0.dp,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_tent),
                            contentDescription = null,
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Text("여행/나들이", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduBlack, modifier = Modifier.align(Alignment.CenterVertically).padding(start = 18.dp))
                        Spacer(modifier = Modifier.weight(1f))
                        Image(
                            painter = painterResource(if(categoryCheck[2]) R.drawable.ic_check_solid else R.drawable.ic_check_line),
                            contentDescription = null,
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier
                    .bounceClick {
                        if(categoryCheck[0] || categoryCheck[1] || categoryCheck[2]) {
                                //회원가입 API 연결 (email, name, password, category)
                                //일반 계정으로 회원가입.
                        val tempObject = JsonArray()
                        for(i in categoryCheck) {
                            when(i) {
                                categoryCheck[0] -> tempObject.add(Category.GARDENING.category)
                                categoryCheck[1] -> tempObject.add(Category.TRIP.category)
                                categoryCheck[2] -> tempObject.add(Category.PLANTERIOR.category)
                                else -> tempObject.add(Category.GARDENING.category)
                            }
                        }
                        val signupJson = JsonObject().apply {
                            addProperty("birth", data.birthday)
                            add("categories", tempObject)
                            addProperty("email", data.email)
                            addProperty("nickname", data.name)
                            addProperty("password", data.password)
                        }
                        signupAPI
                            .signup(signupJson).enqueue(object: Callback<SignupDTO> {
                                override fun onResponse(
                                    call: Call<SignupDTO>,
                                    response: Response<SignupDTO>
                                ) {
                                    if(response.isSuccessful) {
                                        val res = response.body()
                                        if(res != null) {
                                            if(res.isSuccess) {
                                                //회원가입 API에서 회원가입 성공 리턴값을 받으면 가입 축하 화면으로 넘어갑니다.
                                                navController.navigate(NAV_ROUTE_SIGNUP.END.routeName) {
                                                    popUpTo(NAV_ROUTE_SIGNUP.EMAIL.routeName) {
                                                        inclusive = true
                                                    }
                                                }
                                            }
                                            else {
                                                Toast.makeText(mContext, "회원가입을 실패했어요", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        else {
                                            Toast.makeText(mContext, "데이터를 불러오지 못했어요", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    else {
                                        Toast.makeText(mContext, "서버와 연결하지 못했어요", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<SignupDTO>,
                                    t: Throwable
                                ) {
                                    Toast.makeText(mContext, "서버와 연결하지 못했어요", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                    .padding(18.dp)
                    .fillMaxWidth()
                    .alpha(if(categoryCheck[0] || categoryCheck[1] || categoryCheck[2]) 1f else 0.4f),
                shape = RoundedCornerShape(10.dp),
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