package com.carbajal.ayudacampus.data.repository

import com.carbajal.ayudacampus.data.local.entity.Categoria
import com.carbajal.ayudacampus.data.local.entity.Ticket
import com.carbajal.ayudacampus.data.local.entity.Usuario
import com.carbajal.ayudacampus.data.local.relation.TicketConDetalle
import com.carbajal.ayudacampus.model.ConteoCategoria
import com.carbajal.ayudacampus.model.EstadoTicket
import com.carbajal.ayudacampus.model.Prioridad
import kotlinx.coroutines.flow.Flow

interface AyudaRepository {
    suspend fun obtenerUsuario(id: Int): Usuario?
    fun observarNotificaciones(usuarioId: Int, rol: String): Flow<List<com.carbajal.ayudacampus.data.local.entity.Notificacion>>
    suspend fun marcarNotificacionLeida(id: Int, usuarioId: Int, rol: String)
    suspend fun marcarNotificacionesLeidas(usuarioId: Int, rol: String)

    fun observarCitas(): Flow<List<com.carbajal.ayudacampus.data.local.entity.CitaAtencion>>
    fun observarActividad(): Flow<List<com.carbajal.ayudacampus.data.local.entity.ActividadTicket>>
    suspend fun programarCita(cita: com.carbajal.ayudacampus.data.local.entity.CitaAtencion)
    suspend fun cambiarEstadoCita(id: Int, estado: String)

    suspend fun login(correo: String, clave: String): Usuario?
    suspend fun registrar(nombre: String, correo: String, clave: String, rol: String): Result<Usuario>
    fun observarCategorias(): Flow<List<Categoria>>
    fun observarCategoriasActivas(): Flow<List<Categoria>>
    suspend fun guardarCategoria(c: Categoria)
    suspend fun eliminarCategoria(c: Categoria)
    suspend fun cambiarActiva(id: Int, activa: Boolean)
    suspend fun crearTicket(
        clienteId: Int,
        categoriaId: Int,
        titulo: String,
        descripcion: String,
        ubicacion: String,
        prioridad: Prioridad
    ): Long
    fun observarTicketsCliente(clienteId: Int): Flow<List<TicketConDetalle>>
    fun observarTodosLosTickets(): Flow<List<TicketConDetalle>>
    fun observarTicket(id: Int): Flow<TicketConDetalle?>
    suspend fun cambiarEstado(ticketId: Int, estado: EstadoTicket)
    suspend fun cambiarPrioridad(ticketId: Int, prioridad: Prioridad)
    suspend fun cerrarConCalificacion(ticketId: Int, calificacion: Int)
    suspend fun reabrir(ticketId: Int)
    suspend fun agregarComentario(ticketId: Int, autor: Usuario, texto: String, esInterno: Boolean = false)
    fun observarCantidadPorEstado(estado: EstadoTicket): Flow<Int>
    fun observarCantidadPorCategoria(): Flow<List<ConteoCategoria>>
    fun observarAltasSinAtender(limite: Long): Flow<Int>
    fun observarTiempoPromedioResolucion(): Flow<Double?>
}
