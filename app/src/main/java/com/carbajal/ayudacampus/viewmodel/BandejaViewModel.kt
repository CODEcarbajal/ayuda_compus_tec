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


class BandejaViewModel(
    private val repository: AyudaRepository
) : ViewModel() {

    private val _estadoFiltro = MutableStateFlow<EstadoTicket?>(null) // null = Todos
    val estadoFiltro: StateFlow<EstadoTicket?> = _estadoFiltro.asStateFlow()

    val tickets: StateFlow<List<TicketConDetalle>> = combine(
        repository.observarTodosLosTickets(),
        _estadoFiltro
    ) { lista, filtro ->
        if (filtro == null) lista else lista.filter { it.ticket.estado == filtro.name }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun seleccionarFiltro(estado: EstadoTicket?) {
        _estadoFiltro.value = estado
    }
}
