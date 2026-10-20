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
import com.carbajal.ayudacampus.model.SlaInfo
import com.carbajal.ayudacampus.ui.theme.ColorCliente

@Composable
fun SlaChip(
    slaInfo: SlaInfo,
    modifier: Modifier = Modifier
) {
    val (fondoColor, textoColor) = when {
        slaInfo.textoFormateado == "SLA: Finalizado" -> Pair(Color(0xFFF3F4F6), Color(0xFF6B7280))
        slaInfo.expirado -> Pair(Color(0xFFFEE2E2), Color(0xFFDC2626))
        else -> Pair(Color(0xFFE0F2FE), ColorCliente)
    }

    Box(
        modifier = modifier
            .background(color = fondoColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = slaInfo.textoFormateado,
            color = textoColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
