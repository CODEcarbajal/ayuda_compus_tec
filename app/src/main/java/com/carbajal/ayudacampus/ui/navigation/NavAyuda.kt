package com.carbajal.ayudacampus.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.carbajal.ayudacampus.viewmodel.AyudaViewModelFactory
import com.carbajal.ayudacampus.viewmodel.MisTicketsViewModel
import com.carbajal.ayudacampus.viewmodel.NuevoTicketViewModel
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Pantalla de Panel de Soporte (TI)")
            }
        }
    }
}
