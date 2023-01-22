package com.example.modugarden.main.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class SettingsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileSettingsScreen(this@SettingsActivity)
        }
    }
}