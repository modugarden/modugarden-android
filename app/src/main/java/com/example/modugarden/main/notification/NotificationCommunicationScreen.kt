package com.example.modugarden.main.notification

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.room.Room

import com.example.modugarden.R
import com.example.modugarden.data.NotificationDatabase
import com.example.modugarden.route.NAV_ROUTE_DISCOVER_SEARCH
import com.example.modugarden.ui.theme.TopBar
import com.example.modugarden.viewmodel.UserViewModel

//임시 data class. 나중에 data class 파일 따로 만들어서 이주 예정.
//type == 0: 팔로우, 1: 댓글, 2: 덧글, 3: 계정 정지


@Composable
fun NotificationCommunicationScreen(navController: NavHostController, viewModel: UserViewModel) {
    val applicationContext = LocalContext.current.applicationContext

    val db = Room.databaseBuilder(
        applicationContext, NotificationDatabase::class.java, "notifcation database"
    ).allowMainThreadQueries().build()

    val notificationData = remember { mutableStateOf(db.notificationDao().getAll()) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            TopBar(
                title = "알림",
                main = true,
                icon1 = R.drawable.ic_settings,
                icon2 = R.drawable.baseline_delete_24,
                onClick1 = {
                    navController.navigate(NotificationScreen.Setting.name)
                },
                onClick2 = {
                    db.notificationDao().deleteAll(notificationData.value)
                    navController.navigate(NotificationScreen.Center.name) {
                        popUpTo(NotificationScreen.Center.name) {
                            inclusive = true
                        }
                    }
                },
                bottomLine = false
            )
            LazyColumn (
                contentPadding = PaddingValues(18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ){
                itemsIndexed(notificationData.value.reversed()) { index, item ->
                    NotificationCommunicationCard(viewModel, navController, item) {
                        db.notificationDao().delete(item)
                        navController.navigate(NotificationScreen.Center.name) {
                            popUpTo(NotificationScreen.Center.name) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }
    }
}