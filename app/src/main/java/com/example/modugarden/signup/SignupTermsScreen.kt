package com.example.modugarden.signup

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.ui.theme.*
import com.example.modugarden.R

@Composable
fun SignupTermsScreen(navController: NavHostController, email: String, password: String) {
    val isTermsCheck = remember { mutableStateOf(false) } //이용 약관에 동의했는지 여부.
    val mContext = LocalContext.current
    Log.d("certnumber", "${email}/${password}")
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .padding(top = 50.dp)
            ) {
                Text("이용 약관", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = moduBlack)
                Spacer(modifier = Modifier.height(40.dp))
                Card(
                    modifier = Modifier
                        .bounceClick {
                            Toast
                                .makeText(mContext, "이용 약관 페이지로 넘어가요", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .fillMaxWidth(),
                    border = BorderStroke(1.dp, moduGray_light),
                    backgroundColor = if(isTermsCheck.value) moduGray_light else Color.White,
                    elevation = 0.dp,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(18.dp)
                    ) {
                        Image(
                            painter = painterResource(if(isTermsCheck.value) R.drawable.ic_check_solid else R.drawable.ic_check_line),
                            contentDescription = null,
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                                .bounceClick {
                                    isTermsCheck.value = !isTermsCheck.value
                                }
                                .align(Alignment.CenterVertically)
                        )
                        Column(
                            modifier = Modifier.padding(start = 18.dp)
                        ) {
                            Text("개인정보 처리 방침", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduBlack)
                            Spacer(modifier = Modifier.height(5.dp))
                            Text("필수 동의", fontSize = 12.sp, color = moduGray_strong)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Image(
                            painter = painterResource(id = R.drawable.ic_chevron_right),
                            contentDescription = null,
                            modifier = Modifier
                                .height(17.dp)
                                .width(17.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier
                    .bounceClick {
                        if (isTermsCheck.value) {
                            navController.navigate(NAV_ROUTE_SIGNUP.INFO.routeName+"/${email}/${password}")
                        }
                    }
                    .padding(18.dp)
                    .fillMaxWidth()
                    .alpha(if (isTermsCheck.value) 1f else 0.4f),
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