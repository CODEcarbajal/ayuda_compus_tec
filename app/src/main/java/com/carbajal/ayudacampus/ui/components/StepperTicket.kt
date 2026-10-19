package com.carbajal.ayudacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carbajal.ayudacampus.model.EstadoTicket
import com.carbajal.ayudacampus.ui.theme.ColorCliente

@Composable
fun StepperTicket(
    estadoActual: EstadoTicket,
    modifier: Modifier = Modifier
) {
    val pasos = listOf(
        EstadoTicket.ABIERTO to "Abierto",
        EstadoTicket.EN_PROCESO to "En proceso",
        EstadoTicket.RESUELTO to "Resuelto",
        EstadoTicket.CERRADO to "Cerrado"
    )

    val indiceActual = pasos.indexOfFirst { it.first == estadoActual }.coerceAtLeast(0)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            pasos.forEachIndexed { index, pair ->
                val esCompletado = index < indiceActual
                val esActual = index == indiceActual

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Línea conectora izquierda
                        if (index > 0) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(3.dp)
                                    .background(
                                        if (index <= indiceActual) ColorCliente else Color(0xFFE5E7EB)
                                    )
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        // Círculo del paso
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = when {
                                        esCompletado -> Color(0xFF10B981)
                                        esActual -> ColorCliente
                                        else -> Color.White
                                    },
                                    shape = CircleShape
                                )
                                .border(
                                    width = if (esCompletado || esActual) 0.dp else 2.dp,
                                    color = if (esCompletado || esActual) Color.Transparent else Color(0xFFD1D5DB),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (esCompletado) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            } else if (esActual) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Color.White, CircleShape)
                                )
                            }
                        }

                        // Línea conectora derecha
                        if (index < pasos.size - 1) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(3.dp)
                                    .background(
                                        if (index < indiceActual) ColorCliente else Color(0xFFE5E7EB)
                                    )
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = pair.second,
                        fontSize = 11.sp,
                        fontWeight = if (esActual || esCompletado) FontWeight.Bold else FontWeight.Medium,
                        color = if (esActual || esCompletado) Color(0xFF111827) else Color(0xFF9CA3AF)
                    )
                }
            }
        }
    }
}
