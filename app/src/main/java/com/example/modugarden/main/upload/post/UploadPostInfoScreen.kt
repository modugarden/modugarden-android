package com.example.modugarden.main.upload.post

import android.app.Activity
import android.content.Intent
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.modugarden.data.Category
import com.example.modugarden.data.UploadPost
import com.example.modugarden.main.upload.EditTextLikeButton
import com.example.modugarden.ui.theme.*
import com.example.modugarden.viewmodel.UploadPostViewModel
import com.example.modugarden.R
import com.example.modugarden.route.NAV_ROUTE_UPLOAD_POST

@Composable
fun UploadPostInfoScreen(
    navController: NavHostController,
    uploadPostViewModel: UploadPostViewModel,
    data: UploadPost
) {
    val charactersLen = 40

    val titleData = remember { mutableStateOf(data.title) }
    val titleFocused = remember { mutableStateOf(false) }
    val titleDescription = "글자 수 ${titleData.value.length}/${charactersLen}"
    val titleError = remember { mutableStateOf(false) }

    val categoryData = remember { mutableStateOf(data.category) }
    val categoryFocused = remember { mutableStateOf(false) }

    val keyboard by keyboardAsState()
    val dpScale = animateDpAsState(if(keyboard.toString() == "Closed") 18.dp else 0.dp)
    val shapeScale = animateDpAsState(if(keyboard.toString() == "Closed") 10.dp else 0.dp)

    val mContext = LocalContext.current


    titleError.value = titleData.value.length > charactersLen

    Column(
        modifier = Modifier
            .background(Color.White)
    ) {
        //상단 조작 바
        TopBar(
            title = "포스트 정보",
            titleIcon = R.drawable.ic_arrow_left_bold,
            titleIconSize = 20.dp,
            titleIconOnClick = {
                (mContext as Activity).finish()
            },
            main = false,
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
                        uploadPostViewModel.saveTitle(titleData.value)
                        uploadPostViewModel.saveCategory(categoryData.value)
                        navController.navigate(NAV_ROUTE_UPLOAD_POST.IMAGELIST.routeName)
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