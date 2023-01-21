package com.example.modugarden.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.compose.rememberNavController
import com.example.modugarden.route.NavigationGraphLogin
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class LoginActivity: ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val RC_SIGN_IN = 1313

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginNavScreen()
        }
    }
    fun googleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("869252961332-55mlg41lk58vd8qpthfikc0echuu0mb5.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }
}

@Composable
fun LoginNavScreen() {
    val navController = rememberNavController()
    NavigationGraphLogin(navController = navController)
}