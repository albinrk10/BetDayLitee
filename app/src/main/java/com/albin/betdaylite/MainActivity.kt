package com.albin.betdaylite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.albin.betdaylite.core.designsystem.BetDayTheme
import com.albin.betdaylite.core.navigation.BetDayNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BetDayTheme {
                BetDayNavHost()
            }
        }
    }
}
