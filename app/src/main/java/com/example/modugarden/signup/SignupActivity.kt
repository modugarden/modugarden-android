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
        val social = intent.getBooleanExtra("social", false)
        val social_email = intent.getStringExtra("social_email")
        val social_name = intent.getStringExtra("social_name")
        setContent {
            SignupNavScreen(signupViewModel, social, social_email ?: "", social_name ?: "")
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SignupNavScreen(
    signupViewModel: SignupViewModel,
    social: Boolean,
    social_email: String,
    social_name: String
) {
    val navController = rememberAnimatedNavController()
    NavigationGraphSignup(
        navController = navController,
        signupViewModel = signupViewModel,
        social = social,
        social_email = social_email,
        social_name = social_name
    )
}