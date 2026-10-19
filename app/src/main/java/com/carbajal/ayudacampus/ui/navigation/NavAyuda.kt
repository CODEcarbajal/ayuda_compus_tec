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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carbajal.ayudacampus.AyudaApp
import com.carbajal.ayudacampus.model.Rol
import com.carbajal.ayudacampus.ui.cliente.LoginScreen
import com.carbajal.ayudacampus.viewmodel.SesionViewModel

@Composable
fun NavAyuda(
    sesionViewModel: SesionViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as AyudaApp).viewModelFactory
    ),
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val perfilActivo by sesionViewModel.perfilActivo.collectAsState(initial = "")

    val inicio = when (perfilActivo) {
        Rol.CLIENTE.name -> Rutas.MIS_TICKETS
        Rol.ADMIN.name -> Rutas.PANEL_SOPORTE
        else -> Rutas.LOGIN
    }

    NavHost(
        navController = navController,
        startDestination = inicio
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Pantalla de Mis Tickets (Estudiante)")
            }
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
