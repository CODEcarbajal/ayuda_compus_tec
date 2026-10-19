package com.carbajal.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbajal.ayudacampus.data.local.relation.TicketConDetalle
import com.carbajal.ayudacampus.data.repository.AyudaRepository
import com.carbajal.ayudacampus.model.EstadoTicket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MisTicketsViewModel(
    private val clienteId: Int,
    private val repository: AyudaRepository
) : ViewModel() {

    private val _estadoFiltro = MutableStateFlow<EstadoTicket?>(null) // null representa "Todos"
    val estadoFiltro: StateFlow<EstadoTicket?> = _estadoFiltro.asStateFlow()

    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda: StateFlow<String> = _textoBusqueda.asStateFlow()

    // Combina la lista de tickets del cliente con el estado del filtro y la búsqueda
    val tickets: StateFlow<List<TicketConDetalle>> = combine(
        repository.observarTicketsCliente(clienteId),
        _estadoFiltro,
        _textoBusqueda
    ) { lista, estado, busqueda ->
        lista.filter { item ->
            val coincideEstado = estado == null || item.ticket.estado == estado.name
            val query = busqueda.trim().lowercase()
            val coincideTexto = query.isEmpty() ||
                    item.ticket.titulo.lowercase().contains(query) ||
                    "#${item.ticket.id}".contains(query) ||
                    item.ticket.id.toString().contains(query)

            coincideEstado && coincideTexto
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Conteo de tickets abiertos para mostrar en la cabecera
    val cantidadAbiertos: StateFlow<Int> = repository.observarCantidadPorEstado(EstadoTicket.ABIERTO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    fun seleccionarFiltro(estado: EstadoTicket?) {
        _estadoFiltro.value = estado
    }

    fun actualizarBusqueda(texto: String) {
        _textoBusqueda.value = texto
    }
}