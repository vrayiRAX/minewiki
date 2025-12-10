package com.example.minewiki.utils

object ExperienceCalculator {

    fun calculateLevel(xp: Int): Int {
        if (xp < 0) return 0
        return xp / 100
    }

    fun xpToNextLevel(xp: Int): Int {
        if (xp < 0) return 100
        val remainder = xp % 100
        return 100 - remainder
    }
}