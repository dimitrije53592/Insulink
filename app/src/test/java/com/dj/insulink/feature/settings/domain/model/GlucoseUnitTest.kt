package com.dj.insulink.feature.settings.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GlucoseUnitTest {

    @Test
    fun `formatValue Int for MG_DL returns plain integer string`() {
        assertEquals("120", GlucoseUnit.MG_DL.formatValue(120))
    }

    @Test
    fun `formatValue Int for MMOL_L converts and formats with one decimal`() {
        // 180 / 18.0182 = 9.9888 -> "10.0"
        assertEquals("10.0", GlucoseUnit.MMOL_L.formatValue(180))
    }

    @Test
    fun `formatValue Double for MG_DL truncates to integer`() {
        assertEquals("120", GlucoseUnit.MG_DL.formatValue(120.9))
    }

    @Test
    fun `formatValue Double for MMOL_L converts and formats with one decimal`() {
        // 90.091 / 18.0182 = 5.0 -> "5.0"
        assertEquals("5.0", GlucoseUnit.MMOL_L.formatValue(90.091))
    }

    @Test
    fun `fromKey returns matching unit for known keys`() {
        assertEquals(GlucoseUnit.MG_DL, GlucoseUnit.fromKey("mg_dl"))
        assertEquals(GlucoseUnit.MMOL_L, GlucoseUnit.fromKey("mmol_l"))
    }

    @Test
    fun `fromKey falls back to MG_DL for unknown key`() {
        assertEquals(GlucoseUnit.MG_DL, GlucoseUnit.fromKey("unknown"))
    }

    @Test
    fun `convertMgDlToMmolL divides by the conversion factor`() {
        assertEquals(10.0, GlucoseUnit.convertMgDlToMmolL(180.182), 0.0001)
    }

    @Test
    fun `convertMmolLToMgDl multiplies by the conversion factor`() {
        assertEquals(180.182, GlucoseUnit.convertMmolLToMgDl(10.0), 0.0001)
    }

    @Test
    fun `conversion round trips back to the original value`() {
        val original = 137.0
        val roundTripped = GlucoseUnit.convertMmolLToMgDl(GlucoseUnit.convertMgDlToMmolL(original))
        assertEquals(original, roundTripped, 0.0001)
    }

    @Test
    fun `every unit has a non-blank key label and suffix`() {
        GlucoseUnit.entries.forEach { unit ->
            assertTrue(unit.key.isNotBlank())
            assertTrue(unit.label.isNotBlank())
            assertTrue(unit.suffix.isNotBlank())
        }
    }
}
