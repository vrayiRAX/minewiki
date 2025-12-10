package com.example.minewiki.utils


object ItemFormatter {

    fun cleanName(rawName: String): String {
        return rawName
            .replace("minecraft:", "")
            .replace("_", " ")
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
            .trim()
    }
}