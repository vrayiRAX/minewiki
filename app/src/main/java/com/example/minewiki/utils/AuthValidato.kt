package com.example.minewiki.utils

object AuthValidator {

    fun isLoginValid(email: String, pass: String): Boolean {
        if (email.isBlank() || pass.isBlank()) {
            return false
        }
        if (!email.contains("@")) {
            return false
        }
        if (pass.length < 4) {
            return false
        }

        return true
    }
}