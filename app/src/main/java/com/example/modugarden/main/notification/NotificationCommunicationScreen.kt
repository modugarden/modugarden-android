package com.example.modugarden.main.notification

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.modugarden.ApplicationClass.Companion.commentChildNotification
import com.example.modugarden.ApplicationClass.Companion.commentNotification
import com.example.modugarden.ApplicationClass.Companion.followNotification
import com.example.modugarden.ApplicationClass.Companion.serviceNotification
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences

import com.example.modugarden.R
import com.example.modugarden.api.RetrofitBuilder.fcmSendAPI
import com.example.modugarden.api.dto.FcmSendDTO
import com.example.modugarden.data.Notification
import com.example.modugarden.data.NotificationDatabase
import com.example.modugarden.ui.theme.SmallButton
import com.example.modugarden.ui.theme.TopBar
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.messaging.ktx.remoteMessage
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.skydoves.landscapist.glide.GlideImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//임시 data class. 나중에 data class 파일 따로 만들어서 이주 예정.
//type == 0: 팔로우, 1: 댓글, 2: 덧글, 3: 계정 정지


@Composable
fun NotificationCommunicationScreen() {
    val applicationContext = LocalContext.current.applicationContext

    val db = Room.databaseBuilder(
        applicationContext, NotificationDatabase::class.java, "notifcation database"
    ).allowMainThreadQueries().build()

    val mContext = LocalContext.current

    val index = remember { mutableStateOf(0) }

    //알림을 서버에 저장하는 거면 여기서 API 호출.
    //알림을 Room에 저장하는 거면 여기서 Room 호출.

    val notificationData = db.notificationDao().getAll()

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
                bottomLine = false
            )
            SmallButton(text = "알림 전송") {
                val jsonBody = JsonObject()
                val notificationBody = JsonObject()
                val dataBody = JsonObject()
                dataBody.apply {
                    addProperty("image", "https://www.elegantthemes.com/blog/wp-content/uploads/2019/12/401-error-wordpress-featured-image.jpg")
                    addProperty("type", 0)
                    addProperty("name", "kim__tune_test")
                    addProperty("address", "")
                }
                notificationBody.apply {
                    addProperty("title", "kim__tune_test님이 팔로우 했어요")
                    addProperty("body", "kim__tune님이 팔로우 했어요")
                }
                jsonBody.apply {
                    addProperty("to", sharedPreferences.getString("fcmToken", ""))
                    addProperty("priority", "high")
                    add("notification", notificationBody)
                    add("data", dataBody)
                }
                Log.d("apires", jsonBody.toString())
                fcmSendAPI.fcmSendAPI(jsonBody = jsonBody).enqueue(object: Callback<FcmSendDTO> {
                    override fun onResponse(
                        call: Call<FcmSendDTO>,
                        response: Response<FcmSendDTO>
                    ) {
                        if(response.isSuccessful) {
                            val res = response.body()
                            if(res != null) {
                                Log.d("apires", res.results.toString())
                                if(index.value == 3) {
                                    index.value = 0
                                }
                                else {
                                    index.value += 1
                                }
                            }
                        }
                        Log.d("apires", response.toString())
                    }

                    override fun onFailure(call: Call<FcmSendDTO>, t: Throwable) {}

                })
            }
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