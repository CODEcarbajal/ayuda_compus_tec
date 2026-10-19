package com.carbajal.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carbajal.ayudacampus.data.local.entity.Categoria
import com.carbajal.ayudacampus.model.Prioridad
import com.carbajal.ayudacampus.ui.components.AyudaButton
import com.carbajal.ayudacampus.ui.theme.ColorCliente
import com.carbajal.ayudacampus.viewmodel.NuevoTicketViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoTicketScreen(
    viewModel: NuevoTicketViewModel,
    onVolver: () -> Unit = {},
    onTicketEnviado: () -> Unit = {},
    onIrTickets: () -> Unit = {},
    onIrPerfil: () -> Unit = {}
) {
    val categorias by viewModel.categoriasActivas.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val titulo by viewModel.titulo.collectAsState()
    val ubicacion by viewModel.ubicacion.collectAsState()
    val prioridad by viewModel.prioridad.collectAsState()
    val descripcion by viewModel.descripcion.collectAsState()

    val errorCategoria by viewModel.errorCategoria.collectAsState()
    val errorTitulo by viewModel.errorTitulo.collectAsState()
    val errorDescripcion by viewModel.errorDescripcion.collectAsState()
    val cargando by viewModel.cargando.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Nuevo ticket",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF111827)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF111827)
                        )
                    }
                },
                actions = {
                    // Espacio balanceado a la derecha para centrar el título
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            // Barra inferior de navegación
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Opción Tickets
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onIrTickets() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Segment,
                            contentDescription = "Tickets",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Tickets",
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }

                    // Opción Nuevo (Activa)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Nuevo",
                            tint = ColorCliente,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Nuevo",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorCliente
                        )
                    }

                    // Opción Perfil
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onIrPerfil() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Perfil",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Perfil",
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Sección Categoría
            Text(
                text = "Categoría",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                categorias.forEach { cat ->
                    val esSeleccionada = categoriaSeleccionada?.id == cat.id
                    CategoriaPillChip(
                        categoria = cat,
                        seleccionada = esSeleccionada,
                        onClick = { viewModel.seleccionarCategoria(cat) }
                    )
                }
            }

            errorCategoria?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Campo Título
            OutlinedTextField(
                value = titulo,
                onValueChange = { viewModel.actualizarTitulo(it) },
                label = { Text("Título") },
                placeholder = { Text("Ej. Sin Wi-Fi en el laboratorio 3") },
                isError = errorTitulo != null,
                supportingText = {
                    errorTitulo?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ColorCliente,
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedLabelColor = ColorCliente,
                    unfocusedLabelColor = Color(0xFF6B7280)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo Ubicación
            OutlinedTextField(
                value = ubicacion,
                onValueChange = { viewModel.actualizarUbicacion(it) },
                label = { Text("Ubicación") },
                placeholder = { Text("Ej. Lab 3 · Pabellón B") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = ColorCliente
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ColorCliente,
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedLabelColor = ColorCliente,
                    unfocusedLabelColor = Color(0xFF6B7280)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Sección Prioridad
            Text(
                text = "Prioridad",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Prioridad.entries.forEach { p ->
                    val esSeleccionada = prioridad == p
                    val (colorFondoSeleccionado, textoMostrar) = when (p) {
                        Prioridad.BAJA -> Pair(Color(0xFF10B981), "Baja")
                        Prioridad.MEDIA -> Pair(Color(0xFFF59E0B), "Media")
                        Prioridad.ALTA -> Pair(Color(0xFFDC2626), "Alta")
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (esSeleccionada) colorFondoSeleccionado else Color.White)
                            .border(
                                width = if (esSeleccionada) 0.dp else 1.dp,
                                color = if (esSeleccionada) Color.Transparent else Color(0xFFE5E7EB),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { viewModel.seleccionarPrioridad(p) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = textoMostrar,
                            color = if (esSeleccionada) Color.White else Color(0xFF4B5563),
                            fontSize = 14.sp,
                            fontWeight = if (esSeleccionada) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Campo Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { viewModel.actualizarDescripcion(it) },
                label = { Text("Descripción") },
                placeholder = { Text("Detalla el problema que estás experimentando...") },
                isError = errorDescripcion != null,
                supportingText = {
                    errorDescripcion?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                maxLines = 5,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ColorCliente,
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedLabelColor = ColorCliente,
                    unfocusedLabelColor = Color(0xFF6B7280)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Indicador de Tiempo de Respuesta Estimado
            Surface(
                color = Color(0xFFE0F2FE),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Tiempo estimado",
                        tint = ColorCliente,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tiempo de respuesta estimado: 4 horas",
                        fontSize = 13.sp,
                        color = ColorCliente,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Enviar Ticket
            AyudaButton(
                text = if (cargando) "Enviando..." else "Enviar ticket",
                onClick = { viewModel.enviarTicket(onExito = onTicketEnviado) },
                containerColor = ColorCliente,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
private fun CategoriaPillChip(
    categoria: Categoria,
    seleccionada: Boolean,
    onClick: () -> Unit
) {
    val icono = obtenerIconoCategoria(categoria.icono)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (seleccionada) ColorCliente else Color.White)
            .border(
                width = if (seleccionada) 0.dp else 1.dp,
                color = if (seleccionada) Color.Transparent else Color(0xFFE5E7EB),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = if (seleccionada) Color.White else Color(0xFF4B5563),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = categoria.nombre,
                color = if (seleccionada) Color.White else Color(0xFF4B5563),
                fontSize = 13.sp,
                fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

private fun obtenerIconoCategoria(icono: String): ImageVector {
    return when (icono.lowercase()) {
        "pc", "computer" -> Icons.Default.DesktopWindows
        "code" -> Icons.Default.Code
        "wifi" -> Icons.Default.Wifi
        "key" -> Icons.Default.VpnKey
        "printer" -> Icons.Default.Print
        else -> Icons.Default.Tag
    }
}
