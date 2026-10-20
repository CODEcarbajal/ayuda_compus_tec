package com.carbajal.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carbajal.ayudacampus.data.local.entity.Usuario
import com.carbajal.ayudacampus.data.local.relation.TicketConDetalle
import com.carbajal.ayudacampus.data.prefs.PreferenciasStore
import com.carbajal.ayudacampus.model.EstadoTicket
import com.carbajal.ayudacampus.model.Rol
import com.carbajal.ayudacampus.ui.theme.ColorCliente
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    usuario: Usuario,
    tickets: List<TicketConDetalle>,
    preferenciasStore: PreferenciasStore,
    onVolver: () -> Unit = {},
    onIrTickets: () -> Unit = {},
    onIrNuevoTicket: () -> Unit = {},
    onCambiarAPerfilSoporte: () -> Unit = {},
    onCerrarSesion: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val modoOscuro by preferenciasStore.modoOscuro.collectAsState(initial = false)
    val notificaciones by preferenciasStore.notificaciones.collectAsState(initial = true)

    val abiertos = tickets.count { it.ticket.estado == EstadoTicket.ABIERTO.name || it.ticket.estado == EstadoTicket.EN_PROCESO.name }
    val resueltos = tickets.count { it.ticket.estado == EstadoTicket.RESUELTO.name || it.ticket.estado == EstadoTicket.CERRADO.name }
    val total = tickets.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Perfil",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
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
                        modifier = Modifier.clickable { onIrTickets() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Segment,
                            contentDescription = "Tickets",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Tickets", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onIrNuevoTicket() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Nuevo",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Nuevo", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Perfil",
                            tint = ColorCliente,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Perfil", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorCliente)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Tarjeta Datos del Usuario
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(if (modoOscuro) Color(0xFF1E3A8A) else Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = usuario.nombre,
                            tint = ColorCliente,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = usuario.nombre,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = usuario.correo,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (modoOscuro) Color(0xFF1E3A8A) else Color(0xFFE0F2FE))
                                .padding(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Estudiante",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorCliente
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjetas de Contadores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaContadorPerfil("Abiertos", abiertos, if (modoOscuro) Color(0xFF78350F) else Color(0xFFFEF3C7), if (modoOscuro) Color(0xFFFBBF24) else Color(0xFFD97706), Modifier.weight(1f))
                TarjetaContadorPerfil("Resueltos", resueltos, if (modoOscuro) Color(0xFF064E3B) else Color(0xFFDCFCE7), if (modoOscuro) Color(0xFF34D399) else Color(0xFF16A34A), Modifier.weight(1f))
                TarjetaContadorPerfil("Total", total, if (modoOscuro) Color(0xFF1E3A8A) else Color(0xFFE0F2FE), ColorCliente, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Cambiar de Perfil (Modo Práctica)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = ColorCliente,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            preferenciasStore.guardarSesion(Rol.ADMIN.name, 2)
                            onCambiarAPerfilSoporte()
                        }
                    }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Cambiar perfil",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Cambiar de perfil",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Modo práctica: ir a vista Soporte TI",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sección Preferencias
            Text(
                text = "Preferencias",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    // Modo Oscuro
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Modo oscuro", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Cambia el tema de la app", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = modoOscuro,
                            onCheckedChange = { activado ->
                                scope.launch { preferenciasStore.guardarModoOscuro(activado) }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ColorCliente
                            )
                        )
                    }

                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outline))

                    // Notificaciones
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Notificaciones", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Avisar cambios de estado", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = notificaciones,
                            onCheckedChange = { activadas ->
                                scope.launch { preferenciasStore.guardarNotificaciones(activadas) }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ColorCliente
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cuadro Informativo DataStore
            Surface(
                color = if (modoOscuro) Color(0xFF1E3A8A) else Color(0xFFE0F2FE),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Se guardan en el dispositivo", fontSize = 12.sp, color = ColorCliente)
                    Text("con DataStore", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorCliente)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Cerrar Sesión
            OutlinedButton(
                onClick = onCerrarSesion,
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = Color(0xFFDC2626)
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFDC2626)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Cerrar sesión",
                    color = Color(0xFFDC2626),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TarjetaContadorPerfil(
    titulo: String,
    valor: Int,
    fondoColor: Color,
    textoColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = fondoColor,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = valor.toString(),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = textoColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = titulo,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = textoColor
            )
        }
    }
}
