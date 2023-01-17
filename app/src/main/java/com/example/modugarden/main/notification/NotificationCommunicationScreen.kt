package com.example.modugarden.main.notification

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import com.example.modugarden.R
import com.example.modugarden.ui.theme.TopBar

//임시 data class. 나중에 data class 파일 따로 만들어서 이주 예정.
//type == 0: 팔로우, 1: 댓글, 2: 덧글, 3: 계정 정지
data class NotificationData(
    val image: Int,
    val type: Int, //알림 형태, 목적
    val name: String, //연관된 사용자. $name님이 회원님을 팔로우 했어요.
    val description: String, //알림 설명. 댓글/덧글일 경우 표시됨.
    val time: String, //알림 시간.
    val address: String //게시물 연관 주소.
)

@Composable
fun NotificationCommunicationScreen() {


    var notificationData = listOf(
        NotificationData(R.drawable.test_image1, 0, "follo_me", "", "2022-12-31", "101"),
        NotificationData(R.drawable.test_image2, 1, "bbak__daegari", "야 생각좀 하고 살아라 요즘에는 왜이리 생각 없이 사는 애들이 많냐?", "2022-12-31", "102"),
        NotificationData(R.drawable.test_image5, 2, "na_nim_god", "관악산에서 밥머거도 대나여", "2022-12-31", "103"),
        NotificationData(R.drawable.test_image3, 3, "", "서비스 이용이 제한되었어요.", "2022-12-31", ""),
        NotificationData(R.drawable.test_image1, 0, "follo_me", "", "2022-12-31", "101"),
        NotificationData(R.drawable.test_image2, 1, "bbak__daegari", "야 생각좀 하고 살아라 요즘에는 왜이리 생각 없이 사는 애들이 많냐?", "2022-12-31", "102"),
        NotificationData(R.drawable.test_image5, 2, "na_nim_god", "관악산에서 밥머거도 대나여", "2022-12-31", "103"),
        NotificationData(R.drawable.ic_launcher_background, 3, "", "서비스 이용이 제한되었어요.", "2022-12-31", ""),
        NotificationData(R.drawable.test_image1, 0, "follo_me", "", "2022-12-31", "101"),
        NotificationData(R.drawable.test_image2, 1, "bbak__daegari", "야 생각좀 하고 살아라 요즘에는 왜이리 생각 없이 사는 애들이 많냐?", "2022-12-31", "102"),
        NotificationData(R.drawable.test_image5, 2, "na_nim_god", "관악산에서 밥머거도 대나여", "2022-12-31", "103"),
        NotificationData(R.drawable.ic_launcher_background, 3, "", "서비스 이용이 제한되었어요.", "2022-12-31", "")
    )

    val mContext = LocalContext.current

    //알림을 서버에 저장하는 거면 여기서 API 호출.
    //알림을 Room에 저장하는 거면 여기서 Room 호출.

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            TopBar(
                title = "알림",
                main = true,
                icon1 = R.drawable.ic_settings,
                onClick1 = {
                    Toast.makeText(mContext, "알림 설정으로 들어가요", Toast.LENGTH_SHORT).show()
                },
            )
            LazyColumn {
                itemsIndexed(
                    items = notificationData,
                ) { index, item ->
                    NotificationCommunicationCard(data = item, lastItem = index == notificationData.size - 1)
                }
            }
        }
    }
}