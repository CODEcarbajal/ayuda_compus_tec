package com.carbajal.ayudacampus.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Estrellas(
    calificacion: Int,
    onCalificar: (Int) -> Unit,
    modifier: Modifier = Modifier,
    deshabilitado: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            (1..5).forEach { estrella ->
                val estaLleno = estrella <= calificacion
                Icon(
                    imageVector = if (estaLleno) Icons.Default.Star else Icons.Outlined.Star,
                    contentDescription = "Estrella $estrella",
                    tint = if (estaLleno) Color(0xFFF59E0B) else Color(0xFFE5E7EB),
                    modifier = Modifier
                        .size(36.dp)
                        .clickable(enabled = !deshabilitado) { onCalificar(estrella) }
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (calificacion > 0) "$calificacion de 5 estrellas" else "Selecciona una calificación",
            fontSize = 13.sp,
            color = Color(0xFF6B7280)
        )
    }
}
