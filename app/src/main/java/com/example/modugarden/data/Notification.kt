package com.example.modugarden.data

import android.net.Uri
import androidx.room.*

@Entity
data class Notification(
    val image: String,
    val type: Int, //알림 형태, 목적
    val name: String, //연관된 사용자. $name님이 회원님을 팔로우 했어요.
    val description: String, //알림 설명. 댓글/덧글일 경우 표시됨.
    val time: String, //알림 시간.
    val address: String //게시물 연관 주소.
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notification")
    fun getAll(): List<Notification>

    @Insert
    fun insert(notification: Notification)

    @Insert
    fun insertAll(notifications: List<Notification>)

    @Delete
    fun delete(notification: Notification)
}

@Database(entities = [Notification::class], version = 1)
abstract class NotificationDatabase: RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}