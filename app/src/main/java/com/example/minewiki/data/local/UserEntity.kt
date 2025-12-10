package com.example.minewiki.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val password: String, // En una app real, esto se encriptaría
    val profileImage: String? = null // Guardaremos la ruta de la foto aquí
)