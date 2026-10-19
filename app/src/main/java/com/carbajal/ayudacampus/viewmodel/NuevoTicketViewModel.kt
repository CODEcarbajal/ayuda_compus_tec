package com.carbajal.ayudacampus.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbajal.ayudacampus.data.local.entity.Categoria
import com.carbajal.ayudacampus.data.repository.AyudaRepository
import com.carbajal.ayudacampus.model.Prioridad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NuevoTicketViewModel(
    private val clienteId: Int,
    private val repository: AyudaRepository
) : ViewModel() {

    // Categorías activas cargadas desde el repositorio
    val categoriasActivas: StateFlow<List<Categoria>> = repository.observarCategoriasActivas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Estados de los campos del formulario
    private val _categoriaSeleccionada = MutableStateFlow<Categoria?>(null)
    val categoriaSeleccionada: StateFlow<Categoria?> = _categoriaSeleccionada.asStateFlow()

    private val _titulo = MutableStateFlow("")
    val titulo: StateFlow<String> = _titulo.asStateFlow()

    private val _ubicacion = MutableStateFlow("")
    val ubicacion: StateFlow<String> = _ubicacion.asStateFlow()

    private val _prioridad = MutableStateFlow(Prioridad.MEDIA) // Por defecto MEDIA
    val prioridad: StateFlow<Prioridad> = _prioridad.asStateFlow()

    private val _descripcion = MutableStateFlow("")
    val descripcion: StateFlow<String> = _descripcion.asStateFlow()

    // Estados para mensajes de error individuales por campo
    private val _errorCategoria = MutableStateFlow<String?>(null)
    val errorCategoria: StateFlow<String?> = _errorCategoria.asStateFlow()

    private val _errorTitulo = MutableStateFlow<String?>(null)
    val errorTitulo: StateFlow<String?> = _errorTitulo.asStateFlow()

    private val _errorDescripcion = MutableStateFlow<String?>(null)
    val errorDescripcion: StateFlow<String?> = _errorDescripcion.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    // Funciones para actualizar campos
    fun seleccionarCategoria(categoria: Categoria) {
        _categoriaSeleccionada.value = categoria
        _errorCategoria.value = null
    }

    fun actualizarTitulo(texto: String) {
        _titulo.value = texto
        _errorTitulo.value = null
    }

    fun actualizarUbicacion(texto: String) {
        _ubicacion.value = texto
    }

    fun seleccionarPrioridad(p: Prioridad) {
        _prioridad.value = p
    }

    fun actualizarDescripcion(texto: String) {
        _descripcion.value = texto
        _errorDescripcion.value = null
    }

    // Validación y envío del formulario
    fun enviarTicket(onExito: () -> Unit) {
        var esValido = true

        if (_categoriaSeleccionada.value == null) {
            _errorCategoria.value = "Selecciona una categoría"
            esValido = false
        }

        if (_titulo.value.trim().length < 5) {
            _errorTitulo.value = "El título debe tener al menos 5 caracteres"
            esValido = false
        }

        if (_descripcion.value.trim().length < 10) {
            _errorDescripcion.value = "La descripción debe tener al menos 10 caracteres"
            esValido = false
        }

        if (!esValido) return

        viewModelScope.launch {
            _cargando.value = true
            repository.crearTicket(
                clienteId = clienteId,
                categoriaId = _categoriaSeleccionada.value!!.id,
                titulo = _titulo.value.trim(),
                descripcion = _descripcion.value.trim(),
                ubicacion = _ubicacion.value.trim(),
                prioridad = _prioridad.value
            )
            _cargando.value = false
            onExito()
        }
    }
}