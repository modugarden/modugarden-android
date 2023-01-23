package com.example.modugarden.main.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.modugarden.R
import com.example.modugarden.ui.theme.TopBar

class ProfileSaveActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 인텐트로 포스트, 큐레이션 리스트 받아옴
            Column (
                modifier = Modifier.fillMaxSize()
            ) {
                TopBar(
                    title = "저장한 항목",
                    titleIcon = R.drawable.ic_arrow_left_bold,
                    titleIconOnClick = {
                        finish()
                    }
                )
                CuratorProfileTab()
            }
        }
    }
}