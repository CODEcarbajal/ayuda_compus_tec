package com.carbajal.ayudacampus.ui.soporte

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.carbajal.ayudacampus.data.local.entity.CitaAtencion
import com.carbajal.ayudacampus.data.local.relation.TicketConDetalle
import com.carbajal.ayudacampus.ui.theme.ColorSoporte
import com.carbajal.ayudacampus.viewmodel.SoportePerfilViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar

internal fun fechaAtencion(fecha: Long): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(fecha))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(viewModel: SoportePerfilViewModel, soporteId: Int, onVolver: () -> Unit, onTicket: (Int) -> Unit) {
    val citas by viewModel.citas.collectAsState()
    val tickets by viewModel.tickets.collectAsState()
    val guardando by viewModel.guardando.collectAsState()
    val error by viewModel.error.collectAsState()
    var mostrarFormulario by rememberSaveable { mutableStateOf(false) }
    var mostrarAnteriores by rememberSaveable { mutableStateOf(false) }
    var cancelarId by rememberSaveable { mutableStateOf<Int?>(null) }
    val propias = citas.filter { it.soporteId == soporteId }
    val visibles = propias.filter { mostrarAnteriores || it.estado == "PROGRAMADA" }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Agenda de atención") }, navigationIcon = {
            IconButton(onClick = onVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") }
        }) },
        floatingActionButton = { FloatingActionButton(onClick = {
            viewModel.limpiarError(); mostrarFormulario = true
        }, containerColor = ColorSoporte, contentColor = MaterialTheme.colorScheme.onPrimary) {
            Icon(Icons.Default.Add, "Programar atención")
        } }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp, 12.dp, 20.dp, 96.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Text("Mis atenciones", style = MaterialTheme.typography.titleLarge)
                Text("Visitas a laboratorios y horarios coordinados con estudiantes.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = !mostrarAnteriores, onClick = { mostrarAnteriores = false }, label = { Text("Programadas") })
                    FilterChip(selected = mostrarAnteriores, onClick = { mostrarAnteriores = true }, label = { Text("Todas") })
                }
                if (error != null && !mostrarFormulario) Text(error!!, color = MaterialTheme.colorScheme.error)
            }
            if (visibles.isEmpty()) item {
                OutlinedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp)) {
                        Text("No hay atenciones ${if (mostrarAnteriores) "registradas" else "programadas"}.")
                        Text("Usa + para coordinar una atención vinculada a un ticket.")
                    }
                }
            }
            items(visibles, key = { it.id }) { cita ->
                OutlinedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(fechaAtencion(cita.inicio), color = ColorSoporte, style = MaterialTheme.typography.titleMedium)
                        Text("${(cita.fin - cita.inicio) / 60000} minutos · ${cita.estado.lowercase().replaceFirstChar { it.uppercase() }}")
                        Text("#${cita.ticketId} · ${cita.tituloTicket}", style = MaterialTheme.typography.titleSmall)
                        Text("Estudiante: ${cita.estudiante}")
                        Text("Lugar o enlace: ${cita.lugar}")
                        if (cita.notas.isNotBlank()) Text(cita.notas, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        TextButton(onClick = { onTicket(cita.ticketId) }) { Text("Ver ticket", color = ColorSoporte) }
                        if (cita.estado == "PROGRAMADA") Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(enabled = !guardando, onClick = { viewModel.cambiarEstado(cita.id, "COMPLETADA") }) { Text("Completar", color = ColorSoporte) }
                            TextButton(enabled = !guardando, onClick = { cancelarId = cita.id }) { Text("Cancelar atención", color = MaterialTheme.colorScheme.error) }
                        }
                    }
                }
            }
        }
    }
    if (mostrarFormulario) FormularioAtencion(
        tickets = tickets.filter { it.ticket.estado !in listOf("RESUELTO", "CERRADO") },
        guardando = guardando, error = error,
        onCerrar = { if (!guardando) { mostrarFormulario = false; viewModel.limpiarError() } },
        onGuardar = { ticket, inicio, duracion, lugar, notas ->
            viewModel.programar(CitaAtencion(ticketId = ticket.ticket.id, tituloTicket = ticket.ticket.titulo,
                estudiante = ticket.cliente.nombre, soporteId = soporteId, inicio = inicio,
                fin = inicio + duracion * 60000L, lugar = lugar.trim(), notas = notas.trim())) { mostrarFormulario = false }
        }
    )
    if (cancelarId != null) AlertDialog(onDismissRequest = { cancelarId = null }, title = { Text("¿Cancelar atención?") },
        text = { Text("Se conservará en la agenda con estado cancelada.") },
        confirmButton = { TextButton(onClick = { viewModel.cambiarEstado(cancelarId!!, "CANCELADA"); cancelarId = null }) { Text("Cancelar atención") } },
        dismissButton = { TextButton(onClick = { cancelarId = null }) { Text("Volver") } })
}

