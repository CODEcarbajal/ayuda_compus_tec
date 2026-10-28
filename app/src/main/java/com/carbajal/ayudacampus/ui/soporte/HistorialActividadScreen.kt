package com.carbajal.ayudacampus.ui.soporte

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.carbajal.ayudacampus.ui.theme.ColorSoporte
import com.carbajal.ayudacampus.viewmodel.SoportePerfilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialActividadScreen(viewModel: SoportePerfilViewModel, onVolver: () -> Unit, onTicket: (Int) -> Unit) {
    val actividades by viewModel.actividad.collectAsState()
    var filtro by rememberSaveable { mutableStateOf("") }
    var busqueda by rememberSaveable { mutableStateOf("") }
    val tipos = linkedMapOf("" to "Todas", "CREACION" to "Creaciones", "COMENTARIO" to "Comentarios", "REAPERTURA" to "Reaperturas", "CALIFICACION" to "Calificaciones")
    val visibles = actividades.filter {
        (filtro.isEmpty() || it.tipo == filtro) && (busqueda.isBlank() ||
            "#${it.ticketId} ${it.tituloTicket} ${it.autorNombre} ${it.detalle}".contains(busqueda.trim(), ignoreCase = true))
    }
    Scaffold(topBar = { TopAppBar(title = { Text("Historial de actividad") }, navigationIcon = {
        IconButton(onClick = onVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") }
    }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
            Text("Actividad de las incidencias", style = MaterialTheme.typography.titleLarge)
            Text("Quién realizó cada acción y cuándo ocurrió.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedTextField(value = busqueda, onValueChange = { busqueda = it }, label = { Text("Buscar por ticket, persona o texto") }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), singleLine = true)
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                tipos.forEach { (tipo, etiqueta) -> FilterChip(selected = filtro == tipo, onClick = { filtro = tipo }, label = { Text(etiqueta) }) }
            }
            LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item { Text("${visibles.size} actividades · Más recientes primero", style = MaterialTheme.typography.bodySmall) }
                if (visibles.isEmpty()) item { Text("No hay actividades que coincidan con la búsqueda.") }
                items(visibles, key = { it.id }) { actividad ->
                    OutlinedCard(onClick = { onTicket(actividad.ticketId) }, modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(fechaAtencion(actividad.fecha), color = ColorSoporte, style = MaterialTheme.typography.labelLarge)
                            Text("#${actividad.ticketId} · ${actividad.tituloTicket}", style = MaterialTheme.typography.titleSmall)
                            Text(actividad.autorNombre, style = MaterialTheme.typography.titleMedium)
                            Text(actividad.detalle)
                            Text("Ver ticket →", color = ColorSoporte, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
                item { Text("Las reaperturas se registran desde esta versión. Las anteriores no tienen una fecha recuperable.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall) }
            }
        }
    }
}
