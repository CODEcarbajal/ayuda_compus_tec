package com.carbajal.ayudacampus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tickets",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["clienteId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [  //Nos permiten filtrar los datos de la tabla
        Index("clienteId"),
        Index("categoriaId")
    ]
)
data class Ticket(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clienteId: Int,
    val categoriaId: Int,
    val titulo: String,
    val descripcion: String,
    val ubicacion: String,
    val prioridad: String, // "BAJA" | "MEDIA" | "ALTA"
    val estado: String,   // "ABIERTO" | "EN_PROCESO" | "RESUELTO" | "CERRADO"
    val fecha: Long = System.currentTimeMillis(),
    val fechaCierre: Long? = null,
    val calificacion: Int? = null
)