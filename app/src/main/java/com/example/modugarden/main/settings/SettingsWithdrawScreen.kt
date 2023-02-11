package com.example.modugarden.main.settings

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modugarden.R
import com.example.modugarden.api.AuthCallBack
import com.example.modugarden.api.RetrofitBuilder
import com.example.modugarden.api.dto.DeleteCommentResponse
import com.example.modugarden.api.dto.WithdrawResponse
import com.example.modugarden.login.LoginActivity
import com.example.modugarden.ui.theme.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun SettingsWithdrawScreen () {
    Column(modifier = Modifier.fillMaxSize()) {
        val withdrawDialogState = remember { mutableStateOf(false) }
        val context = LocalContext.current
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = "탈퇴하시면\n다음 서비스를 이용할 수 없어요",
            style = TextStyle(
                color = moduBlack,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(horizontal = 18.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomButton(
            title = "탈퇴하기",
            color = moduErrorPoint,
            onClick = {
                // 탈퇴 다이얼로그
                withdrawDialogState.value = !withdrawDialogState.value
            }
        )
        if (withdrawDialogState.value) {
            // 다이얼로그 호출
            SmallDialog(
                text = "정말 탈퇴하시겠습니까?",
                textColor = moduBlack,
                backgroundColor = Color.White,
                positiveButtonText = "탈퇴",
                negativeButtonText = "취소",
                positiveButtonTextColor = Color.White,
                negativeButtonTextColor = moduGray_strong,
                positiveButtonColor = moduErrorPoint,
                negativeButtonColor = moduGray_light,
                dialogState = withdrawDialogState
            ) {
                RetrofitBuilder.userAPI
                    .withdraw()
                    .enqueue(AuthCallBack<WithdrawResponse>(context, "탈퇴 요청"))
                context.startActivity(
                    Intent(context, LoginActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            }
        }
    }
}