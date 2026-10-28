package com.carbajal.ayudacampus

import android.app.Application
import com.carbajal.ayudacampus.data.local.AyudaDatabase
import com.carbajal.ayudacampus.data.local.DatosIniciales
import com.carbajal.ayudacampus.data.prefs.PreferenciasStore
import com.carbajal.ayudacampus.data.repository.AyudaRepository
import com.carbajal.ayudacampus.data.repository.AyudaRepositoryRoom
import com.carbajal.ayudacampus.viewmodel.AyudaViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AyudaApp : Application() {

    lateinit var database: AyudaDatabase
        private set
    lateinit var repository: AyudaRepository
        private set
    lateinit var preferenciasStore: PreferenciasStore
        private set
    lateinit var viewModelFactory: AyudaViewModelFactory
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        database = AyudaDatabase.obtener(this)
        repository = AyudaRepositoryRoom(
            usuarioDao = database.usuarioDao(),
            categoriaDao = database.categoriaDao(),
            ticketDao = database.ticketDao(),
            database = database
        )
        preferenciasStore = PreferenciasStore(this)
        viewModelFactory = AyudaViewModelFactory(repository, preferenciasStore)

        CoroutineScope(Dispatchers.IO).launch {
            DatosIniciales.sembrar(database)
        }
    }

    companion object {
        lateinit var instance: AyudaApp
            private set
    }
}
