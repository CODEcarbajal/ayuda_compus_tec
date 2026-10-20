package com.carbajal.ayudacampus.ui.navigation

import android.widget.Toast
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
import com.carbajal.ayudacampus.ui.soporte.AtenderTicketScreen
import com.carbajal.ayudacampus.ui.soporte.BandejaScreen
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
    val usuarioActualState by sesionViewModel.usuarioActual.collectAsState()

    val clienteId = usuarioActualState?.id ?: 1
    val usuarioFinal = usuarioActualState ?: Usuario(
        id = 1,
        nombre = "Juan Pérez",
        correo = "juan@campus.test",
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

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN
    ) {
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
                }
            )
        }

        composable(Rutas.MIS_TICKETS) {
            val misTicketsViewModel: MisTicketsViewModel = viewModel(
                factory = AyudaViewModelFactory(app.repository, app.preferenciasStore, clienteId = clienteId)
            )
            MisTicketsScreen(
                viewModel = misTicketsViewModel,
                nombreEstudiante = nombreEstudiante,
                onCrearTicket = {
                    navController.navigate(Rutas.NUEVO_TICKET)
                },
                onVerDetalle = { ticketId ->
                    navController.navigate(Rutas.crearDetalleTicket(ticketId))
                },
                onIrPerfil = {
                    // Navegar a perfil
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
                    // Navegar a perfil
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
                    // Navegar a perfil
                }
            )
        }

        composable(Rutas.PANEL_SOPORTE) {
            val panelViewModel: PanelViewModel = viewModel(
                factory = AyudaViewModelFactory(app.repository, app.preferenciasStore)
            )
            PanelScreen(
                viewModel = panelViewModel,
                onVerBandeja = {
                    navController.navigate(Rutas.BANDEJA)
                },
                onIrCategorias = {
                    navController.navigate(Rutas.FORMULARIO_CATEGORIA)
                },
                onIrAjustes = {
                    // Navegar a ajustes
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
                    navController.navigate(Rutas.FORMULARIO_CATEGORIA)
                },
                onIrAjustes = {
                    // Navegar a ajustes
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
                    navController.navigate(Rutas.FORMULARIO_CATEGORIA)
                },
                onIrAjustes = {
                    // Navegar a ajustes
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
    }
}
