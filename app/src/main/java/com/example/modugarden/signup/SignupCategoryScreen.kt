package com.example.modugarden.signup

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder.signupAPI
import com.example.modugarden.data.Category
import com.example.modugarden.data.Signup
import com.example.modugarden.api.dto.SignupDTO
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.SignupViewModel
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable //회원가입 할 때 카테고리를 묻는 화면.
fun SignupCategoryScreen(navController: NavHostController, data: Signup, signupViewModel: SignupViewModel) {
    val mContext = LocalContext.current
    val categoryCheck = remember { mutableStateListOf(false, false, false) }
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
                        Text("가드닝", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduBlack, modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 18.dp))
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
                        Text("플랜테리어", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduBlack, modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 18.dp))
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
                        Text("여행/나들이", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduBlack, modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 18.dp))
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
                        if (categoryCheck[0] || categoryCheck[1] || categoryCheck[2]) {
                            val categoryCheckString = JsonArray()
                            if (categoryCheck[0]) categoryCheckString.add(Category.GARDENING.category)
                            if (categoryCheck[1]) categoryCheckString.add(Category.PLANTERIOR.category)
                            if (categoryCheck[2]) categoryCheckString.add(Category.TRIP.category)
                            //회원가입 API 연결 (email, name, password, category)
                            val jsonData = JsonObject().apply {
                                addProperty("birth", data.birthday.split("/")[0]+ (if(data.birthday.split("/")[1].toInt() < 10) "0"+data.birthday.split("/")[1] else data.birthday.split("/")[1]) + (if(data.birthday.split("/")[2].toInt() < 10) "0"+data.birthday.split("/")[2] else data.birthday.split("/")[2]))
                                add("categories", categoryCheckString)
                                addProperty("email", data.email)
                                addProperty("isSocialLogin", data.social)
                                addProperty("nickname", data.name)
                                addProperty("password", if(data.social) "blabla0312" else data.password)
                            }
                            signupAPI.signup(jsonData).enqueue(object: Callback<SignupDTO> {
                                override fun onResponse(
                                    call: Call<SignupDTO>,
                                    response: Response<SignupDTO>
                                ) {
                                    if(response.isSuccessful) {
                                        val res = response.body()
                                        if(res != null) {
                                            Log.d("apires", res.code.toString())
                                            Toast.makeText(mContext, data.birthday.split("/")[0]+"/"+ (if(data.birthday.split("/")[1].toInt() < 10) "0"+data.birthday.split("/")[1] else data.birthday.split("/")[1]) +"/"+ (if(data.birthday.split("/")[2].toInt() < 10) "0"+data.birthday.split("/")[2] else data.birthday.split("/")[2]), Toast.LENGTH_SHORT).show()
                                            if(res.isSuccess) {
                                                //회원가입 API에서 회원가입 성공 리턴값을 받으면 가입 축하 화면으로 넘어갑니다.
                                                navController.navigate(NAV_ROUTE_SIGNUP.END.routeName) {
                                                    popUpTo(NAV_ROUTE_SIGNUP.EMAIL.routeName) {
                                                        inclusive = true
                                                    }
                                                }
                                            }
                                            else {
                                                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show()
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

                                override fun onFailure(call: Call<SignupDTO>, t: Throwable) {
                                    Toast.makeText(mContext, "서버가 응답하지 않아요", Toast.LENGTH_SHORT).show()
                                }

                            })
                        }
                    }
                    .padding(18.dp)
                    .fillMaxWidth()
                    .alpha(if (categoryCheck[0] || categoryCheck[1] || categoryCheck[2]) 1f else 0.4f),
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