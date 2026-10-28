package com.carbajal.ayudacampus.data.repository

import com.carbajal.ayudacampus.data.local.dao.CategoriaDao
import com.carbajal.ayudacampus.data.local.dao.TicketDao
import com.carbajal.ayudacampus.data.local.dao.UsuarioDao
import com.carbajal.ayudacampus.data.local.entity.Categoria
import com.carbajal.ayudacampus.data.local.entity.Comentario
import com.carbajal.ayudacampus.data.local.entity.Ticket
import com.carbajal.ayudacampus.data.local.entity.Usuario
import com.carbajal.ayudacampus.data.local.relation.TicketConDetalle
import com.carbajal.ayudacampus.model.ConteoCategoria
import com.carbajal.ayudacampus.model.EstadoTicket
import com.carbajal.ayudacampus.model.Prioridad
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import androidx.room.withTransaction
import com.carbajal.ayudacampus.model.validarHorarioAtencion
import com.carbajal.ayudacampus.data.local.AyudaDatabase
import com.carbajal.ayudacampus.data.local.entity.CitaAtencion
import com.carbajal.ayudacampus.data.local.entity.ActividadTicket

class AyudaRepositoryRoom (
    private val usuarioDao: UsuarioDao,
    private val categoriaDao: CategoriaDao,
    private val ticketDao: TicketDao,
    private val database: AyudaDatabase

) : AyudaRepository {
    override suspend fun obtenerUsuario(id: Int) = usuarioDao.obtenerPorId(id)
    override fun observarNotificaciones(usuarioId: Int, rol: String) = database.notificacionDao().observar(usuarioId, rol)
    override suspend fun marcarNotificacionLeida(id: Int, usuarioId: Int, rol: String) =
        database.notificacionDao().marcarLeida(id, usuarioId, rol)
    override suspend fun marcarNotificacionesLeidas(usuarioId: Int, rol: String) =
        database.notificacionDao().marcarTodasLeidas(usuarioId, rol)

    // Se invoca dentro de la misma transacción del cambio para no perder ni duplicar avisos.
    private suspend fun notificarEstado(ticket: Ticket, estado: EstadoTicket, anterior: String? = null) {
        val nombre = when (estado) {
            EstadoTicket.ABIERTO -> "Abierto"
            EstadoTicket.EN_PROCESO -> "En proceso"
            EstadoTicket.RESUELTO -> "Resuelto"
            EstadoTicket.CERRADO -> "Cerrado"
        }
        val mensaje = if (anterior == null) "Ticket creado en estado $nombre" else "El ticket cambió a $nombre"
        val destinatarios = listOf(ticket.clienteId to "CLIENTE") + usuarioDao.obtenerSoportes().map { it.id to "ADMIN" }
        val fecha = System.currentTimeMillis()
        database.notificacionDao().insertar(destinatarios.distinct().map { (id, rol) ->
            com.carbajal.ayudacampus.data.local.entity.Notificacion(
                ticketId = ticket.id, usuarioId = id, rol = rol, tituloTicket = ticket.titulo,
                estado = estado.name, mensaje = mensaje, fecha = fecha)
        })
    }

    private val soporteDao get() = database.soporteDao()
    override fun observarCitas() = soporteDao.observarCitas()
    override fun observarActividad() = soporteDao.observarActividad()

    override suspend fun programarCita(cita: CitaAtencion) {
        database.withTransaction {
            require(soporteDao.contarTecnico(cita.soporteId) == 1) { "Inicia sesión como Soporte TI para programar una atención" }
            validarHorarioAtencion(cita.inicio, cita.fin, cita.lugar, System.currentTimeMillis())
            val ticket = ticketDao.observarUno(cita.ticketId).first()
            require(ticket != null && ticket.ticket.estado !in listOf("RESUELTO", "CERRADO")) { "El ticket ya no está pendiente de atención" }
            require(soporteDao.contarCruces(cita.soporteId, cita.inicio, cita.fin) == 0) { "Ya tienes una atención programada en ese horario" }
            soporteDao.insertarCita(cita.copy(tituloTicket = ticket.ticket.titulo, estudiante = ticket.cliente.nombre))
        }
    }

    override suspend fun cambiarEstadoCita(id: Int, estado: String) {
        require(estado in listOf("COMPLETADA", "CANCELADA"))
        database.withTransaction {
            val cita = soporteDao.observarCitas().first().firstOrNull { it.id == id }
                ?: error("La cita ya no existe")
            require(estado != "COMPLETADA" || cita.inicio <= System.currentTimeMillis()) { "La atención todavía no ha comenzado" }
            soporteDao.cambiarEstadoCita(id, estado)
        }
    }

    override suspend fun login(correo: String, clave: String): Usuario? {
        return usuarioDao.login(correo, clave)
    }
    
    override suspend fun registrar(nombre: String, correo: String, clave: String, rol: String): Result<Usuario> {
        val existente = usuarioDao.obtenerPorCorreo(correo)
        if (existente != null) {
            return Result.failure(Exception("El correo ya está registrado"))
        }
        val nuevo = Usuario(
            nombre = nombre,
            correo = correo,
            clave = clave,
            rol = rol
        )
        val id = usuarioDao.insertar(nuevo)
        return Result.success(nuevo.copy(id = id.toInt()))
    }
    
    override fun observarCategorias(): Flow<List<Categoria>> {
        return categoriaDao.observarTodas()
    }
    override fun observarCategoriasActivas(): Flow<List<Categoria>> {
        return categoriaDao.observarActivas()
    }

    override suspend fun guardarCategoria(c: Categoria) {
        if (c.id == 0) {
            categoriaDao.insertar(c)
        } else {
            categoriaDao.actualizar(c)
        }
    }
    override suspend fun eliminarCategoria(c: Categoria) {
        categoriaDao.eliminar(c)
    }
    override suspend fun cambiarActiva(id: Int, activa: Boolean) {
        categoriaDao.cambiarActiva(id, activa)
    }
    override suspend fun crearTicket(
        clienteId: Int,
        categoriaId: Int,
        titulo: String,
        descripcion: String,
        ubicacion: String,
        prioridad: Prioridad
    ): Long {
        val nuevoTicket = Ticket(
            clienteId = clienteId,
            categoriaId = categoriaId,
            titulo = titulo,
            descripcion = descripcion,
            ubicacion = ubicacion,
            prioridad = prioridad.name,
            estado = EstadoTicket.ABIERTO.name,
            fecha = System.currentTimeMillis()
        )
        return database.withTransaction {
            val id = ticketDao.insertarTicket(nuevoTicket)
            val detalle = requireNotNull(ticketDao.observarUno(id.toInt()).first())
            soporteDao.insertarActividad(ActividadTicket(ticketId = id.toInt(), tituloTicket = titulo,
                autorNombre = detalle.cliente.nombre, tipo = "CREACION", detalle = "Incidencia creada", fecha = nuevoTicket.fecha))
            notificarEstado(detalle.ticket, EstadoTicket.ABIERTO)
            id
        }
    }
    override fun observarTicketsCliente(clienteId: Int): Flow<List<TicketConDetalle>> {
        return ticketDao.observarPorCliente(clienteId)
    }

    override fun observarTodosLosTickets(): Flow<List<TicketConDetalle>> {
        return ticketDao.observarTodos()
    }

    override fun observarTicket(id: Int): Flow<TicketConDetalle?> {
        return ticketDao.observarUno(id)
    }

    override suspend fun cambiarEstado(ticketId: Int, estado: EstadoTicket) {
        database.withTransaction {
            val ticket = requireNotNull(ticketDao.observarUno(ticketId).first()).ticket
            if (ticket.estado != estado.name) {
                ticketDao.actualizarEstado(ticketId, estado.name)
                notificarEstado(ticket, estado, ticket.estado)
            }
        }
    }
    override suspend fun cambiarPrioridad(ticketId: Int, prioridad: Prioridad) {
        ticketDao.actualizarPrioridad(ticketId, prioridad.name)
    }

    override suspend fun cerrarConCalificacion(ticketId: Int, calificacion: Int) {
        val fechaCierre = System.currentTimeMillis()
        database.withTransaction {
            require(calificacion in 1..5)
            val ticket = requireNotNull(ticketDao.observarUno(ticketId).first())
            require(ticket.ticket.estado == EstadoTicket.RESUELTO.name) { "Solo se puede calificar un ticket resuelto" }
            ticketDao.cerrar(ticketId, calificacion, fechaCierre)
            notificarEstado(ticket.ticket, EstadoTicket.CERRADO, ticket.ticket.estado)
            soporteDao.insertarActividad(ActividadTicket(ticketId = ticketId, tituloTicket = ticket.ticket.titulo,
                autorNombre = ticket.cliente.nombre, tipo = "CALIFICACION",
                detalle = "Calificó con $calificacion/5 y cerró la incidencia", fecha = fechaCierre))
        }
    }

    override suspend fun reabrir(ticketId: Int) {
        database.withTransaction {
            val ticket = requireNotNull(ticketDao.observarUno(ticketId).first())
            require(ticket.ticket.estado in listOf(EstadoTicket.RESUELTO.name, EstadoTicket.CERRADO.name)) { "El ticket ya está abierto" }
            ticketDao.reabrir(ticketId)
            notificarEstado(ticket.ticket, EstadoTicket.ABIERTO, ticket.ticket.estado)
            soporteDao.insertarActividad(ActividadTicket(ticketId = ticketId, tituloTicket = ticket.ticket.titulo,
                autorNombre = ticket.cliente.nombre, tipo = "REAPERTURA", detalle = "Incidencia reabierta"))
        }
    }

    override suspend fun agregarComentario(ticketId: Int, autor: Usuario, texto: String, esInterno: Boolean) {
        val comentario = Comentario(
            ticketId = ticketId,
            autorId = autor.id,
            autorNombre = autor.nombre,
            autorRol = autor.rol,
            texto = texto,
            fecha = System.currentTimeMillis(),
            esInterno = esInterno
        )
        database.withTransaction {
            val ticket = requireNotNull(ticketDao.observarUno(ticketId).first())
            ticketDao.insertarComentario(comentario)
            soporteDao.insertarActividad(ActividadTicket(ticketId = ticketId, tituloTicket = ticket.ticket.titulo,
                autorNombre = autor.nombre, tipo = "COMENTARIO",
                detalle = (if (esInterno) "Nota interna: " else "Comentario: ") + texto, fecha = comentario.fecha))
        }
    }

    override fun observarCantidadPorEstado(estado: EstadoTicket): Flow<Int> {
        return ticketDao.contarPorEstado(estado.name)
    }

    override fun observarCantidadPorCategoria(): Flow<List<ConteoCategoria>> {
        return ticketDao.contarPorCategoria()
    }

    override fun observarAltasSinAtender(limite: Long): Flow<Int> {
        return ticketDao.contarAltasSinAtender(limite)
    }

    override fun observarTiempoPromedioResolucion(): Flow<Double?> {
        return ticketDao.observarTiempoPromedioResolucion()
    }
}
