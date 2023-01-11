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
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.ui.theme.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable //회원가입 할 때 카테고리를 묻는 화면.
fun SignupCategoryScreen(navController: NavHostController, email: String, password: String, name: String, birthday: String) {
    val mContext = LocalContext.current
    val isTermsCheck = remember { mutableStateOf(false) } //이용 약관에 동의했는지 여부.
    var categoryCheck = remember { mutableStateOf(listOf(0)) }
    Log.d("certnumber", "${email}/${password}/${name}/${birthday}")
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
                Text("무엇에 관심이 있나요? \uD83D\uDCAD", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = moduBlack)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "김태윤 님이 필요한 콘텐츠를 제공해 드려요.", fontSize = 15.sp, color = moduGray_strong)
                Spacer(modifier = Modifier.height(40.dp))
                Card(
                    modifier = Modifier
                        .bounceClick {
                            if (1 in categoryCheck.value) categoryCheck.value =
                                categoryCheck.value + 1
                            else categoryCheck.value -= 1
                        }
                        .fillMaxWidth(),
                    backgroundColor = if(isTermsCheck.value) moduGray_light else Color.White,
                    elevation = 0.dp,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(18.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_potted_plant),
                            contentDescription = null,
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Text("식물 가꾸기", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = moduBlack)
                        Spacer(modifier = Modifier.weight(1f))
                        Image(
                            painter = painterResource(if(1 in categoryCheck.value) R.drawable.ic_check_solid else R.drawable.ic_check_line),
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
                        if (isTermsCheck.value) {
                            navController.navigate(NAV_ROUTE_SIGNUP.INFO.routeName + "/${email}/${password}")
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