package com.carbajal.ayudacampus.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampanaNotificaciones(pendientes: Int, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        BadgedBox(badge = { if (pendientes > 0) Badge { Text(if (pendientes > 99) "99+" else "$pendientes") } }) {
            Icon(Icons.Default.Notifications, contentDescription = "Notificaciones, $pendientes sin leer")
        }
    }
}
