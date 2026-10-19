package com.carbajal.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbajal.ayudacampus.data.prefs.PreferenciasStore
import com.carbajal.ayudacampus.data.repository.AyudaRepository
import com.carbajal.ayudacampus.data.local.entity.Usuario
import com.carbajal.ayudacampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repository: AyudaRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val perfilActivo = preferenciasStore.perfilActivo
    val modoOscuro = preferenciasStore.modoOscuro

    fun login(correo: String, clave: String, rolSeleccionado: Rol, onExito: (String) -> Unit) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Por favor, ingresa correo y contraseña"
            return
        }
        viewModelScope.launch {
            _error.value = null
            val usuario = repository.login(correo.trim(), clave.trim())

            if (usuario == null) {
                _error.value = "Correo o contraseña incorrectos"
            } else if (usuario.rol != rolSeleccionado.name) {
                _error.value = "El usuario no corresponde al rol seleccionado"
            } else {
                _usuarioActual.value = usuario
                preferenciasStore.guardarSesion(usuario.rol, usuario.id)
                onExito(usuario.rol)
            }
        }
    }

    fun limpiarError(){
        _error.value = null
    }

    fun cerrarSesion(onCerrado: () -> Unit) {
        viewModelScope.launch {
            preferenciasStore.cerrarSesion()
            _usuarioActual.value = null
            onCerrado()
        }
    }
}