package com.carbajal.ayudacampus.ui.soporte

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.carbajal.ayudacampus.ui.theme.ColorSoporte
import com.carbajal.ayudacampus.viewmodel.CategoriasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(
    viewModel: CategoriasViewModel,
    onVolver: () -> Unit = {},
    onNuevaCategoria: () -> Unit = {},
    onEditarCategoria: (Categoria) -> Unit = {},
    onIrPanel: () -> Unit = {},
    onIrBandeja: () -> Unit = {},
    onIrAjustes: () -> Unit = {}
) {
    val categorias by viewModel.categorias.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Categorías",
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
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.limpiarFormulario()
                    onNuevaCategoria()
                },
                shape = CircleShape,
                containerColor = ColorSoporte,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Nueva categoría")
            }
        },
        bottomBar = {
            // Barra inferior Soporte TI
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onIrPanel() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Panel",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Panel", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onIrBandeja() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Segment,
                            contentDescription = "Tickets",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Tickets", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocalOffer,
                            contentDescription = "Categorías",
                            tint = ColorSoporte,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Categorías", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorSoporte)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onIrAjustes() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Ajustes",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Ajustes", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFAFAFA))
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = categorias,
                    key = { it.id }
                ) { cat ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                            .clickable {
                                viewModel.cargarParaEditar(cat)
                                onEditarCategoria(cat)
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF3EFFE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = obtenerIconoVectorCat(cat.icono),
                                        contentDescription = cat.nombre,
                                        tint = ColorSoporte,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column {
                                    Text(
                                        text = cat.nombre,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF111827)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (cat.activa) "Activa" else "Inactiva",
                                        fontSize = 12.sp,
                                        color = if (cat.activa) Color(0xFF16A34A) else Color(0xFF9CA3AF)
                                    )
                                }
                            }

                            Switch(
                                checked = cat.activa,
                                onCheckedChange = { esActiva ->
                                    viewModel.cambiarActiva(cat.id, esActiva)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = ColorSoporte
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun obtenerIconoVectorCat(icono: String): ImageVector {
    return when (icono.lowercase()) {
        "pc", "computer" -> Icons.Default.DesktopWindows
        "code" -> Icons.Default.Code
        "wifi" -> Icons.Default.Wifi
        "key" -> Icons.Default.VpnKey
        "printer" -> Icons.Default.Print
        else -> Icons.Default.Tag
    }
}
