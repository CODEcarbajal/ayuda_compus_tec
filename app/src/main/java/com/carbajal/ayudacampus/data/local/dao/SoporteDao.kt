package com.carbajal.ayudacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.carbajal.ayudacampus.data.local.entity.ActividadTicket
import com.carbajal.ayudacampus.data.local.entity.CitaAtencion
import kotlinx.coroutines.flow.Flow

@Dao
interface SoporteDao {
    @Query("SELECT COUNT(*) FROM usuarios WHERE id = :id AND rol = 'ADMIN'")
    suspend fun contarTecnico(id: Int): Int
    @Insert suspend fun insertarActividad(actividad: ActividadTicket)
    @Insert suspend fun insertarCita(cita: CitaAtencion)
    @Query("SELECT * FROM actividad_tickets ORDER BY fecha DESC, id DESC")
    fun observarActividad(): Flow<List<ActividadTicket>>
    @Query("SELECT * FROM citas_atencion ORDER BY inicio ASC")
    fun observarCitas(): Flow<List<CitaAtencion>>
    @Query("SELECT COUNT(*) FROM citas_atencion WHERE soporteId = :soporteId AND estado = 'PROGRAMADA' AND inicio < :fin AND fin > :inicio")
    suspend fun contarCruces(soporteId: Int, inicio: Long, fin: Long): Int
    @Query("UPDATE citas_atencion SET estado = :estado WHERE id = :id AND estado = 'PROGRAMADA'")
    suspend fun cambiarEstadoCita(id: Int, estado: String)
}
