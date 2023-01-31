package com.example.modugarden.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.example.modugarden.MainActivity
import com.example.modugarden.R
import com.example.modugarden.api.dto.FcmSaveDTO
import com.example.modugarden.api.RetrofitBuilder.fcmSaveAPI
import com.example.modugarden.data.Notification
import com.example.modugarden.data.NotificationDatabase
import com.example.modugarden.login.LoginActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MyFcmService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("MyFcmService", "New token :: $token")
        sendTokenToServer(token)
    }
    private fun sendTokenToServer(token: String) {
        val prefs = getSharedPreferences("prefs", 0)
        val editor = prefs.edit()
        val jsonData = JsonObject().apply {
            addProperty("fcmToken", token)
        }
        fcmSaveAPI.fcmSaveAPI(jsonData).enqueue(object: Callback<FcmSaveDTO> {
            override fun onResponse(call: Call<FcmSaveDTO>, response: Response<FcmSaveDTO>) {
                if(response.isSuccessful) {
                    val res = response.body()
                    if(res != null) {
                        if(res.isSuccess) {
                            Log.d("apires", "토큰을 정상적으로 서버에 저장했어요")
                        }
                        else {
                            Log.e("apires", res.message)
                        }
                    }
                    else {
                        Log.e("apires", "res == null")
                    }
                }
                else {
                    Log.e("apires", "response == not Successful")
                }
            }

            override fun onFailure(call: Call<FcmSaveDTO>, t: Throwable) {
                Log.e("apires", "토큰 전송 실패!")
            }

        })

        editor.putString("fcm token", token).apply()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val db = Room.databaseBuilder(
            applicationContext, NotificationDatabase::class.java, "notifcation database"
        ).allowMainThreadQueries().build()
        Log.d("MyFcmService", "Notification Title :: ${message.notification?.title}")
        Log.d("MyFcmService", "Notification Body :: ${message.notification?.body}")
        Log.d("MyFcmService", "Notification Data :: ${message.data}")
        val title = message.notification?.title ?: ""
        val body = message.notification?.body ?: ""
        val data = message.data
        message.notification?.let {
            showNotification(it)
            db.notificationDao().insert(Notification(R.drawable.test_image1, 1, title, body, "", ""))
        }
    }
    private fun showNotification(notification: RemoteMessage.Notification) {
        val prefs = getSharedPreferences("prefs", 0)
        val intent = Intent(this, LoginActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val channelId = prefs.getString("fcm token", "")
        val notificationBuilder = NotificationCompat.Builder(this, channelId ?: "")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setContentIntent(pIntent)

        getSystemService(NotificationManager::class.java).run {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, "알림", NotificationManager.IMPORTANCE_HIGH)
                createNotificationChannel(channel)
            }
            notify(Date().time.toInt(), notificationBuilder.build())
        }
    }
}