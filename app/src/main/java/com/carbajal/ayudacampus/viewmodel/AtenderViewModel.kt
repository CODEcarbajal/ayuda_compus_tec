package com.carbajal.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbajal.ayudacampus.data.local.entity.Usuario
import com.carbajal.ayudacampus.data.local.relation.TicketConDetalle
import com.carbajal.ayudacampus.data.repository.AyudaRepository
import com.carbajal.ayudacampus.model.EstadoTicket
import com.carbajal.ayudacampus.model.Prioridad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AtenderViewModel(
    private val ticketId: Int,
    private val repository: AyudaRepository
) : ViewModel() {

    val ticket: StateFlow<TicketConDetalle?> = repository.observarTicket(ticketId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _estadoSeleccionado = MutableStateFlow<EstadoTicket?>(null)
    val estadoSeleccionado: StateFlow<EstadoTicket?> = _estadoSeleccionado.asStateFlow()

    private val _prioridadSeleccionada = MutableStateFlow<Prioridad?>(null)
    val prioridadSeleccionada: StateFlow<Prioridad?> = _prioridadSeleccionada.asStateFlow()

    private val _nuevoRespuesta = MutableStateFlow("")
    val nuevoRespuesta: StateFlow<String> = _nuevoRespuesta.asStateFlow()

    private val _esNotaInterna = MutableStateFlow(false)
    val esNotaInterna: StateFlow<Boolean> = _esNotaInterna.asStateFlow()

    fun inicializarValores(estadoInicial: String, prioridadInicial: String) {
        if (_estadoSeleccionado.value == null) {
            _estadoSeleccionado.value = EstadoTicket.entries.find { it.name == estadoInicial } ?: EstadoTicket.ABIERTO
        }
        if (_prioridadSeleccionada.value == null) {
            _prioridadSeleccionada.value = Prioridad.entries.find { it.name == prioridadInicial } ?: Prioridad.MEDIA
        }
    }

    fun seleccionarEstado(estado: EstadoTicket) {
        _estadoSeleccionado.value = estado
    }

    fun seleccionarPrioridad(prioridad: Prioridad) {
        _prioridadSeleccionada.value = prioridad
    }

    fun actualizarRespuesta(texto: String) {
        _nuevoRespuesta.value = texto
    }

    fun actualizarEsNotaInterna(activado: Boolean) {
        _esNotaInterna.value = activado
    }

    fun enviarRespuesta(autor: Usuario) {
        val texto = _nuevoRespuesta.value.trim()
        if (texto.isBlank()) return

        viewModelScope.launch {
            repository.agregarComentario(ticketId, autor, texto, esInterno = _esNotaInterna.value)
            _nuevoRespuesta.value = ""
            _esNotaInterna.value = false
        }
    }

    fun guardarCambios(onExito: () -> Unit) {
        val estado = _estadoSeleccionado.value ?: return
        val prioridad = _prioridadSeleccionada.value ?: return

        viewModelScope.launch {
            repository.cambiarEstado(ticketId, estado)
            repository.cambiarPrioridad(ticketId, prioridad)
            onExito()
        }
    }
}
