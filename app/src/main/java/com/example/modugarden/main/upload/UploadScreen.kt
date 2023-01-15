package com.example.modugarden.main.upload

import android.app.DatePickerDialog
import android.content.Intent
import android.util.Log
import android.widget.DatePicker
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.example.modugarden.data.Category
import com.example.modugarden.main.upload.curation.UploadCurationActivity
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.ui.theme.*
import java.util.*

@Composable //업로드.
fun UploadScreen(navController: NavHostController) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .addFocusCleaner(focusManager)
    ) {
        UploadInfoScreen()
    }
}

@Composable
fun UploadInfoScreen() {
    val charactersLen = 40

    val titleData = remember { mutableStateOf("") }
    val titleFocused = remember { mutableStateOf(false) }
    val titleDescription = "글자 수 ${titleData.value.length}/${charactersLen}"
    val titleError = remember { mutableStateOf(false) }

    val categoryData = remember { mutableStateOf(Category.GARDENING) }
    val categoryFocused = remember { mutableStateOf(false) }

    val keyboard by keyboardAsState()
    val dpScale = animateDpAsState(if(keyboard.toString() == "Closed") 18.dp else 0.dp)
    val shapeScale = animateDpAsState(if(keyboard.toString() == "Closed") 10.dp else 0.dp)

    val mContext = LocalContext.current


    titleError.value = titleData.value.length > charactersLen

    Column {
        //상단 조작 바
        TopBar(
            title = "게시물 업로드",
            main = true,
        )
        Box(modifier = Modifier.padding(18.dp)) {
            Column {
                //제목 textField
                EditText(
                    title = "제목",
                    data = titleData,
                    isTextFieldFocused = titleFocused,
                    description = titleDescription,
                    errorListener = titleError,
                    singleLine = false,
                    textStyle = TextStyle(fontSize = 16.sp, color = moduBlack)
                )
                Spacer(modifier = Modifier.height(18.dp))
                //카테고리 설정 Button
                EditTextLikeButton(title = "카테고리", data = categoryData, isTextFieldFocused = categoryFocused)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        //다음 버튼
        Card(
            modifier = Modifier
                .bounceClick {
                    if(titleData.value.length in (1..charactersLen)) { //제목 글자 수가 1~25자라면
                        val intent = Intent(mContext, UploadCurationActivity::class.java) //임시 코드 : 큐레이션 정보 추가 화면으로 넘어감.
                        intent.putExtra("title", titleData.value)
                        intent.putExtra("category", categoryData.value.toString())
                        mContext.startActivity(intent)
                    }
                }
                .padding(dpScale.value)
                .fillMaxWidth()
                .alpha(if (titleData.value.length in (1..charactersLen)) 1f else 0.4f),
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

//카테고리 설정 버튼
@Composable
fun EditTextLikeButton(
    title: String,
    data: MutableState<Category>,
    isTextFieldFocused: MutableState<Boolean>,
) {
    val mContext = LocalContext.current

    val focusRequester = remember { FocusRequester() }
    Column {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (isTextFieldFocused.value) moduPoint else moduGray_strong)
        Spacer(modifier = Modifier.height(5.dp))
        Card(
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .bounceClick {
                    data.value =
                        if (data.value == Category.GARDENING) Category.TRIP else Category.GARDENING //임시 코드
                },
            elevation = 0.dp,
            backgroundColor = if(isTextFieldFocused.value) moduTextFieldPoint else moduBackground,
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(
                text = data.value.toString(),
                color = moduBlack,
                fontSize = 16.sp,
                modifier = Modifier.padding(15.dp)
            )
        }
    }
}