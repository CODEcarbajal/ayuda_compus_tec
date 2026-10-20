package com.carbajal.ayudacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.carbajal.ayudacampus.ui.navigation.NavAyuda
import com.carbajal.ayudacampus.ui.theme.AyudaCampusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = applicationContext as AyudaApp

        setContent {
            val modoOscuro by app.preferenciasStore.modoOscuro.collectAsState(initial = false)
            AyudaCampusTheme(darkTheme = modoOscuro) {
                NavAyuda()
            }
        }
    }
}
