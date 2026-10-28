package com.carbajal.ayudacampus.ui.cliente

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.carbajal.ayudacampus.ui.theme.ColorFondoModoPractica
import com.carbajal.ayudacampus.ui.theme.ColorFondoSelector
import com.carbajal.ayudacampus.ui.theme.ColorSoporte
import com.carbajal.ayudacampus.viewmodel.SesionViewModel

@Composable
fun LoginScreen(
    viewModel: SesionViewModel,
    onLoginExitoso: (String) -> Unit,
    onIrRegistro: () -> Unit
) {
    var rolSeleccionado by remember { mutableStateOf(Rol.CLIENTE) }
    var correo by remember { mutableStateOf("meliton@campus.test") }
    var clave by remember { mutableStateOf("1234") }

    val error by viewModel.error.collectAsState()
    val colorRol = when (rolSeleccionado) {
        Rol.CLIENTE -> ColorCliente
        Rol.ADMIN, Rol.GERENTE -> ColorSoporte
    }

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
            text = "Bienvenido",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Ingresa para continuar",
            fontSize = 15.sp,
            color = Color(0xFF6B7280)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Selector de Rol (Estudiante | Soporte TI | Gerente)
        Surface(
            color = ColorFondoSelector,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val rolActual = rolSeleccionado

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (rolActual == Rol.CLIENTE) ColorCliente else Color.Transparent)
                        .clickable {
                            rolSeleccionado = Rol.CLIENTE
                            correo = "meliton@campus.test"
                            clave = "1234"
                            viewModel.limpiarError()
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Estudiante",
                        color = if (rolActual == Rol.CLIENTE) Color.White else Color(0xFF4B5563),
                        fontWeight = if (rolActual == Rol.CLIENTE) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (rolActual == Rol.ADMIN) ColorSoporte else Color.Transparent)
                        .clickable {
                            rolSeleccionado = Rol.ADMIN
                            correo = "soporte@campus.test"
                            clave = "admin"
                            viewModel.limpiarError()
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Soporte TI",
                        color = if (rolActual == Rol.ADMIN) Color.White else Color(0xFF4B5563),
                        fontWeight = if (rolActual == Rol.ADMIN) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (rolActual == Rol.GERENTE) ColorSoporte else Color.Transparent)
                        .clickable {
                            rolSeleccionado = Rol.GERENTE
                            correo = "gerente@campus.test"
                            clave = "gerente"
                            viewModel.limpiarError()
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Gerente",
                        color = if (rolActual == Rol.GERENTE) Color.White else Color(0xFF4B5563),
                        fontWeight = if (rolActual == Rol.GERENTE) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Campo Correo
        AyudaTextField(
            value = correo,
            onValueChange = {
                correo = it
                viewModel.limpiarError()
            },
            label = "Correo",
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
                viewModel.limpiarError()
            },
            label = "Contraseña",
            leadingIcon = Icons.Outlined.Lock,
            iconTint = colorRol,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        // Enlace ¿Olvidaste tu contraseña?
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = colorRol,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { /* Olvidé contraseña */ }
            )
        }

        // Mensaje de Error
        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Ingresar
        AyudaButton(
            text = "Ingresar",
            onClick = {
                viewModel.login(correo, clave, rolSeleccionado, onLoginExitoso)
            },
            containerColor = colorRol,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sección Inferior diferente según el Rol
        if (rolSeleccionado == Rol.CLIENTE) {
            // Estudiante: ¿No tienes cuenta? Regístrate
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = ColorCliente, fontWeight = FontWeight.Normal)) {
                            append("¿No tienes cuenta? ")
                        }
                        withStyle(style = SpanStyle(color = ColorCliente, fontWeight = FontWeight.Bold)) {
                            append("Regístrate")
                        }
                    },
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { 
                        viewModel.limpiarError()
                        onIrRegistro() 
                    }
                )
            }
        } else {
            // Soporte TI: Tarjeta de Modo Práctica
            Surface(
                color = ColorFondoModoPractica,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Modo práctica",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = ColorSoporte
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Puedes cambiar de perfil desde Ajustes para probar ambos roles.",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
