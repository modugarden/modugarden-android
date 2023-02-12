package com.example.modugarden.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.example.modugarden.ApplicationClass.Companion.commentChildNotification
import com.example.modugarden.ApplicationClass.Companion.commentNotification
import com.example.modugarden.ApplicationClass.Companion.followNotification
import com.example.modugarden.ApplicationClass.Companion.serviceNotification
import com.example.modugarden.ApplicationClass.Companion.sharedPreferences
import com.example.modugarden.R
import com.example.modugarden.data.Notification
import com.example.modugarden.data.NotificationDatabase
import com.example.modugarden.login.LoginActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

val notificationList = listOf(followNotification, commentNotification, commentChildNotification, serviceNotification)

class MyFcmService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("MyFcmService", "New token :: $token")
        sendTokenToServer(token)
    }
    private fun sendTokenToServer(token: String) {
        val editor = sharedPreferences.edit()

        editor.putString("fcmToken", token).apply()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val db = Room.databaseBuilder(
            applicationContext, NotificationDatabase::class.java, "notifcation database"
        ).allowMainThreadQueries().build()
        Log.d("MyFcmService", "Notification Title :: ${message.notification?.title}")
        Log.d("MyFcmService", "Notification Body :: ${message.notification?.body}")
        Log.d("MyFcmService", "Notification ImageUrl :: ${message.notification?.imageUrl}")
        Log.d("MyFcmService", "Notification Data :: ${message.data}")
        val title = message.data["title"] ?: ""
        val body = message.data["body"] ?: ""
        val image = message.data["image"] ?: ""
        val type = message.data["type"] ?: ""
        val name = message.data["name"] ?: ""
        val address = message.data["address"] ?: ""
        message.data.let {
            showNotification(message.data)
            db.notificationDao().insert(Notification(image, type.toInt(), name, body, "", address))
        }
    }
    private fun showNotification(data: MutableMap<String, String>) {
        val prefs = sharedPreferences
        val intent = Intent(this, LoginActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val channelId = prefs.getString("fcm token", "")

        if(sharedPreferences.getBoolean(notificationList[data["type"]!!.toInt()],true)) {

            val notificationBuilder = NotificationCompat.Builder(this, channelId ?: "")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setContentTitle(data["title"])
                .setContentText(data["body"])
                .setContentIntent(pIntent)

            getSystemService(NotificationManager::class.java).run {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(channelId, "알림", NotificationManager.IMPORTANCE_HIGH)
                    createNotificationChannel(channel)
                }
                notify(Date().time.toInt(), notificationBuilder.build())
            }
        }
        // 타입 보고 설정 온이면 표시
        // 표시할때 타입이랑 아이디 빼고 표시
    }
}