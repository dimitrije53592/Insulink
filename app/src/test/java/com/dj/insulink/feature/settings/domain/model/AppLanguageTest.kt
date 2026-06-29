package com.dj.insulink.feature.settings.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class AppLanguageTest {

    @Test
    fun `fromKey returns matching language for known keys`() {
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromKey("en"))
        assertEquals(AppLanguage.SERBIAN, AppLanguage.fromKey("sr-Latn"))
    }

    @Test
    fun `fromKey falls back to English for unknown key`() {
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromKey("de"))
    }
}
