package com.example.modugarden.main.content

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun pre(){
    val time = "2023-02-06T09:23:00.016639"
    val value = remember{ mutableStateOf("") }
   Text(text = timeToDate(time))
}

@RequiresApi(Build.VERSION_CODES.O)
fun timeToDate(createdTime: String): String {
    val date = LocalDateTime.parse(createdTime)
    return date.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
}
//댓글, 팔로우피드
@RequiresApi(Build.VERSION_CODES.O)
fun timeFomatter (createdTime:String,value: MutableState<String>) : String {
    val date = createdTime.split("T")[0]
    val time = createdTime.split("T")[1].split(".")[0]
    val dateTime = "$date $time"
    return calculationTime(dateTimeToMillSec(dateTime), value = value)
}

@SuppressLint("SimpleDateFormat")
// 스트링으로 저장된 값을 millisecond 단위로 변경하는 메서드
fun dateTimeToMillSec(dateTime: String): Long{
    var timeInMilliseconds: Long = 0
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    try {
        val mDate = sdf.parse(dateTime)
        timeInMilliseconds = mDate!!.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return timeInMilliseconds
}

// 현재 시간과 비교해서 얼마나 지났는지 보여주는 메서드
fun calculationTime(createDateTime: Long, value:MutableState<String>): String{
    val nowDateTime = Calendar.getInstance().timeInMillis //현재 시간 to millisecond
    val differenceValue = nowDateTime - createDateTime //현재 시간 - 비교가 될 시간
    when {
        differenceValue < 60000 ->{ //59초보다 적다면
            value.value = "방금 전"
        }
        differenceValue < 3600000 -> { //59분 보다 적다면
            value.value =  TimeUnit.MILLISECONDS.toMinutes(differenceValue).toString() + "분 전"
        }
        differenceValue < 86400000 -> { //23시간 보다 적다면
            value.value =  TimeUnit.MILLISECONDS.toHours(differenceValue).toString() + "시간 전"
        }
        differenceValue <  604800000 -> { //7일 보다 적다면
            value.value =  TimeUnit.MILLISECONDS.toDays(differenceValue).toString() + "일 전"
        }
        differenceValue < 2419200000 -> { //3주 보다 적다면
            value.value =  (TimeUnit.MILLISECONDS.toDays(differenceValue)/7).toString() + "주 전"
        }
        differenceValue < 31556952000 -> { //12개월 보다 적다면
            value.value =  (TimeUnit.MILLISECONDS.toDays(differenceValue)/30).toString() + "개월 전"
        }
        else -> { //그 외
            value.value =  (TimeUnit.MILLISECONDS.toDays(differenceValue)/365).toString() + "년 전"
        }
    }
    return value.value
}

