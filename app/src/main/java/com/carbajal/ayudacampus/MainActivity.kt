package com.carbajal.ayudacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.carbajal.ayudacampus.ui.navigation.NavAyuda
import com.carbajal.ayudacampus.ui.theme.AyudaCampusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AyudaCampusTheme {
                NavAyuda()
            }
        }
    }
}
