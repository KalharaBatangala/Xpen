package com.example.xpen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.xpen.ui.theme.XpenTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        var keepSplashOn = true
        splashScreen.setKeepOnScreenCondition { keepSplashOn }

        lifecycleScope.launch {
            delay(1200) // guaranteed minimum — same in debug AND release
            keepSplashOn = false
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XpenTheme {
                XpenApp()
            }
        }
    }
}