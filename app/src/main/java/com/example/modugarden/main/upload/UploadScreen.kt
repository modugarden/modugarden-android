package com.example.modugarden.main.upload

import android.app.DatePickerDialog
import android.content.Intent
import android.util.Log
import android.widget.DatePicker
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.bumptech.glide.request.RequestOptions
import com.example.modugarden.R
import com.example.modugarden.data.Category
import com.example.modugarden.main.upload.curation.UploadCurationActivity
import com.example.modugarden.main.upload.post.UploadPostActivity
import com.example.modugarden.route.NAV_ROUTE_SIGNUP
import com.example.modugarden.route.NAV_ROUTE_UPLOAD_POST
import com.example.modugarden.ui.theme.*
import com.skydoves.landscapist.glide.GlideImage
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
    val mContext = LocalContext.current

    Column() {
        TopBar(
            title = "업로드",
            main = true,
            bottomLine = false
        )
        //포스트 작성 버튼
        Card(
            modifier = Modifier
                .wrapContentSize(),
            shape = CircleShape,
            elevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 25.dp)
                    .padding(horizontal = 18.dp)
                    .fillMaxWidth()
                    .bounceClick {
                        mContext.startActivity(Intent(mContext, UploadPostActivity::class.java))
                    }
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_upload_post),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(60.dp)
                        .width(60.dp),
                )
                Spacer(modifier = Modifier.width(18.dp))
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text("포스트 작성", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = moduBlack)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text("카드 형식으로 넘겨볼 수 있어요", fontSize = 14.sp, color = moduGray_strong)
                }
            }
        }
        //큐레이션 작성 버튼
        Card(
            modifier = Modifier
                .wrapContentSize(),
            shape = CircleShape,
            elevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 25.dp)
                    .padding(horizontal = 18.dp)
                    .fillMaxWidth()
                    .bounceClick {
                        mContext.startActivity(Intent(mContext, UploadCurationActivity::class.java))
                    }
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_upload_curation),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(60.dp)
                        .width(60.dp),
                )
                Spacer(modifier = Modifier.width(18.dp))
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text("큐레이션 작성", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = moduBlack)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text("외부 게시물을 소개할 수 있어요", fontSize = 14.sp, color = moduGray_strong)
                }
            }
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