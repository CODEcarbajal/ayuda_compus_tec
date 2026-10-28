package com.carbajal.ayudacampus.model

internal fun validarHorarioAtencion(inicio: Long, fin: Long, lugar: String, ahora: Long) {
    require(inicio > ahora) { "Selecciona una fecha y hora futuras" }
    require(fin > inicio && lugar.isNotBlank()) { "Completa el lugar y una duración válida" }
}
