package com.example.minewiki.utils

object AuthValidator {

    // Función pura: Recibe datos, devuelve True/False
    fun isLoginValid(email: String, pass: String): Boolean {
        // 1. Que no estén vacíos
        if (email.isBlank() || pass.isBlank()) {
            return false
        }

        // 2. Que el email tenga arroba (validación simple)
        if (!email.contains("@")) {
            return false
        }

        // 3. Que la contraseña tenga al menos 4 caracteres
        if (pass.length < 4) {
            return false
        }

        return true
    }
}