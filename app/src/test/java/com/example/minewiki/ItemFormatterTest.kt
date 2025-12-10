package com.example.minewiki

import com.example.minewiki.utils.ItemFormatter
import org.junit.Assert.*
import org.junit.Test

class ItemFormatterTest {

    @Test
    fun `debe quitar el prefijo minecraft`() {
        val resultado = ItemFormatter.cleanName("minecraft:stone")
        assertEquals("Stone", resultado)
    }

    @Test
    fun `debe quitar guiones bajos y poner mayusculas`() {
        val resultado = ItemFormatter.cleanName("diamond_sword")
        assertEquals("Diamond Sword", resultado)
    }

    @Test
    fun `debe funcionar con nombres complejos`() {
        val resultado = ItemFormatter.cleanName("minecraft:cooked_porkchop")
        assertEquals("Cooked Porkchop", resultado)
    }
}