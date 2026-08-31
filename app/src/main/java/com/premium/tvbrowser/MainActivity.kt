package com.premium.tvbrowser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.premium.tvbrowser.ui.BrowserApp
import com.premium.tvbrowser.ui.theme.OrbitalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrbitalTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BrowserApp()
                }
            }
        }
    }
}
