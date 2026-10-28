package com.carbajal.ayudacampus

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.ViewModelStore
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.carbajal.ayudacampus.data.local.AyudaDatabase
import com.carbajal.ayudacampus.data.local.DatosIniciales
import com.carbajal.ayudacampus.data.repository.AyudaRepositoryRoom
import com.carbajal.ayudacampus.ui.soporte.AgendaScreen
import com.carbajal.ayudacampus.ui.soporte.HistorialActividadScreen
import com.carbajal.ayudacampus.ui.theme.AyudaCampusTheme
import com.carbajal.ayudacampus.viewmodel.SoportePerfilViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SoportePantallasTest {
    @get:Rule val compose = createComposeRule()
    private lateinit var db: AyudaDatabase
    private lateinit var vm: SoportePerfilViewModel
    private val store = ViewModelStore()
    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before fun preparar() = runBlocking {
        db = Room.inMemoryDatabaseBuilder(context, AyudaDatabase::class.java).build()
        DatosIniciales.sembrar(db)
        vm = SoportePerfilViewModel(AyudaRepositoryRoom(db.usuarioDao(), db.categoriaDao(), db.ticketDao(), db))
        store.put("soporte", vm)
    }
    @After fun cerrar() { store.clear(); db.close() }

    @Test fun agendaProgramaAtencionYPermiteCancelarla() {
        compose.setContent { AyudaCampusTheme(darkTheme = false) { AgendaScreen(vm, 2, {}, {}) } }
        compose.waitUntil(10000) { vm.tickets.value.isNotEmpty() }
        compose.onNodeWithContentDescription("Programar atención").performClick()
        compose.onNodeWithText("Seleccionar ticket").performClick()
        compose.onNodeWithText("#2 · Teclado no responde\nMeliton Carbajal").performClick()
        compose.onNodeWithText("Programar", useUnmergedTree = true).performClick()
        compose.waitUntil(10000) { vm.citas.value.size == 1 }
        compose.onNodeWithText("Estudiante: Meliton Carbajal").assertIsDisplayed()
        compose.onRoot().captureToImage().asAndroidBitmap().let { bitmap ->
            context.openFileOutput("agenda-prueba.png", 0).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        }
        compose.onNodeWithText("Cancelar atención").performClick()
        compose.onAllNodesWithText("Cancelar atención").onLast().performClick()
        compose.waitUntil(10000) { vm.citas.value.singleOrNull()?.estado == "CANCELADA" }
        compose.onNodeWithText("Todas").performClick()
        compose.onNodeWithText("30 minutos · Cancelada").assertIsDisplayed()
    }

    @Test fun historialFiltraBuscaYAbreElTicketEnModoOscuro() {
        var abierto = 0
        compose.setContent { AyudaCampusTheme(darkTheme = true) { HistorialActividadScreen(vm, {}, { abierto = it }) } }
        compose.waitUntil(10000) { vm.actividad.value.size == 10 }
        compose.onNodeWithText("Comentarios", useUnmergedTree = true).performClick()
        compose.onNodeWithText("Buscar por ticket, persona o texto").performTextInput("tóner")
        compose.onNodeWithText("#3 · No puedo imprimir en biblioteca").assertIsDisplayed().performClick()
        org.junit.Assert.assertEquals(3, abierto)
        compose.onRoot().captureToImage().asAndroidBitmap().let { bitmap ->
            context.openFileOutput("historial-prueba.png", 0).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        }
    }
}
