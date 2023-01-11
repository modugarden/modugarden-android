package com.example.modugarden.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.route.NavigationGraphLogin

class LoginActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginNavScreen()
        }
    }
}

@Composable
fun LoginNavScreen() {
    val navController = rememberNavController()
    NavigationGraphLogin(navController = navController)
}