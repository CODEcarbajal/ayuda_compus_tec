package com.carbajal.ayudacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.carbajal.ayudacampus.data.local.entity.Comentario
import com.carbajal.ayudacampus.data.local.entity.Ticket
import com.carbajal.ayudacampus.data.local.relation.TicketConDetalle
import com.carbajal.ayudacampus.model.ConteoCategoria
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTicket(ticket: Ticket): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarComentario(comentario: Comentario): Long

    @Query("UPDATE tickets SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: Int, estado: String)

    @Query("UPDATE tickets SET prioridad = :prioridad WHERE id = :id")
    suspend fun actualizarPrioridad(id: Int, prioridad: String)

    @Query("UPDATE tickets SET estado = 'CERRADO', calificacion = :calificacion, fechaCierre = :fechaCierre WHERE id = :id")
    suspend fun cerrar(id: Int, calificacion: Int, fechaCierre: Long)

    @Query("UPDATE tickets SET estado = 'ABIERTO', calificacion = NULL, fechaCierre = NULL WHERE id = :id")
    suspend fun reabrir(id: Int)


    // --- CONSULTAS DE LECTURA (REACTIVAS CON FLOW) ---

    @Transaction
    @Query("SELECT * FROM tickets WHERE clienteId = :clienteId ORDER BY fecha DESC")
    fun observarPorCliente(clienteId: Int): Flow<List<TicketConDetalle>>

    @Transaction
    @Query("""
        SELECT * FROM tickets 
        ORDER BY 
            CASE prioridad 
                WHEN 'ALTA' THEN 0 
                WHEN 'MEDIA' THEN 1 
                ELSE 2 
            END, 
            fecha DESC
    """)
    fun observarTodos(): Flow<List<TicketConDetalle>>

    @Transaction
    @Query("SELECT * FROM tickets WHERE id = :id LIMIT 1")
    fun observarUno(id: Int): Flow<TicketConDetalle?>


    // --- CONSULTAS PARA EL PANEL DE SOPORTE ---

    @Query("SELECT COUNT(*) FROM tickets WHERE estado = :estado")
    fun contarPorEstado(estado: String): Flow<Int>

    @Query("""
        SELECT c.nombre AS categoria, COUNT(t.id) AS cantidad 
        FROM categorias c 
        LEFT JOIN tickets t ON c.id = t.categoriaId 
        GROUP BY c.id, c.nombre
    """)
    fun contarPorCategoria(): Flow<List<ConteoCategoria>>

    @Query("""
        SELECT COUNT(*) FROM tickets 
        WHERE prioridad = 'ALTA' AND estado = 'ABIERTO' AND (fecha < :limite)
    """)
    fun contarAltasSinAtender(limite: Long): Flow<Int>

    @Query("SELECT AVG(fechaCierre - fecha) FROM tickets WHERE fechaCierre IS NOT NULL")
    fun observarTiempoPromedioResolucion(): Flow<Double?>
}
