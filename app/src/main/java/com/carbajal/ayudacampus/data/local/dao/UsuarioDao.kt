package com.carbajal.ayudacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.carbajal.ayudacampus.data.local.entity.Usuario

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(usuarios: List<Usuario>)

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int
}
