package com.carbajal.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbajal.ayudacampus.data.repository.AyudaRepository
import com.carbajal.ayudacampus.model.EstadoTicket
import com.carbajal.ayudacampus.ui.components.graficos.DatoGrafico
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Locale

class PanelViewModel(
    private val repository: AyudaRepository
) : ViewModel() {

    // Contadores individuales
    val abiertos: StateFlow<Int> = repository.observarCantidadPorEstado(EstadoTicket.ABIERTO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val enProceso: StateFlow<Int> = repository.observarCantidadPorEstado(EstadoTicket.EN_PROCESO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val resueltos: StateFlow<Int> = repository.observarCantidadPorEstado(EstadoTicket.RESUELTO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val cerrados: StateFlow<Int> = repository.observarCantidadPorEstado(EstadoTicket.CERRADO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Datos mapeados para GraficoDona (Tickets por estado)
    val porEstado: StateFlow<List<DatoGrafico>> = combine(
        abiertos, enProceso, resueltos, cerrados
    ) { a, ep, r, c ->
        listOf(
            DatoGrafico("Abierto", a.toFloat()),
            DatoGrafico("En proceso", ep.toFloat()),
            DatoGrafico("Resuelto", r.toFloat()),
            DatoGrafico("Cerrado", c.toFloat())
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Datos mapeados para GraficoBarras (Tickets por categoría)
    val porCategoria: StateFlow<List<DatoGrafico>> = repository.observarCantidadPorCategoria()
        .map { lista ->
            lista.map { ConteoCategoria ->
                DatoGrafico(ConteoCategoria.categoria, ConteoCategoria.cantidad.toFloat())
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Tickets ALTA abiertos con más de 1 hora sin atender (haceHoras(1) = System.currentTimeMillis() - 3600000)
    val altasSinAtender: StateFlow<Int> = repository.observarAltasSinAtender(System.currentTimeMillis() - 3600000)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Tiempo promedio de resolución formateado
    val tiempoPromedioText: StateFlow<String> = repository.observarTiempoPromedioResolucion()
        .map { promedioMs ->
            if (promedioMs == null || promedioMs <= 0) {
                "N/A"
            } else {
                val horas = promedioMs / (1000.0 * 3600.0)
                if (horas < 1) {
                    val minutos = (promedioMs / (1000.0 * 60.0)).toInt()
                    "$minutos min"
                } else {
                    String.format(Locale.getDefault(), "%.1f hrs", horas)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "N/A")
}
