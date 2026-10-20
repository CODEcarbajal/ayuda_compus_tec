package com.carbajal.ayudacampus.model

data class SlaInfo(
    val limiteHoras: Int,
    val tiempoRestanteMs: Long,
    val expirado: Boolean,
    val textoFormateado: String
)

fun calcularSla(fechaTicketMs: Long, prioridadNombre: String, estadoNombre: String): SlaInfo {
    val limiteHoras = when (prioridadNombre.uppercase()) {
        Prioridad.ALTA.name -> 4
        Prioridad.MEDIA.name -> 24
        Prioridad.BAJA.name -> 72
        else -> 24
    }

    if (estadoNombre.equals(EstadoTicket.RESUELTO.name, ignoreCase = true) ||
        estadoNombre.equals(EstadoTicket.CERRADO.name, ignoreCase = true)
    ) {
        return SlaInfo(
            limiteHoras = limiteHoras,
            tiempoRestanteMs = 0L,
            expirado = false,
            textoFormateado = "SLA: Finalizado"
        )
    }

    val limiteMs = limiteHoras * 3600 * 1000L
    val fechaLimiteMs = fechaTicketMs + limiteMs
    val ahora = System.currentTimeMillis()
    val restanteMs = fechaLimiteMs - ahora

    return if (restanteMs <= 0) {
        val transcurridoExpiradoMs = ahora - fechaLimiteMs
        val minExpirados = transcurridoExpiradoMs / (1000 * 60)
        val horasExpiradas = minExpirados / 60
        val textoExpirado = if (horasExpiradas > 0) {
            "SLA vencido hace ${horasExpiradas}h"
        } else {
            "SLA vencido hace ${minExpirados.coerceAtLeast(1)}m"
        }
        SlaInfo(
            limiteHoras = limiteHoras,
            tiempoRestanteMs = restanteMs,
            expirado = true,
            textoFormateado = textoExpirado
        )
    } else {
        val minRestantes = restanteMs / (1000 * 60)
        val horasRestantes = minRestantes / 60
        val textoRestante = if (horasRestantes > 0) {
            "SLA: ${horasRestantes}h restantes"
        } else {
            "SLA: ${minRestantes.coerceAtLeast(1)}m restantes"
        }
        SlaInfo(
            limiteHoras = limiteHoras,
            tiempoRestanteMs = restanteMs,
            expirado = false,
            textoFormateado = textoRestante
        )
    }
}
