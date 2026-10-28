package com.carbajal.ayudacampus.data.local

import com.carbajal.ayudacampus.data.local.entity.Categoria
import com.carbajal.ayudacampus.data.local.entity.Comentario
import com.carbajal.ayudacampus.data.local.entity.Ticket
import com.carbajal.ayudacampus.data.local.entity.Usuario
import com.carbajal.ayudacampus.model.EstadoTicket
import com.carbajal.ayudacampus.model.Prioridad
import com.carbajal.ayudacampus.model.Rol
import com.carbajal.ayudacampus.data.local.entity.ActividadTicket
import kotlinx.coroutines.flow.first
import androidx.room.withTransaction

object DatosIniciales {

    suspend fun sembrar(db: AyudaDatabase) = db.withTransaction {
        // Solo sembrar si la base de datos no tiene usuarios
        if (db.usuarioDao().contar() == 0) {

            // 1. Categorías iniciales
            val categorias = listOf(
                Categoria(id = 1, nombre = "Hardware", icono = "pc", activa = true),
                Categoria(id = 2, nombre = "Software", icono = "code", activa = true),
                Categoria(id = 3, nombre = "Red / Wi-Fi", icono = "wifi", activa = true),
                Categoria(id = 4, nombre = "Cuenta", icono = "key", activa = true),
                Categoria(id = 5, nombre = "Impresión", icono = "printer", activa = true)
            )
            db.categoriaDao().insertarTodas(categorias)

            // 2. Usuarios de prueba
            val estudiante = Usuario(
                id = 1,
                nombre = "Meliton Carbajal",
                correo = "meliton@campus.test",
                clave = "1234",
                rol = Rol.CLIENTE.name
            )
            val soporte = Usuario(
                id = 2,
                nombre = "Soporte TI",
                correo = "soporte@campus.test",
                clave = "admin",
                rol = Rol.ADMIN.name
            )
            db.usuarioDao().insertarTodos(listOf(estudiante, soporte))

            // Helper para calcular tiempos relativos en milisegundos
            val ahora = System.currentTimeMillis()
            fun haceHoras(h: Long) = ahora - (h * 3600 * 1000)

            // 3. Tickets de prueba
            val ticket1Id = db.ticketDao().insertarTicket(
                Ticket(
                    id = 1,
                    clienteId = estudiante.id,
                    categoriaId = 3, // Red / Wi-Fi
                    titulo = "Sin Wi-Fi en el laboratorio 3",
                    descripcion = "Desde ayer no hay señal de Wi-Fi en todo el laboratorio. Afecta a 30 alumnos.",
                    ubicacion = "Lab 3 - Pabellón B",
                    prioridad = Prioridad.ALTA.name,
                    estado = EstadoTicket.EN_PROCESO.name,
                    fecha = haceHoras(5)
                )
            ).toInt()

            val ticket2Id = db.ticketDao().insertarTicket(
                Ticket(
                    id = 2,
                    clienteId = estudiante.id,
                    categoriaId = 1, // Hardware
                    titulo = "Teclado no responde",
                    descripcion = "El teclado de la PC #12 del Lab 2 no escribe la barra espaciadora.",
                    ubicacion = "Lab 2 - Aula 204",
                    prioridad = Prioridad.MEDIA.name,
                    estado = EstadoTicket.ABIERTO.name,
                    fecha = haceHoras(3)
                )
            ).toInt()

            val ticket3Id = db.ticketDao().insertarTicket(
                Ticket(
                    id = 3,
                    clienteId = estudiante.id,
                    categoriaId = 5, // Impresión
                    titulo = "No puedo imprimir en biblioteca",
                    descripcion = "Mando el documento pero la impresora muestra error de papel.",
                    ubicacion = "Biblioteca - Piso 1",
                    prioridad = Prioridad.BAJA.name,
                    estado = EstadoTicket.RESUELTO.name,
                    fecha = haceHoras(24)
                )
            ).toInt()

            db.ticketDao().insertarTicket(
                Ticket(
                    id = 4,
                    clienteId = estudiante.id,
                    categoriaId = 1, // Hardware
                    titulo = "Servidor del aula 305 caído",
                    descripcion = "No hay acceso a los archivos locales desde los equipos.",
                    ubicacion = "Aula 305",
                    prioridad = Prioridad.ALTA.name,
                    estado = EstadoTicket.ABIERTO.name,
                    fecha = haceHoras(2) // > 1 hora sin atender (genera alerta en el panel)
                )
            )

            db.ticketDao().insertarTicket(
                Ticket(
                    id = 5,
                    clienteId = estudiante.id,
                    categoriaId = 2, // Software
                    titulo = "Instalar Android Studio",
                    descripcion = "Se requiere actualización de versión en el equipo asignado.",
                    ubicacion = "Lab 1",
                    prioridad = Prioridad.MEDIA.name,
                    estado = EstadoTicket.ABIERTO.name,
                    fecha = haceHoras(1)
                )
            )

            db.ticketDao().insertarTicket(
                Ticket(
                    id = 6,
                    clienteId = estudiante.id,
                    categoriaId = 4, // Cuenta
                    titulo = "Olvidé mi contraseña del aula virtual",
                    descripcion = "No puedo ingresar a descargar mis laboratorios.",
                    ubicacion = "Remoto",
                    prioridad = Prioridad.BAJA.name,
                    estado = EstadoTicket.ABIERTO.name,
                    fecha = haceHoras(12)
                )
            )

            // 4. Comentarios de ejemplo
            db.ticketDao().insertarComentario(
                Comentario(
                    ticketId = ticket1Id,
                    autorId = soporte.id,
                    autorNombre = soporte.nombre,
                    autorRol = soporte.rol,
                    texto = "Revisamos el access point del pabellón B hoy.",
                    fecha = haceHoras(4)
                )
            )
            db.ticketDao().insertarComentario(
                Comentario(
                    ticketId = ticket1Id,
                    autorId = estudiante.id,
                    autorNombre = estudiante.nombre,
                    autorRol = estudiante.rol,
                    texto = "Gracias, quedo atento.",
                    fecha = haceHoras(3)
                )
            )
            db.ticketDao().insertarComentario(
                Comentario(
                    ticketId = ticket1Id,
                    autorId = soporte.id,
                    autorNombre = soporte.nombre,
                    autorRol = soporte.rol,
                    texto = "Reiniciamos el equipo, ¿ya tienes señal?",
                    fecha = haceHoras(1)
                )
            )

            db.ticketDao().insertarComentario(
                Comentario(
                    ticketId = ticket3Id,
                    autorId = soporte.id,
                    autorNombre = soporte.nombre,
                    autorRol = soporte.rol,
                    texto = "Reemplazamos el tóner y limpiamos la bandeja. Ya debería imprimir.",
                    fecha = haceHoras(2)
                )
            )
            // Registrar también las acciones de los datos de demostración con sus fechas originales.
            db.ticketDao().observarTodos().first().forEach { detalle ->
                db.soporteDao().insertarActividad(ActividadTicket(ticketId = detalle.ticket.id,
                    tituloTicket = detalle.ticket.titulo, autorNombre = detalle.cliente.nombre,
                    tipo = "CREACION", detalle = "Incidencia creada", fecha = detalle.ticket.fecha))
                detalle.comentarios.forEach { comentario ->
                    db.soporteDao().insertarActividad(ActividadTicket(ticketId = detalle.ticket.id,
                        tituloTicket = detalle.ticket.titulo, autorNombre = comentario.autorNombre,
                        tipo = "COMENTARIO", detalle = (if (comentario.esInterno) "Nota interna: " else "Comentario: ") + comentario.texto,
                        fecha = comentario.fecha))
                }
            }
        }
    }
}