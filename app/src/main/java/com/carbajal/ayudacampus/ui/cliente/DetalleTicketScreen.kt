package com.carbajal.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carbajal.ayudacampus.data.local.entity.Usuario
import com.carbajal.ayudacampus.model.EstadoTicket
import com.carbajal.ayudacampus.ui.components.BurbujaComentario
import com.carbajal.ayudacampus.ui.components.EstadoChip
import com.carbajal.ayudacampus.ui.components.Estrellas
import com.carbajal.ayudacampus.ui.components.PrioridadChip
import com.carbajal.ayudacampus.ui.components.StepperTicket
import com.carbajal.ayudacampus.ui.theme.ColorCliente
import com.carbajal.ayudacampus.viewmodel.TicketDetalleViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleTicketScreen(
    viewModel: TicketDetalleViewModel,
    usuarioActual: Usuario,
    onVolver: () -> Unit = {},
    onIrTickets: () -> Unit = {},
    onIrNuevoTicket: () -> Unit = {},
    onIrPerfil: () -> Unit = {}
) {
    val ticketConDetalle by viewModel.ticket.collectAsState()
    val nuevoComentario by viewModel.nuevoComentario.collectAsState()
    val calificacion by viewModel.calificacion.collectAsState()

    var comentarioFinal by remember { mutableStateOf("") }

    if (ticketConDetalle == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Cargando detalle del ticket...", color = Color(0xFF6B7280))
        }
        return
    }

    val item = ticketConDetalle!!
    val ticket = item.ticket
    val esCerrado = ticket.estado == EstadoTicket.CERRADO.name
    val esResuelto = ticket.estado == EstadoTicket.RESUELTO.name

    val idFormateado = String.format(Locale.getDefault(), "#%04d", ticket.id)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Ticket $idFormateado",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF111827)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF111827)
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            // Barra inferior de navegación
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Opción Tickets
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onIrTickets() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Segment,
                            contentDescription = "Tickets",
                            tint = ColorCliente,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Tickets",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorCliente
                        )
                    }

                    // Opción Nuevo
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onIrNuevoTicket() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Nuevo",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Nuevo",
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }

                    // Opción Perfil
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onIrPerfil() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Perfil",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Perfil",
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFAFAFA))
                .padding(horizontal = 20.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                // Tarjeta de Cabecera del Ticket
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = ticket.titulo,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    EstadoChip(estadoNombre = ticket.estado)
                                    PrioridadChip(prioridadNombre = ticket.prioridad)
                                }

                                Text(
                                    text = formatearTiempoRelativoDetalle(ticket.fecha),
                                    fontSize = 12.sp,
                                    color = Color(0xFF9CA3AF)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = obtenerIconoCategoriaDetalle(item.categoria.icono),
                                    contentDescription = item.categoria.nombre,
                                    tint = Color(0xFF6B7280),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${item.categoria.nombre} · ${ticket.ubicacion}",
                                    fontSize = 13.sp,
                                    color = Color(0xFF6B7280)
                                )
                            }
                        }
                    }
                }

                // Banner de estado resuelto (si aplica)
                if (esResuelto) {
                    item {
                        Surface(
                            color = Color(0xFFDCFCE7),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Resuelto",
                                    tint = Color(0xFF16A34A),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Soporte marcó este ticket como resuelto. ¿El problema se solucionó?",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF16A34A),
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                } else if (!esCerrado) {
                    // Stepper de progreso cuando no está resuelto
                    item {
                        EstadoTicket.entries.find { it.name == ticket.estado }?.let { est ->
                            StepperTicket(estadoActual = est)
                        }
                    }
                }

                // Título de Sección Conversación
                item {
                    Text(
                        text = "Conversación",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                }

                // Burbujas de Comentarios (Filtra notas internas para el estudiante)
                items(
                    items = item.comentarios.filter { !it.esInterno },
                    key = { it.id }
                ) { comentario ->
                    val esMio = comentario.autorId == usuarioActual.id
                    BurbujaComentario(
                        comentario = comentario,
                        esMio = esMio
                    )
                }

                // Tarjeta para Calificar (si está RESUELTO)
                if (esResuelto) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Califica la atención",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF111827)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Estrellas(
                                    calificacion = calificacion,
                                    onCalificar = { viewModel.seleccionarCalificacion(it) }
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                OutlinedTextField(
                                    value = comentarioFinal,
                                    onValueChange = { comentarioFinal = it },
                                    label = { Text("Comentario final (opcional)") },
                                    placeholder = { Text("Ej. Atención rápida, gracias.") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ColorCliente,
                                        unfocusedBorderColor = Color(0xFFE5E7EB),
                                        focusedLabelColor = ColorCliente,
                                        unfocusedLabelColor = Color(0xFF6B7280)
                                    )
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        if (calificacion > 0) {
                                            viewModel.cerrarConCalificacion(comentarioFinal, usuarioActual)
                                        }
                                    },
                                    enabled = calificacion > 0,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = ColorCliente,
                                        contentColor = Color.White,
                                        disabledContainerColor = ColorCliente.copy(alpha = 0.5f),
                                        disabledContentColor = Color.White.copy(alpha = 0.7f)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                ) {
                                    Text(
                                        text = "Confirmar y cerrar",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedButton(
                                    onClick = { viewModel.reabrir() },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.White,
                                        contentColor = Color(0xFFDC2626)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFDC2626))
                                ) {
                                    Text(
                                        text = "Reabrir ticket",
                                        color = Color(0xFFDC2626),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Si está CERRADO y tiene calificación
                if (esCerrado && ticket.calificacion != null) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Atención finalizada",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color(0xFF111827)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Estrellas(
                                    calificacion = ticket.calificacion,
                                    onCalificar = {},
                                    deshabilitado = true
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            // Campo inferior para enviar nuevos comentarios (Oculto si está CERRADO o RESUELTO)
            if (!esCerrado && !esResuelto) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {
                    OutlinedTextField(
                        value = nuevoComentario,
                        onValueChange = { viewModel.actualizarNuevoComentario(it) },
                        placeholder = { Text("Escribe un comentario...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = ColorCliente,
                            unfocusedBorderColor = Color(0xFFE5E7EB)
                        )
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                if (nuevoComentario.isNotBlank()) ColorCliente else Color(0xFFE5E7EB)
                            )
                            .clickable(enabled = nuevoComentario.isNotBlank()) {
                                viewModel.agregarComentario(usuarioActual)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Enviar",
                            tint = if (nuevoComentario.isNotBlank()) Color.White else Color(0xFF9CA3AF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun obtenerIconoCategoriaDetalle(icono: String): ImageVector {
    return when (icono.lowercase()) {
        "wifi" -> Icons.Default.Wifi
        "pc", "computer" -> Icons.Default.DesktopWindows
        "printer" -> Icons.Default.Print
        "code" -> Icons.Default.Code
        "key" -> Icons.Default.VpnKey
        else -> Icons.Default.ConfirmationNumber
    }
}

private fun formatearTiempoRelativoDetalle(fechaMs: Long): String {
    val ahora = System.currentTimeMillis()
    val diffMs = ahora - fechaMs
    val diffHoras = diffMs / (1000 * 3600)
    val diffDias = diffHoras / 24

    return when {
        diffHoras < 1 -> "Hace 1 min"
        diffHoras < 24 -> "Hace $diffHoras h"
        diffDias == 1L -> "Ayer"
        else -> "Hace $diffDias d"
    }
}
