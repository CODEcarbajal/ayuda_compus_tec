package com.carbajal.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carbajal.ayudacampus.data.prefs.PreferenciasStore
import com.carbajal.ayudacampus.data.repository.AyudaRepository

class AyudaViewModelFactory(
    private val repository: AyudaRepository,
    private val preferenciasStore: PreferenciasStore,
    private val clienteId: Int = 1,
    private val ticketId: Int = 1
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SesionViewModel::class.java)) {
            return SesionViewModel(repository, preferenciasStore) as T
        }
        if (modelClass.isAssignableFrom(MisTicketsViewModel::class.java)) {
            return MisTicketsViewModel(clienteId, repository) as T
        }
        if (modelClass.isAssignableFrom(NuevoTicketViewModel::class.java)) {
            return NuevoTicketViewModel(clienteId, repository) as T
        }
        if (modelClass.isAssignableFrom(TicketDetalleViewModel::class.java)) {
            return TicketDetalleViewModel(ticketId, repository) as T
        }
        if (modelClass.isAssignableFrom(PanelViewModel::class.java)) {
            return PanelViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(BandejaViewModel::class.java)) {
            return BandejaViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(AtenderViewModel::class.java)) {
            return AtenderViewModel(ticketId, repository) as T
        }
        if (modelClass.isAssignableFrom(CategoriasViewModel::class.java)) {
            return CategoriasViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida: ${modelClass.name}")
    }
}
