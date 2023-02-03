package com.example.modugarden.main.content

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class CurationContentActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val curation_id = intent.getIntExtra("curation_id",0)
            CurationContentScreen(curation_id)
        }
    }
}


