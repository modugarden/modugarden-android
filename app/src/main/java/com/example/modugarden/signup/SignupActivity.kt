package com.example.modugarden.signup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.route.NavigationGraphSignup
import com.example.modugarden.viewmodel.SignupViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class SignupActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val signupViewModel: SignupViewModel by viewModels()
        setContent {
            SignupNavScreen(signupViewModel)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SignupNavScreen(
    signupViewModel: SignupViewModel
) {
    val navController = rememberAnimatedNavController()
    NavigationGraphSignup(
        navController = navController,
        signupViewModel = signupViewModel
    )
}