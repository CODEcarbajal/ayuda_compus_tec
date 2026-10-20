package com.carbajal.ayudacampus.ui.components.graficos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carbajal.ayudacampus.ui.theme.ColorSoporte

@Composable
fun GraficoBarras(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier
) {
    val valorMaximo = (datos.maxOfOrNull { it.valor } ?: 1f).coerceAtLeast(1f)
    val alturaMaxBarra = 90.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        datos.forEach { dato ->
            val fraccion = (dato.valor / valorMaximo).coerceIn(0.1f, 1f)
            val alturaCalculada = alturaMaxBarra * fraccion

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                // Número superior encima de la barra
                Text(
                    text = dato.valor.toInt().toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Barra morada
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(alturaCalculada)
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        .background(ColorSoporte)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Etiqueta corta de la categoría
                Text(
                    text = obtenerEtiquetaCorta(dato.nombre),
                    fontSize = 11.sp,
                    color = Color(0xFF6B7280),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun obtenerEtiquetaCorta(nombre: String): String {
    return when {
        nombre.contains("Hardware", ignoreCase = true) -> "Hardware"
        nombre.contains("Software", ignoreCase = true) -> "Software"
        nombre.contains("Red", ignoreCase = true) || nombre.contains("Wi-Fi", ignoreCase = true) -> "Red"
        nombre.contains("Cuenta", ignoreCase = true) -> "Cuenta"
        nombre.contains("Impresión", ignoreCase = true) -> "Impresión"
        else -> nombre.take(8)
    }
}
