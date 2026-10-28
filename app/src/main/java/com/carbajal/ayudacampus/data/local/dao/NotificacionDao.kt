package com.carbajal.ayudacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.carbajal.ayudacampus.data.local.entity.Notificacion
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificacionDao {
    @Insert
    suspend fun insertar(notificaciones: List<Notificacion>)

    @Query("SELECT * FROM notificaciones WHERE usuarioId = :usuarioId AND rol = :rol ORDER BY fecha DESC, id DESC")
    fun observar(usuarioId: Int, rol: String): Flow<List<Notificacion>>

    @Query("UPDATE notificaciones SET leida = 1 WHERE id = :id AND usuarioId = :usuarioId AND rol = :rol")
    suspend fun marcarLeida(id: Int, usuarioId: Int, rol: String)

    @Query("UPDATE notificaciones SET leida = 1 WHERE usuarioId = :usuarioId AND rol = :rol")
    suspend fun marcarTodasLeidas(usuarioId: Int, rol: String)
}
