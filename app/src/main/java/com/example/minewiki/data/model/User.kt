package com.example.minewiki.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity le dice a Android que esto es una tabla de la base de datos
@Entity(tableName = "users_table")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID único automático
    val username: String,
    val email: String,
    val password: String, // En una app real esto debería ir encriptado
    val xpLevel: Int = 0,  // Nivel de experiencia en Minecraft
    val skinUrl: String? = null // Por si quieres guardar la foto después
)