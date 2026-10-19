package com.carbajal.ayudacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carbajal.ayudacampus.model.Prioridad

@Composable
fun PrioridadChip(
    prioridadNombre: String,
    modifier: Modifier = Modifier
) {
    val (fondoColor, textoColor, textoMostrar) = when (prioridadNombre.uppercase()) {
        Prioridad.ALTA.name -> Triple(Color(0xFFFEE2E2), Color(0xFFDC2626), "Alta")
        Prioridad.MEDIA.name -> Triple(Color(0xFFFFEDD5), Color(0xFFEA580C), "Media")
        Prioridad.BAJA.name -> Triple(Color(0xFFF3F4F6), Color(0xFF4B5563), "Baja")
        else -> Triple(Color(0xFFF3F4F6), Color(0xFF4B5563), prioridadNombre)
    }

    Box(
        modifier = modifier
            .background(color = fondoColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = textoMostrar,
            color = textoColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
