package com.carbajal.ayudacampus.ui.navigation

object Rutas {
    const val LOGIN = "login"
    const val MIS_TICKETS = "mis_tickets"
    const val NUEVO_TICKET = "nuevo_ticket"
    const val DETALLE_TICKET = "detalle_ticket/{ticketId}"
    const val PERFIL = "perfil"
    const val BANDEJA = "bandeja"
    const val PANEL_SOPORTE = "panel_soporte"
    const val ATENDER_TICKET = "atender_ticket/{ticketId}"
    const val CATEGORIAS = "categorias"
    const val AJUSTES = "ajustes"

    fun crearDetalleTicket(ticketId: Int) = "detalle_ticket/$ticketId"
    fun crearAtenderTicket(ticketId: Int) = "atender_ticket/$ticketId"
}