@Composable
private fun FormularioAtencion(
    tickets: List<TicketConDetalle>, guardando: Boolean, error: String?, onCerrar: () -> Unit,
    onGuardar: (TicketConDetalle, Long, Int, String, String) -> Unit
) {
    val context = LocalContext.current
    var ticketId by rememberSaveable { mutableStateOf<Int?>(null) }
    var inicio by rememberSaveable { mutableLongStateOf(System.currentTimeMillis() + 3600000L) }
    var duracion by rememberSaveable { mutableIntStateOf(30) }
    var lugar by rememberSaveable { mutableStateOf("") }
    var notas by rememberSaveable { mutableStateOf("") }
    var menu by remember { mutableStateOf(false) }
    val seleccionado = tickets.firstOrNull { it.ticket.id == ticketId }
    val calendario = Calendar.getInstance().apply { timeInMillis = inicio }
    AlertDialog(onDismissRequest = onCerrar,
        title = { Text("Programar atención") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Selecciona un ticket y coordina el horario con el estudiante.")
                Box {
                    OutlinedButton(onClick = { menu = true }, enabled = !guardando && tickets.isNotEmpty()) {
                        Text(seleccionado?.let { "#${it.ticket.id} · ${it.cliente.nombre}" } ?: "Seleccionar ticket")
                    }
                    DropdownMenu(expanded = menu, onDismissRequest = { menu = false }, modifier = Modifier.heightIn(max = 240.dp)) {
                        tickets.forEach { t -> DropdownMenuItem(text = { Text("#${t.ticket.id} · ${t.ticket.titulo}\n${t.cliente.nombre}") }, onClick = {
                            ticketId = t.ticket.id; if (lugar.isBlank()) lugar = t.ticket.ubicacion; menu = false
                        }) }
                    }
                }
                if (tickets.isEmpty()) Text("No hay tickets pendientes para programar una atención.")
                seleccionado?.let { Text(it.ticket.titulo, style = MaterialTheme.typography.bodySmall) }
                Text(fechaAtencion(inicio), color = ColorSoporte, style = MaterialTheme.typography.titleMedium)
                Row {
                    TextButton(enabled = !guardando, onClick = {
                        DatePickerDialog(context, { _, year, month, day ->
                            inicio = Calendar.getInstance().apply {
                                timeInMillis = inicio; set(Calendar.YEAR, year); set(Calendar.MONTH, month); set(Calendar.DAY_OF_MONTH, day)
                                set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
                            }.timeInMillis
                        }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH)).apply {
                            datePicker.minDate = System.currentTimeMillis(); show()
                        }
                    }) { Text("Elegir fecha", color = ColorSoporte) }
                    TextButton(enabled = !guardando, onClick = {
                        TimePickerDialog(context, { _, hour, minute ->
                            inicio = Calendar.getInstance().apply {
                                timeInMillis = inicio; set(Calendar.HOUR_OF_DAY, hour); set(Calendar.MINUTE, minute)
                                set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
                            }.timeInMillis
                        }, calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE), true).show()
                    }) { Text("Elegir hora", color = ColorSoporte) }
                }
                Text("Duración")
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(30, 60, 90).forEach { minutos -> FilterChip(enabled = !guardando, selected = duracion == minutos,
                        onClick = { duracion = minutos }, label = { Text("$minutos min") }) }
                }
                OutlinedTextField(value = lugar, onValueChange = { lugar = it }, label = { Text("Laboratorio, lugar o enlace") }, enabled = !guardando, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = notas, onValueChange = { notas = it }, label = { Text("Indicaciones (opcional)") }, enabled = !guardando, modifier = Modifier.fillMaxWidth(), minLines = 2)
                Text("La cita queda registrada en este dispositivo. Confirma el horario con el estudiante.", style = MaterialTheme.typography.bodySmall)
                if (error != null) Text(error, color = MaterialTheme.colorScheme.error)
            }
        },
        confirmButton = { TextButton(enabled = !guardando && seleccionado != null && lugar.isNotBlank(), onClick = {
            seleccionado?.let { onGuardar(it, inicio, duracion, lugar, notas) }
        }) { Text(if (guardando) "Guardando…" else "Programar", color = ColorSoporte) } },
        dismissButton = { TextButton(enabled = !guardando, onClick = onCerrar) { Text("Volver") } }
    )
}
