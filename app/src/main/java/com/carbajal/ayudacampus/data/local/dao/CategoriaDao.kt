package com.carbajal.ayudacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.carbajal.ayudacampus.data.local.entity.Categoria
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {

    //Las consultas, en este espacio para insertar y listar categorias

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(categoria: Categoria): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(categorias: List<Categoria>)

    @Update
    suspend fun actualizar(categoria: Categoria)

    @Delete
    suspend fun eliminar(categoria: Categoria)

    @Query("UPDATE categorias SET activa = :activa WHERE id = :id")
    suspend fun cambiarActiva(id: Int, activa: Boolean)

    @Query("SELECT * FROM categorias ORDER BY nombre ASC")
    fun observarTodas(): Flow<List<Categoria>>

    @Query("SELECT * FROM categorias WHERE activa = 1 ORDER BY nombre ASC")
    fun observarActivas(): Flow<List<Categoria>>
}
