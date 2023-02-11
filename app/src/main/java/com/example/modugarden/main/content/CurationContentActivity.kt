package com.example.modugarden.main.content

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi

class CurationContentActivity: ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val extras = intent.extras
            if(extras != null) {
                val curation_id = extras.getInt("curation_id")
                Log.d("result-like", "receive : curation_id ${extras.getInt("curation_id")}")
                CurationContentScreen(curation_id)

            }
        }
    }
}
