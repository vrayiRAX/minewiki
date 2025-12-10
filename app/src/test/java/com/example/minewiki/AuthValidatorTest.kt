package com.example.minewiki

import com.example.minewiki.utils.AuthValidator
import org.junit.Assert.*
import org.junit.Test

class AuthValidatorTest {

    @Test
    fun `login con campos vacios debe fallar`() {
        val resultado = AuthValidator.isLoginValid("", "")
        assertFalse(resultado)
    }

    @Test
    fun `login con email sin arroba debe fallar`() {
        val resultado = AuthValidator.isLoginValid("juan.com", "1234")
        assertFalse(resultado)
    }

    @Test
    fun `login con password corta debe fallar`() {
        val resultado = AuthValidator.isLoginValid("juan@gmail.com", "12")
        assertFalse(resultado)
    }

    @Test
    fun `login correcto debe devolver true`() {
        val resultado = AuthValidator.isLoginValid("steve@minecraft.com", "diamante")
        assertTrue(resultado)
    }
}