package com.carbajal.ayudacampus.ui.navigation

import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flowOf
import com.carbajal.ayudacampus.ui.cliente.NotificacionesScreen
import com.carbajal.ayudacampus.ui.soporte.AgendaScreen
import com.carbajal.ayudacampus.ui.soporte.HistorialActividadScreen
import com.carbajal.ayudacampus.viewmodel.SoportePerfilViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.carbajal.ayudacampus.AyudaApp
import com.carbajal.ayudacampus.data.local.entity.Usuario
import com.carbajal.ayudacampus.model.Rol
import com.carbajal.ayudacampus.ui.cliente.DetalleTicketScreen
import com.carbajal.ayudacampus.ui.cliente.LoginScreen
import com.carbajal.ayudacampus.ui.cliente.MisTicketsScreen
import com.carbajal.ayudacampus.ui.cliente.NuevoTicketScreen
import com.carbajal.ayudacampus.ui.cliente.PerfilScreen
import com.carbajal.ayudacampus.ui.cliente.RegisterScreen
import com.carbajal.ayudacampus.ui.cliente.WelcomeScreen
import com.carbajal.ayudacampus.ui.soporte.AjustesScreen
import com.carbajal.ayudacampus.ui.soporte.AtenderTicketScreen
import com.carbajal.ayudacampus.ui.soporte.BandejaScreen
import com.carbajal.ayudacampus.ui.soporte.CategoriasScreen
import com.carbajal.ayudacampus.ui.soporte.FormularioCategoriaScreen
import com.carbajal.ayudacampus.ui.soporte.PanelScreen
import com.carbajal.ayudacampus.viewmodel.AtenderViewModel
import com.carbajal.ayudacampus.viewmodel.AyudaViewModelFactory
import com.carbajal.ayudacampus.viewmodel.BandejaViewModel
import com.carbajal.ayudacampus.viewmodel.CategoriasViewModel
import com.carbajal.ayudacampus.viewmodel.MisTicketsViewModel
import com.carbajal.ayudacampus.viewmodel.NuevoTicketViewModel
import com.carbajal.ayudacampus.viewmodel.PanelViewModel
import com.carbajal.ayudacampus.viewmodel.SesionViewModel
import com.carbajal.ayudacampus.viewmodel.TicketDetalleViewModel

