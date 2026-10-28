package com.carbajal.ayudacampus.ui.cliente

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carbajal.ayudacampus.model.Rol
import com.carbajal.ayudacampus.ui.components.AyudaButton
import com.carbajal.ayudacampus.ui.components.AyudaTextField
import com.carbajal.ayudacampus.ui.theme.ColorCliente
import com.carbajal.ayudacampus.viewmodel.SesionViewModel

@Composable
fun RegisterScreen(
    viewModel: SesionViewModel,
    onRegistroExitoso: (String) -> Unit,
    onIrLogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var confirmarClave by remember { mutableStateOf("") }
    var errorValidacion by remember { mutableStateOf<String?>(null) }

    val errorState by viewModel.error.collectAsState()
    val errorVisible = errorValidacion ?: errorState
    val colorRol = ColorCliente // Por defecto registramos clientes

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Título y Subtítulo
        Text(
            text = "Crear Cuenta",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Regístrate como estudiante",
            fontSize = 15.sp,
            color = Color(0xFF6B7280)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo Nombre
        AyudaTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                errorValidacion = null
                viewModel.limpiarError()
            },
            label = "Nombre completo",
            leadingIcon = Icons.Outlined.Person,
            iconTint = colorRol,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Correo
        AyudaTextField(
            value = correo,
            onValueChange = {
                correo = it
                errorValidacion = null
                viewModel.limpiarError()
            },
            label = "Correo institucional",
            leadingIcon = Icons.Outlined.Email,
            iconTint = colorRol,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Contraseña
        AyudaTextField(
            value = clave,
            onValueChange = {
                clave = it
                errorValidacion = null
                viewModel.limpiarError()
            },
            label = "Contraseña",
            leadingIcon = Icons.Outlined.Lock,
            iconTint = colorRol,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Confirmar Contraseña
        AyudaTextField(
            value = confirmarClave,
            onValueChange = {
                confirmarClave = it
                errorValidacion = null
                viewModel.limpiarError()
            },
            label = "Confirmar contraseña",
            leadingIcon = Icons.Outlined.Lock,
            iconTint = colorRol,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        // Mensaje de Error
        if (errorVisible != null) {
            Text(
                text = errorVisible,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Registrarse
        AyudaButton(
            text = "Registrarse",
            onClick = {
                if (clave != confirmarClave) {
                    errorValidacion = "Las contraseñas no coinciden"
                } else if (clave.length < 4) {
                    errorValidacion = "La contraseña debe tener al menos 4 caracteres"
                } else {
                    viewModel.registrar(nombre, correo, clave, Rol.CLIENTE, onRegistroExitoso)
                }
            },
            containerColor = colorRol,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Enlace para volver al login
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF6B7280), fontWeight = FontWeight.Normal)) {
                        append("¿Ya tienes cuenta? ")
                    }
                    withStyle(style = SpanStyle(color = ColorCliente, fontWeight = FontWeight.Bold)) {
                        append("Inicia sesión")
                    }
                },
                fontSize = 14.sp,
                modifier = Modifier.clickable { 
                    viewModel.limpiarError()
                    onIrLogin() 
                }
            )
        }
    }
}
