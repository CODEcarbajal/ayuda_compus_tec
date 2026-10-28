package com.carbajal.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbajal.ayudacampus.data.local.entity.CitaAtencion
import com.carbajal.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SoportePerfilViewModel(private val repository: AyudaRepository) : ViewModel() {
    val citas = repository.observarCitas().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val actividad = repository.observarActividad().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val tickets = repository.observarTodosLosTickets().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    private val _guardando = MutableStateFlow(false)
    val guardando = _guardando.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun limpiarError() { _error.value = null }

    fun programar(cita: CitaAtencion, onExito: () -> Unit) = ejecutar(onExito) {
        repository.programarCita(cita)
    }

    fun cambiarEstado(id: Int, estado: String) = ejecutar({}) {
        repository.cambiarEstadoCita(id, estado)
    }

    private fun ejecutar(onExito: () -> Unit, accion: suspend () -> Unit) {
        if (_guardando.value) return
        _guardando.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                accion()
                onExito()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = e.message ?: "No se pudo guardar. Intenta nuevamente."
            } finally {
                _guardando.value = false
            }
        }
    }
}
