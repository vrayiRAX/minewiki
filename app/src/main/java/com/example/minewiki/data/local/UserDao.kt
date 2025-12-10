package com.example.minewiki.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.minewiki.data.model.User

@Dao
interface UserDao {

    // Función para registrar un usuario nuevo
    // OnConflictStrategy.IGNORE significa que si ya existe, no hace nada (o puedes usar REPLACE)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    // Función para el Login: Busca si existe un usuario con ese email y contraseña
    @Query("SELECT * FROM users_table WHERE email = :email AND password = :pass LIMIT 1")
    suspend fun login(email: String, pass: String): User?

    // Función para obtener datos del usuario por su correo
    @Query("SELECT * FROM users_table WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?
}