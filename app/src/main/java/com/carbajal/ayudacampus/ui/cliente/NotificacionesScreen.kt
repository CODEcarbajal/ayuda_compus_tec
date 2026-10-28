package com.carbajal.ayudacampus.ui.cliente

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.carbajal.ayudacampus.data.local.entity.Notificacion
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesScreen(
    notificaciones: List<Notificacion>,
    onVolver: () -> Unit,
    onAbrir: (Notificacion) -> Unit,
    onMarcarTodas: () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text("Notificaciones") }, navigationIcon = {
            IconButton(onClick = onVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") }
        })
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
            val pendientes = notificaciones.count { !it.leida }
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text("$pendientes sin leer")
                TextButton(onClick = onMarcarTodas, enabled = pendientes > 0) { Text("Marcar todas leídas") }
            }
            if (notificaciones.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tienes notificaciones.\nAquí verás los cambios de estado de tus tickets.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)) {
                    items(notificaciones, key = { it.id }) { aviso ->
                        Card(colors = CardDefaults.cardColors(containerColor = if (aviso.leida)
                            MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth().clickable { onAbrir(aviso) }) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("#${aviso.ticketId} · ${aviso.tituloTicket}", fontWeight = FontWeight.Bold)
                                Text(aviso.mensaje)
                                Text(SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(aviso.fecha)),
                                    style = MaterialTheme.typography.bodySmall)
                                if (!aviso.leida) Text("Sin leer", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
