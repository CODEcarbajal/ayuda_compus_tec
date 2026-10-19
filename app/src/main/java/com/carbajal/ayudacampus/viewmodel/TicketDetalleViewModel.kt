package com.carbajal.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbajal.ayudacampus.data.local.entity.Usuario
import com.carbajal.ayudacampus.data.local.relation.TicketConDetalle
import com.carbajal.ayudacampus.data.repository.AyudaRepository
import com.carbajal.ayudacampus.model.EstadoTicket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TicketDetalleViewModel(
    private val ticketId: Int,
    private val repository: AyudaRepository
) : ViewModel() {

    // Flujo reactivo en tiempo real del detalle del ticket
    val ticket: StateFlow<TicketConDetalle?> = repository.observarTicket(ticketId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _nuevoComentario = MutableStateFlow("")
    val nuevoComentario: StateFlow<String> = _nuevoComentario.asStateFlow()

    private val _calificacion = MutableStateFlow(0)
    val calificacion: StateFlow<Int> = _calificacion.asStateFlow()

    fun actualizarNuevoComentario(texto: String) {
        _nuevoComentario.value = texto
    }

    fun seleccionarCalificacion(estrellas: Int) {
        _calificacion.value = estrellas
    }

    fun agregarComentario(autor: Usuario) {
        val texto = _nuevoComentario.value.trim()
        val currentTicket = ticket.value?.ticket

        // Regla: No comentar si está vacío o si el ticket ya está CERRADO
        if (texto.isBlank() || currentTicket?.estado == EstadoTicket.CERRADO.name) return

        viewModelScope.launch {
            repository.agregarComentario(ticketId, autor, texto)
            _nuevoComentario.value = ""
        }
    }

    fun cerrarConCalificacion(comentarioFinal: String, autor: Usuario) {
        val currentTicket = ticket.value?.ticket

        // Regla: Solo cerrar si está RESUELTO y la calificación es de 1 a 5
        if (currentTicket?.estado != EstadoTicket.RESUELTO.name) return
        if (_calificacion.value !in 1..5) return

        viewModelScope.launch {
            if (comentarioFinal.isNotBlank()) {
                repository.agregarComentario(ticketId, autor, comentarioFinal.trim())
            }
            repository.cerrarConCalificacion(ticketId, _calificacion.value)
        }
    }

    fun reabrir() {
        val currentTicket = ticket.value?.ticket

        // Regla: Solo reabrir si el estado actual es RESUELTO
        if (currentTicket?.estado != EstadoTicket.RESUELTO.name) return

        viewModelScope.launch {
            repository.reabrir(ticketId)
            _calificacion.value = 0
        }
    }
}