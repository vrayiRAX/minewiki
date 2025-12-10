package com.example.minewiki.utils

object ExperienceCalculator {

    // Regla: Cada 100 puntos subes un nivel
    fun calculateLevel(xp: Int): Int {
        if (xp < 0) return 0
        return xp / 100
    }

    // Regla: Cuánto falta para el siguiente nivel
    fun xpToNextLevel(xp: Int): Int {
        if (xp < 0) return 100
        val remainder = xp % 100
        return 100 - remainder
    }
}