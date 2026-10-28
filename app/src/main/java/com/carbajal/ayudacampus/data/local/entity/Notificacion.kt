package com.carbajal.ayudacampus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "notificaciones", foreignKeys = [
    ForeignKey(entity = Ticket::class, parentColumns = ["id"], childColumns = ["ticketId"], onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Usuario::class, parentColumns = ["id"], childColumns = ["usuarioId"], onDelete = ForeignKey.CASCADE)
], indices = [Index("ticketId"), Index("usuarioId")])
data class Notificacion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ticketId: Int,
    val usuarioId: Int,
    val rol: String,
    val tituloTicket: String,
    val estado: String,
    val mensaje: String,
    val fecha: Long = System.currentTimeMillis(),
    val leida: Boolean = false
)
