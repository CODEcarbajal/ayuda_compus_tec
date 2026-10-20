package com.carbajal.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbajal.ayudacampus.data.local.entity.Categoria
import com.carbajal.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriasViewModel(
    private val repository: AyudaRepository
) : ViewModel() {

    // Observa todas las categorías (activas e inactivas)
    val categorias: StateFlow<List<Categoria>> = repository.observarCategorias()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Estados del formulario para crear/editar categoría
    private val _categoriaEditando = MutableStateFlow<Categoria?>(null)
    val categoriaEditando: StateFlow<Categoria?> = _categoriaEditando.asStateFlow()

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _icono = MutableStateFlow("tag")
    val icono: StateFlow<String> = _icono.asStateFlow()

    private val _activa = MutableStateFlow(true)
    val activa: StateFlow<Boolean> = _activa.asStateFlow()

    private val _errorNombre = MutableStateFlow<String?>(null)
    val errorNombre: StateFlow<String?> = _errorNombre.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    fun cargarParaEditar(categoria: Categoria) {
        _categoriaEditando.value = categoria
        _nombre.value = categoria.nombre
        _icono.value = categoria.icono
        _activa.value = categoria.activa
        _errorNombre.value = null
    }

    fun limpiarFormulario() {
        _categoriaEditando.value = null
        _nombre.value = ""
        _icono.value = "tag"
        _activa.value = true
        _errorNombre.value = null
    }

    fun actualizarNombre(texto: String) {
        _nombre.value = texto
        _errorNombre.value = null
    }

    fun actualizarIcono(nuevoIcono: String) {
        _icono.value = nuevoIcono
    }

    fun actualizarActiva(esActiva: Boolean) {
        _activa.value = esActiva
    }

    fun guardarCategoria(onExito: () -> Unit) {
        val nombreTrim = _nombre.value.trim()
        if (nombreTrim.isBlank()) {
            _errorNombre.value = "Ingresa el nombre de la categoría"
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            val categoriaGuardar = Categoria(
                id = _categoriaEditando.value?.id ?: 0,
                nombre = nombreTrim,
                icono = _icono.value,
                activa = _activa.value
            )
            repository.guardarCategoria(categoriaGuardar)
            _cargando.value = false
            limpiarFormulario()
            onExito()
        }
    }

    fun eliminarCategoria(categoria: Categoria) {
        viewModelScope.launch {
            repository.eliminarCategoria(categoria)
        }
    }

    fun cambiarActiva(id: Int, esActiva: Boolean) {
        viewModelScope.launch {
            repository.cambiarActiva(id, esActiva)
        }
    }
}