@Composable
fun NavAyuda(
    sesionViewModel: SesionViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as AyudaApp).viewModelFactory
    ),
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val app = context.applicationContext as AyudaApp
    val perfilActivo by app.preferenciasStore.perfilActivo.collectAsState(initial = "")
    val usuarioId by app.preferenciasStore.usuarioId.collectAsState(initial = 0)
    val usuarioActualState by sesionViewModel.usuarioActual.collectAsState()

    val scope = rememberCoroutineScope()
    val avisosFlow = remember(usuarioId, perfilActivo) {
        if (usuarioId > 0 && perfilActivo.isNotBlank()) app.repository.observarNotificaciones(usuarioId, perfilActivo)
        else flowOf(emptyList())
    }
    val avisos by androidx.compose.runtime.key(usuarioId, perfilActivo) {
        avisosFlow.collectAsState(initial = emptyList())
    }
    val pendientes = avisos.count { !it.leida }
    val avisosActivadosValor by app.preferenciasStore.notificaciones.collectAsState(initial = true)
    val avisosActivados by androidx.compose.runtime.rememberUpdatedState(avisosActivadosValor)
    // Solo los eventos nuevos de la sesión muestran un aviso inmediato; la bandeja siempre conserva el historial.
    LaunchedEffect(usuarioId, perfilActivo) {
        var ultimoId: Int? = null
        avisosFlow.collect { lista ->
            val nuevoId = lista.maxOfOrNull { it.id } ?: 0
            if (ultimoId?.let { nuevoId > it } == true && avisosActivados) {
                lista.firstOrNull()?.let { Toast.makeText(context, "${it.tituloTicket}: ${it.mensaje}", Toast.LENGTH_LONG).show() }
            }
            ultimoId = nuevoId
        }
    }

    val clienteId = usuarioId.takeIf { it > 0 } ?: 1
    val usuarioFinal = usuarioActualState ?: Usuario(
        id = 1,
        nombre = "Meliton Carbajal",
        correo = "meliton@campus.test",
        clave = "1234",
        rol = Rol.CLIENTE.name
    )
    val usuarioSoporte = Usuario(
        id = 2,
        nombre = "Soporte TI",
        correo = "soporte@campus.test",
        clave = "admin",
        rol = Rol.ADMIN.name
    )
    val nombreEstudiante = usuarioFinal.nombre.split(" ").firstOrNull() ?: "Meliton"

    val inicio = when (perfilActivo) {
        Rol.CLIENTE.name -> Rutas.MIS_TICKETS
        Rol.ADMIN.name -> Rutas.PANEL_SOPORTE
        else -> Rutas.WELCOME
    }

    NavHost(
        navController = navController,
        startDestination = inicio
    ) {
        composable(Rutas.NOTIFICACIONES) {
            NotificacionesScreen(notificaciones = avisos, onVolver = { navController.popBackStack() },
                onAbrir = { aviso ->
                    scope.launch {
                        app.repository.marcarNotificacionLeida(aviso.id, usuarioId, perfilActivo)
                        navController.navigate(if (perfilActivo == Rol.ADMIN.name)
                            Rutas.crearAtenderTicket(aviso.ticketId) else Rutas.crearDetalleTicket(aviso.ticketId))
                    }
                },
                onMarcarTodas = { scope.launch { app.repository.marcarNotificacionesLeidas(usuarioId, perfilActivo) } })
        }
        composable(Rutas.WELCOME) {
            WelcomeScreen(
                onComenzar = {
                    navController.navigate(Rutas.REGISTER)
                },
                onYaTengoCuenta = {
                    navController.navigate(Rutas.LOGIN)
                }
            )
        }

        composable(Rutas.LOGIN) {
            LoginScreen(
                viewModel = sesionViewModel,
                onLoginExitoso = { rol ->
                    Toast.makeText(context, "Inicio de sesión exitoso como $rol", Toast.LENGTH_SHORT).show()
                    val destino = if (rol == Rol.CLIENTE.name) {
                        Rutas.MIS_TICKETS
                    } else {
                        Rutas.PANEL_SOPORTE
                    }
                    navController.navigate(destino) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                },
                onIrRegistro = {
                    navController.navigate(Rutas.REGISTER)
                }
            )
        }

        composable(Rutas.REGISTER) {
            RegisterScreen(
                viewModel = sesionViewModel,
                onRegistroExitoso = { _ ->
                    Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    navController.navigate(Rutas.MIS_TICKETS) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                },
                onIrLogin = {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.MIS_TICKETS) {
            val misTicketsViewModel: MisTicketsViewModel = viewModel(
                factory = AyudaViewModelFactory(app.repository, app.preferenciasStore, clienteId = clienteId)
            )
            MisTicketsScreen(
                notificacionesPendientes = pendientes,
                onIrNotificaciones = { navController.navigate(Rutas.NOTIFICACIONES) },
                viewModel = misTicketsViewModel,
                nombreEstudiante = nombreEstudiante,
                onCrearTicket = {
                    navController.navigate(Rutas.NUEVO_TICKET)
                },
                onVerDetalle = { ticketId ->
                    navController.navigate(Rutas.crearDetalleTicket(ticketId))
                },
                onIrPerfil = {
                    navController.navigate(Rutas.PERFIL)
                }
            )
        }

        composable(Rutas.NUEVO_TICKET) {
            val nuevoTicketViewModel: NuevoTicketViewModel = viewModel(
                factory = AyudaViewModelFactory(app.repository, app.preferenciasStore, clienteId = clienteId)
            )
            NuevoTicketScreen(
                viewModel = nuevoTicketViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onTicketEnviado = {
                    Toast.makeText(context, "Ticket creado con éxito", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                onIrTickets = {
                    navController.navigate(Rutas.MIS_TICKETS) {
                        popUpTo(Rutas.MIS_TICKETS) { inclusive = true }
                    }
                },
                onIrPerfil = {
                    navController.navigate(Rutas.PERFIL)
                }
            )
        }

        composable(
            route = Rutas.DETALLE_TICKET,
            arguments = listOf(navArgument("ticketId") { type = NavType.IntType })
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getInt("ticketId") ?: 1
            val ticketDetalleViewModel: TicketDetalleViewModel = viewModel(
                factory = AyudaViewModelFactory(
                    repository = app.repository,
                    preferenciasStore = app.preferenciasStore,
                    clienteId = clienteId,
                    ticketId = ticketId
                )
            )
            DetalleTicketScreen(
                viewModel = ticketDetalleViewModel,
                usuarioActual = usuarioFinal,
                onVolver = {
                    navController.popBackStack()
                },
                onIrTickets = {
                    navController.navigate(Rutas.MIS_TICKETS) {
                        popUpTo(Rutas.MIS_TICKETS) { inclusive = true }
                    }
                },
                onIrNuevoTicket = {
                    navController.navigate(Rutas.NUEVO_TICKET)
                },
                onIrPerfil = {
                    navController.navigate(Rutas.PERFIL)
                }
            )
        }

        composable(Rutas.PERFIL) {
            val misTicketsViewModel: MisTicketsViewModel = viewModel(
                factory = AyudaViewModelFactory(app.repository, app.preferenciasStore, clienteId = clienteId)
            )
            val ticketsState by misTicketsViewModel.tickets.collectAsState()

            PerfilScreen(
                notificacionesPendientes = pendientes,
                onIrNotificaciones = { navController.navigate(Rutas.NOTIFICACIONES) },
                usuario = usuarioFinal,
                tickets = ticketsState,
                preferenciasStore = app.preferenciasStore,
                onVolver = {
                    navController.popBackStack()
                },
                onIrTickets = {
                    navController.navigate(Rutas.MIS_TICKETS) {
                        popUpTo(Rutas.MIS_TICKETS) { inclusive = true }
                    }
                },
                onIrNuevoTicket = {
                    navController.navigate(Rutas.NUEVO_TICKET)
                },
                onCambiarAPerfilSoporte = {
                    navController.navigate(Rutas.PANEL_SOPORTE) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onCerrarSesion = {
                    sesionViewModel.cerrarSesion {
                        navController.navigate(Rutas.WELCOME) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Rutas.PANEL_SOPORTE) {
            val panelViewModel: PanelViewModel = viewModel(
                factory = AyudaViewModelFactory(app.repository, app.preferenciasStore)
            )
            PanelScreen(
                notificacionesPendientes = pendientes,
                onIrNotificaciones = { navController.navigate(Rutas.NOTIFICACIONES) },
                viewModel = panelViewModel,
                onVerBandeja = {
                    navController.navigate(Rutas.BANDEJA)
                },
                onIrCategorias = {
                    navController.navigate(Rutas.CATEGORIAS)
                },
                onIrAjustes = {
                    navController.navigate(Rutas.AJUSTES)
                }
            )
        }

        composable(Rutas.BANDEJA) {
            val bandejaViewModel: BandejaViewModel = viewModel(
                factory = AyudaViewModelFactory(app.repository, app.preferenciasStore)
            )
            BandejaScreen(
                viewModel = bandejaViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onAtenderTicket = { ticketId ->
                    navController.navigate(Rutas.crearAtenderTicket(ticketId))
                },
                onIrPanel = {
                    navController.navigate(Rutas.PANEL_SOPORTE) {
                        popUpTo(Rutas.PANEL_SOPORTE) { inclusive = true }
                    }
                },
                onIrCategorias = {
                    navController.navigate(Rutas.CATEGORIAS)
                },
                onIrAjustes = {
                    navController.navigate(Rutas.AJUSTES)
                }
            )
        }

        composable(
            route = Rutas.ATENDER_TICKET,
            arguments = listOf(navArgument("ticketId") { type = NavType.IntType })
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getInt("ticketId") ?: 1
            val atenderViewModel: AtenderViewModel = viewModel(
                factory = AyudaViewModelFactory(
                    repository = app.repository,
                    preferenciasStore = app.preferenciasStore,
                    ticketId = ticketId
                )
            )
            AtenderTicketScreen(
                viewModel = atenderViewModel,
                usuarioSoporte = usuarioSoporte,
                onVolver = {
                    navController.popBackStack()
                },
                onIrPanel = {
                    navController.navigate(Rutas.PANEL_SOPORTE) {
                        popUpTo(Rutas.PANEL_SOPORTE) { inclusive = true }
                    }
                },
                onIrCategorias = {
                    navController.navigate(Rutas.CATEGORIAS)
                },
                onIrAjustes = {
                    navController.navigate(Rutas.AJUSTES)
                }
            )
        }

        composable(Rutas.CATEGORIAS) {
            val categoriasViewModel: CategoriasViewModel = viewModel(
                factory = AyudaViewModelFactory(app.repository, app.preferenciasStore)
            )
            CategoriasScreen(
                viewModel = categoriasViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onNuevaCategoria = {
                    navController.navigate(Rutas.FORMULARIO_CATEGORIA)
                },
                onEditarCategoria = {
                    navController.navigate(Rutas.FORMULARIO_CATEGORIA)
                },
                onIrPanel = {
                    navController.navigate(Rutas.PANEL_SOPORTE) {
                        popUpTo(Rutas.PANEL_SOPORTE) { inclusive = true }
                    }
                },
                onIrBandeja = {
                    navController.navigate(Rutas.BANDEJA)
                },
                onIrAjustes = {
                    navController.navigate(Rutas.AJUSTES)
                }
            )
        }

        composable(Rutas.FORMULARIO_CATEGORIA) {
            val categoriasViewModel: CategoriasViewModel = viewModel(
                factory = AyudaViewModelFactory(app.repository, app.preferenciasStore)
            )
            FormularioCategoriaScreen(
                viewModel = categoriasViewModel,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.AGENDA) {
            val vm: SoportePerfilViewModel = viewModel(factory = app.viewModelFactory)
            AgendaScreen(viewModel = vm, soporteId = usuarioId, onVolver = { navController.popBackStack() },
                onTicket = { navController.navigate(Rutas.crearAtenderTicket(it)) })
        }
        composable(Rutas.HISTORIAL_ACTIVIDAD) {
            val vm: SoportePerfilViewModel = viewModel(factory = app.viewModelFactory)
            HistorialActividadScreen(viewModel = vm, onVolver = { navController.popBackStack() },
                onTicket = { navController.navigate(Rutas.crearAtenderTicket(it)) })
        }
        composable(Rutas.AJUSTES) {
            AjustesScreen(
                notificacionesPendientes = pendientes,
                onIrNotificaciones = { navController.navigate(Rutas.NOTIFICACIONES) },
                onIrAgenda = { navController.navigate(Rutas.AGENDA) },
                onIrHistorial = { navController.navigate(Rutas.HISTORIAL_ACTIVIDAD) },
                preferenciasStore = app.preferenciasStore,
                onVolver = {
                    navController.popBackStack()
                },
                onIrPanel = {
                    navController.navigate(Rutas.PANEL_SOPORTE) {
                        popUpTo(Rutas.PANEL_SOPORTE) { inclusive = true }
                    }
                },
                onIrBandeja = {
                    navController.navigate(Rutas.BANDEJA)
                },
                onIrCategorias = {
                    navController.navigate(Rutas.CATEGORIAS)
                },
                onCambiarAPerfilEstudiante = {
                    navController.navigate(Rutas.MIS_TICKETS) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onCerrarSesion = {
                    sesionViewModel.cerrarSesion {
                        navController.navigate(Rutas.WELCOME) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}
