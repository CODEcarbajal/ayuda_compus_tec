package com.carbajal.ayudacampus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "citas_atencion",
    foreignKeys = [ForeignKey(entity = Ticket::class, parentColumns = ["id"], childColumns = ["ticketId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("ticketId"), Index("inicio")]
)
data class CitaAtencion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ticketId: Int,
    val tituloTicket: String,
    val estudiante: String,
    val soporteId: Int,
    val inicio: Long,
    val fin: Long,
    val lugar: String,
    val notas: String,
    val estado: String = "PROGRAMADA"
)
