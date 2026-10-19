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
import com.carbajal.ayudacampus.model.EstadoTicket

@Composable
fun EstadoChip(
    estadoNombre: String,
    modifier: Modifier = Modifier
) {
    val (fondoColor, textoColor, textoMostrar) = when (estadoNombre.uppercase()) {
        EstadoTicket.EN_PROCESO.name -> Triple(Color(0xFFE0F2FE), Color(0xFF0284C7), "En proceso")
        EstadoTicket.ABIERTO.name -> Triple(Color(0xFFFEF3C7), Color(0xFFD97706), "Abierto")
        EstadoTicket.RESUELTO.name -> Triple(Color(0xFFDCFCE7), Color(0xFF16A34A), "Resuelto")
        EstadoTicket.CERRADO.name -> Triple(Color(0xFFF3F4F6), Color(0xFF4B5563), "Cerrado")
        else -> Triple(Color(0xFFF3F4F6), Color(0xFF4B5563), estadoNombre)
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
