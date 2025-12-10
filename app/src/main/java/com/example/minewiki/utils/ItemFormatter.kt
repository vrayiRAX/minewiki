package com.example.minewiki.utils

import java.util.Locale

object ItemFormatter {

    fun cleanName(rawName: String): String {
        return rawName
            .replace("minecraft:", "") // Quita el prefijo
            .replace("_", " ")         // Cambia guiones por espacios
            .split(" ")                // Separa por palabras
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } } // Mayúscula inicial
            .trim()
    }
}