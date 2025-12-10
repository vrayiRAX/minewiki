package com.example.minewiki.utils

object VersionHelper {

    fun getVersionStatus(version: String): String {
        return when {
            version.startsWith("1.21") -> "ACTUAL"
            version.startsWith("1.20") -> "ESTABLE"
            version.startsWith("1.8") -> "LEGACY"
            version.contains("beta") || version.contains("preview") -> "BETA"
            else -> "ANTIGUA"
        }
    }

    fun isCompatible(serverVersion: String, clientVersion: String): Boolean {
        return serverVersion == clientVersion
    }
}