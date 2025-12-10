package com.example.minewiki

import com.example.minewiki.utils.VersionHelper
import org.junit.Assert.*
import org.junit.Test

class VersionHelperTest {

    @Test
    fun `version 1_21 debe ser ACTUAL`() {
        val estado = VersionHelper.getVersionStatus("1.21.1")
        assertEquals("ACTUAL", estado)
    }

    @Test
    fun `version 1_8 debe ser LEGACY`() {
        val estado = VersionHelper.getVersionStatus("1.8.9")
        assertEquals("LEGACY", estado)
    }

    @Test
    fun `versiones distintas no son compatibles`() {
        val esCompatible = VersionHelper.isCompatible("1.20.1", "1.21.0")
        assertFalse(esCompatible)
    }

    @Test
    fun `versiones iguales son compatibles`() {
        val esCompatible = VersionHelper.isCompatible("1.21.0", "1.21.0")
        assertTrue(esCompatible)
    }
}