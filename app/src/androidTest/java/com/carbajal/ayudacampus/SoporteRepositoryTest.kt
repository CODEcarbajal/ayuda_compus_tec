package com.carbajal.ayudacampus

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.carbajal.ayudacampus.data.local.AyudaDatabase
import com.carbajal.ayudacampus.data.local.DatosIniciales
import com.carbajal.ayudacampus.data.local.entity.CitaAtencion
import com.carbajal.ayudacampus.data.repository.AyudaRepositoryRoom
import com.carbajal.ayudacampus.model.EstadoTicket
import com.carbajal.ayudacampus.model.Prioridad
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SoporteRepositoryTest {
    private val context: Context get() = InstrumentationRegistry.getInstrumentation().targetContext
    private fun repo(db: AyudaDatabase) = AyudaRepositoryRoom(db.usuarioDao(), db.categoriaDao(), db.ticketDao(), db)
    private fun cita(inicio: Long) = CitaAtencion(ticketId = 2, tituloTicket = "", estudiante = "", soporteId = 2,
        inicio = inicio, fin = inicio + 1800000, lugar = "Lab 2", notas = "Verificar teclado")

    @Test fun registraAccionesConAutorYConservaCalificacionTrasReabrir() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(context, AyudaDatabase::class.java).build()
        try {
            DatosIniciales.sembrar(db)
            val repository = repo(db)
            val estudiante = db.usuarioDao().obtenerPorCorreo("meliton@campus.test")!!
            val id = repository.crearTicket(1, 1, "Falla monitor", "No enciende el monitor", "Lab 1", Prioridad.MEDIA).toInt()
            repository.agregarComentario(id, estudiante, "Sigue apagado")
            repository.cambiarEstado(id, EstadoTicket.RESUELTO)
            repository.cerrarConCalificacion(id, 5)
            repository.reabrir(id)
            val eventos = repository.observarActividad().first().filter { it.ticketId == id }
            assertEquals(setOf("CREACION", "COMENTARIO", "CALIFICACION", "REAPERTURA"), eventos.map { it.tipo }.toSet())
            assertTrue(eventos.all { it.autorNombre == estudiante.nombre })
            assertTrue(eventos.first { it.tipo == "CALIFICACION" }.detalle.contains("5/5"))
            assertNull(repository.observarTicket(id).first()!!.ticket.calificacion)
            assertEquals("ABIERTO", repository.observarTicket(id).first()!!.ticket.estado)
        } finally { db.close() }
    }

    @Test fun impideCrucesPermiteHorariosContiguosYLiberaHorarioCancelado() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(context, AyudaDatabase::class.java).build()
        try {
            DatosIniciales.sembrar(db)
            val repository = repo(db)
            val inicio = System.currentTimeMillis() + 3600000
            repository.programarCita(cita(inicio))
            assertTrue(runCatching { repository.programarCita(cita(inicio + 60000)) }.isFailure)
            repository.programarCita(cita(inicio + 1800000))
            val primera = repository.observarCitas().first().first()
            assertEquals("Meliton Carbajal", primera.estudiante)
            assertTrue(primera.tituloTicket.isNotBlank())
            assertTrue(runCatching { repository.cambiarEstadoCita(primera.id, "COMPLETADA") }.isFailure)
            repository.cambiarEstadoCita(primera.id, "CANCELADA")
            repository.programarCita(cita(inicio))
            assertEquals(3, repository.observarCitas().first().size)
            assertTrue(runCatching { repository.programarCita(cita(inicio + 7200000).copy(ticketId = 3)) }.isFailure)
            assertTrue(runCatching { repository.programarCita(cita(inicio + 7200000).copy(soporteId = 1)) }.isFailure)
        } finally { db.close() }
    }

    @Test fun migraVersionTresSinPerderTicketsYRecuperaActividadHistorica() = runBlocking {
        val nombre = "prueba-migracion-${System.nanoTime()}.db"
        fun abrir() = Room.databaseBuilder(context, AyudaDatabase::class.java, nombre)
            .addMigrations(AyudaDatabase.MIGRATION_3_4, AyudaDatabase.MIGRATION_4_5).build()
        var db = abrir()
        try {
            DatosIniciales.sembrar(db)
            db.ticketDao().cerrar(3, 4, System.currentTimeMillis())
            db.close()
            // Las tablas de negocio de v3 no cambiaron: reconstruir una base v3 sin las tablas nuevas.
            SQLiteDatabase.openDatabase(context.getDatabasePath(nombre).path, null, SQLiteDatabase.OPEN_READWRITE).use {
                it.execSQL("DROP TABLE notificaciones")
                it.execSQL("DROP TABLE citas_atencion")
                it.execSQL("DROP TABLE actividad_tickets")
                it.execSQL("DROP TABLE room_master_table")
                it.version = 3
            }
            db = abrir()
            val repository = repo(db)
            assertEquals(6, repository.observarTodosLosTickets().first().size)
            val eventos = repository.observarActividad().first()
            assertEquals(6, eventos.count { it.tipo == "CREACION" })
            assertEquals(4, eventos.count { it.tipo == "COMENTARIO" })
            assertEquals(1, eventos.count { it.tipo == "CALIFICACION" })
            assertEquals(0, eventos.count { it.tipo == "REAPERTURA" })
            assertEquals(4, repository.observarTicket(3).first()!!.ticket.calificacion)
            repository.programarCita(cita(System.currentTimeMillis() + 3600000))
            db.close()
            db = abrir()
            assertEquals(1, repo(db).observarCitas().first().size)
            assertEquals(11, repo(db).observarActividad().first().size)
        } finally { db.close(); context.deleteDatabase(nombre) }
    }
}
