package com.carbajal.ayudacampus

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.carbajal.ayudacampus.data.local.AyudaDatabase
import com.carbajal.ayudacampus.data.local.entity.Categoria
import com.carbajal.ayudacampus.data.local.entity.Usuario
import com.carbajal.ayudacampus.data.repository.AyudaRepositoryRoom
import com.carbajal.ayudacampus.model.EstadoTicket
import com.carbajal.ayudacampus.model.Prioridad
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class NotificacionesRepositoryTest {
    private lateinit var db: AyudaDatabase
    private lateinit var repository: AyudaRepositoryRoom

    @Before fun preparar() = runBlocking {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().targetContext,
            AyudaDatabase::class.java).build()
        repository = AyudaRepositoryRoom(db.usuarioDao(), db.categoriaDao(), db.ticketDao(), db)
        db.usuarioDao().insertarTodos(listOf(
            Usuario(1, "Ana", "ana@test", "1234", "CLIENTE"),
            Usuario(2, "Soporte", "soporte@test", "1234", "ADMIN"),
            Usuario(3, "Luis", "luis@test", "1234", "CLIENTE")
        ))
        db.categoriaDao().insertar(Categoria(1, "Red", "wifi"))
        Unit
    }

    @After fun cerrar() { db.close() }

    private suspend fun crear() = repository.crearTicket(1, 1, "Sin conexión", "No hay red", "Aula 1", Prioridad.MEDIA).toInt()

    @Test fun todosLosEstadosLleganAlUsuarioYSoporteSinDuplicarse() = runBlocking {
        val id = crear()
        repository.cambiarEstado(id, EstadoTicket.ABIERTO)
        repository.cambiarEstado(id, EstadoTicket.EN_PROCESO)
        repository.cambiarEstado(id, EstadoTicket.EN_PROCESO)
        repository.cambiarEstado(id, EstadoTicket.RESUELTO)
        repository.cerrarConCalificacion(id, 5)
        repository.reabrir(id)
        val cliente = repository.observarNotificaciones(1, "CLIENTE").first()
        val soporte = repository.observarNotificaciones(2, "ADMIN").first()
        val esperados = listOf("ABIERTO", "CERRADO", "RESUELTO", "EN_PROCESO", "ABIERTO")
        assertEquals(esperados, cliente.map { it.estado })
        assertEquals(esperados, soporte.map { it.estado })
        assertTrue(repository.observarNotificaciones(3, "CLIENTE").first().isEmpty())
        assertTrue(repository.observarNotificaciones(1, "ADMIN").first().isEmpty())
    }

    @Test fun lecturaSeGuardaSoloParaElDestinatario() = runBlocking {
        crear()
        val aviso = repository.observarNotificaciones(1, "CLIENTE").first().single()
        repository.marcarNotificacionLeida(aviso.id, 3, "CLIENTE")
        assertFalse(repository.observarNotificaciones(1, "CLIENTE").first().single().leida)
        repository.marcarNotificacionLeida(aviso.id, 1, "CLIENTE")
        assertTrue(repository.observarNotificaciones(1, "CLIENTE").first().single().leida)
        assertFalse(repository.observarNotificaciones(2, "ADMIN").first().single().leida)
        repository.marcarNotificacionesLeidas(2, "ADMIN")
        assertTrue(repository.observarNotificaciones(2, "ADMIN").first().single().leida)
    }

    @Test fun cierreDesdeSoporteNotificaYOperacionInvalidaNoGeneraAvisos() = runBlocking {
        val id = crear()
        try {
            repository.cerrarConCalificacion(id, 5)
            fail("Un ticket abierto no se puede calificar")
        } catch (_: IllegalArgumentException) { }
        assertEquals(1, repository.observarNotificaciones(1, "CLIENTE").first().size)
        repository.cambiarEstado(id, EstadoTicket.CERRADO)
        assertEquals("CERRADO", repository.observarNotificaciones(1, "CLIENTE").first().first().estado)
    }
}
