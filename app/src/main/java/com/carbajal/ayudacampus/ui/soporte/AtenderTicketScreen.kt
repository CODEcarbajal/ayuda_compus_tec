package com.carbajal.ayudacampus.ui.soporte

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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carbajal.ayudacampus.data.local.entity.Usuario
import com.carbajal.ayudacampus.model.EstadoTicket
import com.carbajal.ayudacampus.model.Prioridad
import com.carbajal.ayudacampus.model.Rol
import com.carbajal.ayudacampus.ui.components.BurbujaComentario
import com.carbajal.ayudacampus.ui.theme.ColorSoporte
import com.carbajal.ayudacampus.viewmodel.AtenderViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtenderTicketScreen(
    viewModel: AtenderViewModel,
    usuarioSoporte: Usuario,
    onVolver: () -> Unit = {},
    onIrPanel: () -> Unit = {},
    onIrCategorias: () -> Unit = {},
    onIrAjustes: () -> Unit = {}
) {
    val ticketConDetalle by viewModel.ticket.collectAsState()
    val estadoSeleccionado by viewModel.estadoSeleccionado.collectAsState()
    val prioridadSeleccionada by viewModel.prioridadSeleccionada.collectAsState()
    val nuevoRespuesta by viewModel.nuevoRespuesta.collectAsState()

    if (ticketConDetalle == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Cargando información del ticket...", color = Color(0xFF6B7280))
        }
        return
    }

    val item = ticketConDetalle!!
    val ticket = item.ticket

    // Inicializar estado y prioridad en el ViewModel
    LaunchedEffect(ticket) {
        viewModel.inicializarValores(ticket.estado, ticket.prioridad)
    }

    val idFormateado = String.format(Locale.getDefault(), "#%04d", ticket.id)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Atender ticket $idFormateado",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
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
            // Barra inferior Soporte TI
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onIrPanel() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Panel",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Panel", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Segment,
                            contentDescription = "Tickets",
                            tint = ColorSoporte,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Tickets", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorSoporte)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onIrCategorias() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocalOffer,
                            contentDescription = "Categorías",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Categorías", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onIrAjustes() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Ajustes",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Ajustes", fontSize = 12.sp, color = Color(0xFF9CA3AF))
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
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${item.cliente.nombre} · ${ticket.ubicacion} · ${item.categoria.nombre}",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = ticket.descripcion,
                                fontSize = 13.sp,
                                color = Color(0xFF374151),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                // Sección Selector de Estado
                item {
                    Text(
                        text = "Estado",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EstadoTicket.entries.forEach { est ->
                            val seleccionado = estadoSeleccionado == est
                            val (fondo, textoColor, label) = when (est) {
                                EstadoTicket.ABIERTO -> Triple(Color(0xFF3B82F6), Color.White, "Abierto")
                                EstadoTicket.EN_PROCESO -> Triple(Color(0xFF2563EB), Color.White, "En proceso")
                                EstadoTicket.RESUELTO -> Triple(Color(0xFF16A34A), Color.White, "Resuelto")
                                EstadoTicket.CERRADO -> Triple(Color(0xFF4B5563), Color.White, "Cerrado")
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (seleccionado) fondo else Color.White)
                                    .border(
                                        width = if (seleccionado) 0.dp else 1.dp,
                                        color = if (seleccionado) Color.Transparent else Color(0xFFE5E7EB),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .clickable { viewModel.seleccionarEstado(est) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    color = if (seleccionado) textoColor else Color(0xFF4B5563),
                                    fontSize = 12.sp,
                                    fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Sección Selector de Prioridad
                item {
                    Text(
                        text = "Prioridad",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Prioridad.entries.forEach { pri ->
                            val seleccionada = prioridadSeleccionada == pri
                            val (fondo, label) = when (pri) {
                                Prioridad.BAJA -> Pair(Color(0xFF10B981), "Baja")
                                Prioridad.MEDIA -> Pair(Color(0xFFF59E0B), "Media")
                                Prioridad.ALTA -> Pair(Color(0xFFDC2626), "Alta")
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (seleccionada) fondo else Color.White)
                                    .border(
                                        width = if (seleccionada) 0.dp else 1.dp,
                                        color = if (seleccionada) Color.Transparent else Color(0xFFE5E7EB),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { viewModel.seleccionarPrioridad(pri) }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    color = if (seleccionada) Color.White else Color(0xFF4B5563),
                                    fontSize = 13.sp,
                                    fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Conversación
                item {
                    Text(
                        text = "Conversación",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                }

                items(
                    items = item.comentarios,
                    key = { it.id }
                ) { com ->
                    val esSoporte = com.autorRol == Rol.ADMIN.name
                    BurbujaComentario(
                        comentario = com,
                        esMio = esSoporte
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }
            }

            // Input Responder al cliente
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = nuevoRespuesta,
                    onValueChange = { viewModel.actualizarRespuesta(it) },
                    placeholder = { Text("Responder al cliente...", color = Color(0xFF9CA3AF), fontSize = 14.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = ColorSoporte,
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    )
                )

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (nuevoRespuesta.isNotBlank()) ColorSoporte else Color(0xFFE5E7EB)
                        )
                        .clickable(enabled = nuevoRespuesta.isNotBlank()) {
                            viewModel.enviarRespuesta(usuarioSoporte)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar",
                        tint = if (nuevoRespuesta.isNotBlank()) Color.White else Color(0xFF9CA3AF),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Botón Guardar Cambios
            Button(
                onClick = { viewModel.guardarCambios(onExito = onVolver) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorSoporte,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Guardar cambios",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
