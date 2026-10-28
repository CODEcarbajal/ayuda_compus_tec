package com.carbajal.ayudacampus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//Al ser una capa de entidad, esta contiene una data class Ctegoria que define la estructura de la tabla

@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val icono: String, // pc, code, wifi, key, printer, tag
    val activa: Boolean = true
)