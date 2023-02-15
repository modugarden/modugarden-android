package com.example.modugarden

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.ui.theme.moduBlack
import com.example.modugarden.ui.theme.moduGray_normal
import com.example.modugarden.route.NAV_ROUTE_BNB
import com.example.modugarden.route.NAV_ROUTE_DISCOVER_SEARCH
import com.example.modugarden.route.NAV_ROUTE_FOLLOW
import com.example.modugarden.route.NavigationGraphBNB
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var onBackPressedAction: () -> Unit
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TedPermission.create()
                .setPermissionListener(object: PermissionListener {

                    override fun onPermissionGranted() {
                    }
                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    }
                })
                .setDeniedMessage("알림 권한을 허용하지 않으면\n푸시알림을 받을 수 없어요.")
                .setPermissions(Manifest.permission.POST_NOTIFICATIONS)
                .check()



            MainNavScreen()
        }
    }

}

@Composable
fun BottomNav(
    navController: NavHostController,
    scope: CoroutineScope,
    lazyScroll: LazyListState,
    navFollowController: NavHostController
) {
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
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true }

                        launchSingleTop = true
                        restoreState = true

                    }
                    if(currentRoute==item.routeName){
                        if(currentRoute == "FOLLOW") {
                            scope.launch {
                                lazyScroll.animateScrollToItem(0)
                            }
                            navFollowController
                                .popBackStack(NAV_ROUTE_FOLLOW.USERPROFILE.routeName, true, true)
                        }
                    }

                },
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavScreen() {
    val navController = rememberAnimatedNavController()
    val scope = rememberCoroutineScope()
    val lazyScroll = rememberLazyListState()
    val navFollowController = rememberNavController()
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = { BottomNav(navController = navController,scope,lazyScroll, navFollowController = navFollowController) }
        ) {
            Box(modifier = Modifier.padding(it)) {
                NavigationGraphBNB(navController = navController, scope = scope, lazyScroll = lazyScroll, navFollowController = navFollowController)

            }
        }
    }
}