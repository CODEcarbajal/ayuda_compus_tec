package com.carbajal.ayudacampus

import android.app.Application
import com.carbajal.ayudacampus.data.local.AyudaDatabase
import com.carbajal.ayudacampus.data.local.DatosIniciales
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AyudaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val database = AyudaDatabase.obtener(this)
        CoroutineScope(Dispatchers.IO).launch {
            DatosIniciales.sembrar(database)
        }
    }
}