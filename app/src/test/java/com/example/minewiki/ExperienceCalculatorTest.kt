package com.example.minewiki

import com.example.minewiki.utils.ExperienceCalculator
import org.junit.Assert.*
import org.junit.Test

class ExperienceCalculatorTest {

    @Test
    fun `0 xp debe ser nivel 0`() {
        assertEquals(0, ExperienceCalculator.calculateLevel(0))
    }

    @Test
    fun `150 xp debe ser nivel 1`() {
        assertEquals(1, ExperienceCalculator.calculateLevel(150))
    }

    @Test
    fun `si tengo 250 xp faltan 50 para el siguiente`() {
        assertEquals(50, ExperienceCalculator.xpToNextLevel(250))
    }

    @Test
    fun `xp negativa no rompe la app`() {
        assertEquals(0, ExperienceCalculator.calculateLevel(-50))
    }
}