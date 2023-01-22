package com.example.modugarden.main.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.modugarden.R
import com.example.modugarden.main.profile.myId
import com.example.modugarden.main.profile.user
import com.example.modugarden.main.profile.userId
import com.example.modugarden.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun SettingsProfileScreen(
    onButtonClicked: () -> Unit = {}
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(18.dp)
    ) {
        Box(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(100.dp)
            .bounceClick {

            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.test_image1),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = painterResource(id = R.drawable.ic_plus_profile),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
            )
        }
        val nicknameState = remember { mutableStateOf("") }
        val birthState = remember { mutableStateOf("") }
        val emailState = remember { mutableStateOf("") }
        val nicknameFocusState = remember { mutableStateOf(false) }
        val birthFocusState = remember { mutableStateOf(false) }
        val emailFocusState = remember { mutableStateOf(false) }
        Spacer(modifier = Modifier.height(36.dp))
        EditText(
            title = "닉네임",
            data = nicknameState,
            isTextFieldFocused = nicknameFocusState
        )
        Spacer(modifier = Modifier.height(18.dp))
        EditText(
            title = "생년월일",
            data = birthState,
            isTextFieldFocused = birthFocusState
        )
        Spacer(modifier = Modifier.height(18.dp))
        EditText(
            title = "이메일",
            data = emailState,
            isTextFieldFocused = emailFocusState
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "카테고리",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = moduGray_strong
        )
        Spacer(modifier = Modifier.height(5.dp))
        Row (
            Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            // 카테고리 리스트가 들어갈 레이지로우
            LazyRow(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                items(user.category) { category ->
                    Card (
                        Modifier
                            .wrapContentSize()
                            .fillMaxHeight()
                            .padding(end = 10.dp),
                        backgroundColor = moduBackground,
                        shape = RoundedCornerShape(10.dp),
                        elevation = 0.dp
                    ) {
                        Text(
                            text = category,
                            style = TextStyle(
                                color = moduBlack,
                                fontSize = 12.sp
                            ),
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(10.dp),
                backgroundColor = moduBackground,
                onClick = {},
                elevation = 0.dp
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            enabled = true
                        ) {
                            // 카테고리 추가
                        }
                        .wrapContentSize()
                        .background(color = Color.Transparent)
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        BottomButton(
            title = "수정 완료",
            onClick = {
                // 정보 수정 api
                onButtonClicked()
                },
            dpScale = 0.dp
        )
    }
}