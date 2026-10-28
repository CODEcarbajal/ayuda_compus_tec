package com.carbajal.ayudacampus.ui.cliente

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carbajal.ayudacampus.AyudaApp
import com.carbajal.ayudacampus.model.EstadoTicket
import com.carbajal.ayudacampus.ui.components.TicketCard
import com.carbajal.ayudacampus.ui.theme.ColorCliente
import com.carbajal.ayudacampus.viewmodel.MisTicketsViewModel

@Composable
fun MisTicketsScreen(
    notificacionesPendientes: Int = 0,
    onIrNotificaciones: () -> Unit = {},
    viewModel: MisTicketsViewModel,
    nombreEstudiante: String = "Meliton",
    onCrearTicket: () -> Unit = {},
    onVerDetalle: (Int) -> Unit = {},
    onIrPerfil: () -> Unit = {}
) {
    val context = LocalContext.current
    val app = context.applicationContext as AyudaApp
    val recibirTickets by app.preferenciasStore.recibirTickets.collectAsState(initial = true)

    val tickets by viewModel.tickets.collectAsState()
    val estadoFiltro by viewModel.estadoFiltro.collectAsState()
    val textoBusqueda by viewModel.textoBusqueda.collectAsState()
    val cantidadAbiertos by viewModel.cantidadAbiertos.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (recibirTickets) {
                        onCrearTicket()
                    } else {
                        Toast.makeText(context, "Mesa de ayuda cerrada", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = CircleShape,
                containerColor = if (recibirTickets) ColorCliente else Color(0xFF9CA3AF),
                contentColor = Color.White,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear Ticket",
                    modifier = Modifier.size(28.dp)
                )
            }
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
                    // Opción Tickets (Activa)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { }
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
                        modifier = Modifier.clickable {
                            if (recibirTickets) {
                                onCrearTicket()
                            } else {
                                Toast.makeText(context, "Mesa de ayuda cerrada", Toast.LENGTH_SHORT).show()
                            }
                        }
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
            Spacer(modifier = Modifier.height(16.dp))

            // Cabecera de Saludo y Notificación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Mis tickets",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Hola, $nombreEstudiante · $cantidadAbiertos abiertos",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                com.carbajal.ayudacampus.ui.components.CampanaNotificaciones(
                    pendientes = notificacionesPendientes, onClick = onIrNotificaciones
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Banner Aviso Mesa de ayuda cerrada
            if (!recibirTickets) {
                Surface(
                    color = Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Aviso",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Mesa de ayuda cerrada",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF991B1B)
                        )
                    }
                }
            }

            // Buscador Redondeado
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { viewModel.actualizarBusqueda(it) },
                placeholder = {
                    Text(
                        text = "Buscar por título o número...",
                        color = Color(0xFF9CA3AF),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color(0xFF9CA3AF)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = ColorCliente,
                    unfocusedBorderColor = Color(0xFFE5E7EB)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filtros de Estado (Chips horizontales)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FiltroChipPill(
                        texto = "Todos",
                        seleccionado = estadoFiltro == null,
                        onClick = { viewModel.seleccionarFiltro(null) }
                    )
                }
                item {
                    FiltroChipPill(
                        texto = "Abiertos",
                        seleccionado = estadoFiltro == EstadoTicket.ABIERTO,
                        onClick = { viewModel.seleccionarFiltro(EstadoTicket.ABIERTO) }
                    )
                }
                item {
                    FiltroChipPill(
                        texto = "En proceso",
                        seleccionado = estadoFiltro == EstadoTicket.EN_PROCESO,
                        onClick = { viewModel.seleccionarFiltro(EstadoTicket.EN_PROCESO) }
                    )
                }
                item {
                    FiltroChipPill(
                        texto = "Resueltos",
                        seleccionado = estadoFiltro == EstadoTicket.RESUELTO,
                        onClick = { viewModel.seleccionarFiltro(EstadoTicket.RESUELTO) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Tickets o Estado Vacío
            if (tickets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tienes tickets aquí",
                        fontSize = 15.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(
                        items = tickets,
                        key = { it.ticket.id }
                    ) { item ->
                        TicketCard(
                            ticketConDetalle = item,
                            onClick = { onVerDetalle(item.ticket.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FiltroChipPill(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (seleccionado) ColorCliente else Color.White)
            .border(
                width = if (seleccionado) 0.dp else 1.dp,
                color = if (seleccionado) Color.Transparent else Color(0xFFE5E7EB),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = if (seleccionado) Color.White else Color(0xFF4B5563),
            fontSize = 13.sp,
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium
        )
    }
}
