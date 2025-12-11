package com.example.minewiki.data.model

data class Mob(
    val name: String,
    val imageRes: Int,
    val type: String,
    val health: Int,
    val description: String,
    val drops: String,
    val spawn: String
)