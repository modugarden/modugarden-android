package com.example.modugarden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.main.notification.NotificationScreen
import com.example.modugarden.main.profile.MyProfileScreen
import com.example.modugarden.main.upload.UploadScreen
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_normal
import com.example.modugarden.main.discover.DiscoverScreen
import com.example.modugarden.main.follow.FollowScreen
import com.example.modugarden.route.NAV_ROUTE_BNB
import com.example.modugarden.route.NavigationGraphBNB
import com.example.modugarden.ui.theme.bounceClick

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainNavScreen()
        }
    }
}

@Composable
fun BottomNav(navController: NavController) {
    val items = listOf<NAV_ROUTE_BNB>(
        NAV_ROUTE_BNB.FOLLOW,
        NAV_ROUTE_BNB.DISCOVER,
        NAV_ROUTE_BNB.UPLOAD,
        NAV_ROUTE_BNB.NOTIFICATION,
        NAV_ROUTE_BNB.MYPROFILE
    )
    androidx.compose.material.BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color(0xFFB6BFBA)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.description,
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                    )
                },
                label = { Text(item.description, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                selectedContentColor = moduBlack,
                unselectedContentColor = moduGray_normal,
                selected = currentRoute == item.routeName,
                alwaysShowLabel = true,
                onClick = {
                    navController.navigate(item.routeName) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

@Composable
fun MainNavScreen() {
    val navController = rememberNavController()
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = { BottomNav(navController = navController) }
        ) {
            Box(modifier = Modifier.padding(it)) {
                NavigationGraphBNB(navController = navController)
            }
        }
    }
}