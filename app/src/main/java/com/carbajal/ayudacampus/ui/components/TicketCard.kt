package com.carbajal.ayudacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carbajal.ayudacampus.data.local.relation.TicketConDetalle
import com.carbajal.ayudacampus.model.EstadoTicket
import com.carbajal.ayudacampus.model.Prioridad
import com.carbajal.ayudacampus.ui.theme.ColorCliente

@Composable
fun TicketCard(
    ticketConDetalle: TicketConDetalle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ticket = ticketConDetalle.ticket
    val categoria = ticketConDetalle.categoria

    // Color de la barra vertical lateral según la prioridad o estado
    val colorBarraLateral = when {
        ticket.estado.equals(EstadoTicket.RESUELTO.name, ignoreCase = true) -> Color(0xFF9CA3AF)
        ticket.prioridad.equals(Prioridad.ALTA.name, ignoreCase = true) -> Color(0xFFDC2626)
        ticket.prioridad.equals(Prioridad.MEDIA.name, ignoreCase = true) -> Color(0xFFEA580C)
        else -> Color(0xFF9CA3AF)
    }

    // Selección de ícono por categoría
    val iconoCategoria: ImageVector = when (categoria.icono.lowercase()) {
        "wifi" -> Icons.Default.Wifi
        "pc", "computer" -> Icons.Default.DesktopWindows
        "printer" -> Icons.Default.Print
        "code" -> Icons.Default.Code
        "key" -> Icons.Default.VpnKey
        else -> Icons.Default.ConfirmationNumber
    }

    val idFormateado = String.format(java.util.Locale.getDefault(), "#%04d", ticket.id)
    val tiempoTexto = formatearTiempoRelativo(ticket.fecha)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Barra vertical indicadora a la izquierda
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(colorBarraLateral)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ícono de la categoría en un recuadro azul claro
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE0F2FE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconoCategoria,
                        contentDescription = categoria.nombre,
                        tint = ColorCliente,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Información del ticket
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = ticket.titulo,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "$idFormateado · ${ticket.ubicacion}",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Fila de Chips y Tiempo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        EstadoChip(estadoNombre = ticket.estado)

                        Spacer(modifier = Modifier.width(6.dp))

                        PrioridadChip(prioridadNombre = ticket.prioridad)

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = tiempoTexto,
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }
            }
        }
    }
}

fun formatearTiempoRelativo(fechaMs: Long): String {
    val ahora = System.currentTimeMillis()
    val diffMs = ahora - fechaMs
    val diffMin = diffMs / (1000 * 60)
    val diffHoras = diffMin / 60
    val diffDias = diffHoras / 24

    return when {
        diffMin < 1 -> "Hace 1 min"
        diffMin < 60 -> "Hace $diffMin min"
        diffHoras < 24 -> "Hace $diffHoras h"
        diffDias == 1L -> "Ayer"
        else -> "Hace $diffDias d"
    }
}
