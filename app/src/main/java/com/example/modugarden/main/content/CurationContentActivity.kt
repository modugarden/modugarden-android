package com.example.modugarden.main.content

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class CurationContentActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val extras = intent.extras
            if(extras != null) {
                val curation_id = extras.getInt("curation_id")
//                val curation_id = intent.getIntExtra("curation_id",0)
                Log.d("result-like", "receive : curation_id ${extras.getInt("curation_id")}")
                CurationContentScreen(curation_id)

            }
        }
    }
}
