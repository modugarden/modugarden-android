package com.example.modugarden.signup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.route.NavigationGraphSignup

class SignupActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignupNavScreen()
        }
    }
}

@Composable
fun SignupNavScreen() {
    val navController = rememberNavController()
    NavigationGraphSignup(navController = navController)
}