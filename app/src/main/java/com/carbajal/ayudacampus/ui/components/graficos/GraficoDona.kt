package com.carbajal.ayudacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GraficoDona(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier
) {
    val colores = listOf(
        Color(0xFFD97706), // Abierto: Naranja
        Color(0xFF2563EB), // En proceso: Azul
        Color(0xFF16A34A), // Resuelto: Verde
        Color(0xFF4B5563)  // Cerrado: Gris
    )

    val total = datos.sumOf { it.valor.toDouble() }.toFloat()
    val totalInt = total.toInt()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Gráfico Dona dibujado con Canvas
        Box(
            modifier = Modifier.size(130.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(120.dp)) {
                if (total <= 0f) {
                    drawArc(
                        color = Color(0xFFE5E7EB),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 24.dp.toPx(), cap = StrokeCap.Butt)
                    )
                } else {
                    var startAngle = -90f
                    datos.forEachIndexed { index, dato ->
                        val sweepAngle = (dato.valor / total) * 360f
                        if (sweepAngle > 0) {
                            val color = colores.getOrElse(index) { Color.Gray }
                            drawArc(
                                color = color,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                style = Stroke(width = 24.dp.toPx(), cap = StrokeCap.Butt)
                            )
                            startAngle += sweepAngle
                        }
                    }
                }
            }

            // Texto central dentro de la dona
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = totalInt.toString(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "tickets",
                    fontSize = 11.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Leyenda a la derecha
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            datos.forEachIndexed { index, dato ->
                val color = colores.getOrElse(index) { Color.Gray }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(color, RoundedCornerShape(2.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = dato.nombre,
                            fontSize = 13.sp,
                            color = Color(0xFF374151),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = dato.valor.toInt().toString(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                }
            }
        }
    }
}
