package com.carbajal.ayudacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carbajal.ayudacampus.data.local.entity.Comentario
import com.carbajal.ayudacampus.ui.theme.ColorCliente
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BurbujaComentario(
    comentario: Comentario,
    esMio: Boolean,
    modifier: Modifier = Modifier
) {
    val alineacion = if (esMio) Alignment.End else Alignment.Start
    val fondoColor = if (esMio) ColorCliente else Color(0xFFF3EFFE)
    val textoColor = if (esMio) Color.White else Color(0xFF1F2937)
    val tituloHeader = if (esMio) "Tú" else comentario.autorNombre

    val horaFormateada = formatearHoraOFecha(comentario.fecha)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = alineacion
    ) {
        // Cabecera con Autor y Hora
        Text(
            text = "$tituloHeader · $horaFormateada",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp, end = 4.dp)
        )

        // Burbuja de texto
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (esMio) 16.dp else 4.dp,
                        bottomEnd = if (esMio) 4.dp else 16.dp
                    )
                )
                .background(fondoColor)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = comentario.texto,
                color = textoColor,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

private fun formatearHoraOFecha(fechaMs: Long): String {
    val ahora = System.currentTimeMillis()
    val diffHoras = (ahora - fechaMs) / (1000 * 3600)

    return if (diffHoras < 24) {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.format(Date(fechaMs))
    } else if (diffHoras < 48) {
        "ayer"
    } else {
        val diffDias = diffHoras / 24
        "hace $diffDias d"
    }
}
