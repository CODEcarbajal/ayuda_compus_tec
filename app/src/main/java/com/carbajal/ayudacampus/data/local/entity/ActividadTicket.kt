package com.carbajal.ayudacampus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "actividad_tickets",
    foreignKeys = [ForeignKey(entity = Ticket::class, parentColumns = ["id"], childColumns = ["ticketId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("ticketId"), Index("fecha")]
)
data class ActividadTicket(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ticketId: Int,
    val tituloTicket: String,
    val autorNombre: String,
    val tipo: String,
    val detalle: String,
    val fecha: Long = System.currentTimeMillis()
)
