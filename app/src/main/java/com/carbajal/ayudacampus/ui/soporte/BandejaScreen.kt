package com.carbajal.ayudacampus.ui.soporte

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carbajal.ayudacampus.model.EstadoTicket
import com.carbajal.ayudacampus.ui.components.TicketCard
import com.carbajal.ayudacampus.ui.theme.ColorSoporte
import com.carbajal.ayudacampus.viewmodel.BandejaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandejaScreen(
    viewModel: BandejaViewModel,
    onVolver: (() -> Unit)? = null,
    onAtenderTicket: (Int) -> Unit = {},
    onIrPanel: () -> Unit = {},
    onIrCategorias: () -> Unit = {},
    onIrAjustes: () -> Unit = {}
) {
    val tickets by viewModel.tickets.collectAsState()
    val estadoFiltro by viewModel.estadoFiltro.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Tickets",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF111827)
                        )
                    }
                },
                navigationIcon = {
                    if (onVolver != null) {
                        IconButton(onClick = onVolver) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color(0xFF111827)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            // Barra inferior de navegación (Soporte TI)
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
                    // Panel
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
                        Text(
                            text = "Panel",
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }

                    // Tickets (Bandeja Activa)
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
                        Text(
                            text = "Tickets",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorSoporte
                        )
                    }

                    // Categorías
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
                        Text(
                            text = "Categorías",
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }

                    // Ajustes
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
                        Text(
                            text = "Ajustes",
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
            Spacer(modifier = Modifier.height(8.dp))

            // Chips de filtrado
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FiltroChipSoportePill(
                        texto = "Todos",
                        seleccionado = estadoFiltro == null,
                        onClick = { viewModel.seleccionarFiltro(null) }
                    )
                }
                item {
                    FiltroChipSoportePill(
                        texto = "Abiertos",
                        seleccionado = estadoFiltro == EstadoTicket.ABIERTO,
                        onClick = { viewModel.seleccionarFiltro(EstadoTicket.ABIERTO) }
                    )
                }
                item {
                    FiltroChipSoportePill(
                        texto = "En proceso",
                        seleccionado = estadoFiltro == EstadoTicket.EN_PROCESO,
                        onClick = { viewModel.seleccionarFiltro(EstadoTicket.EN_PROCESO) }
                    )
                }
                item {
                    FiltroChipSoportePill(
                        texto = "Resueltos",
                        seleccionado = estadoFiltro == EstadoTicket.RESUELTO,
                        onClick = { viewModel.seleccionarFiltro(EstadoTicket.RESUELTO) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (tickets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay tickets en esta sección",
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
                            onClick = { onAtenderTicket(item.ticket.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FiltroChipSoportePill(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (seleccionado) ColorSoporte else Color.White)
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
