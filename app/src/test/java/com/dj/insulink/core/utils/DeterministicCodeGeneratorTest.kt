package com.dj.insulink.core.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DeterministicCodeGeneratorTest {

    private val charSet = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toSet()

    @Test
    fun `same email always produces the same code`() {
        val first = DeterministicCodeGenerator.generateCodeFromEmail("user@example.com")
        val second = DeterministicCodeGenerator.generateCodeFromEmail("user@example.com")
        assertEquals(first, second)
    }

    @Test
    fun `email is normalized for case and surrounding whitespace`() {
        val normalized = DeterministicCodeGenerator.generateCodeFromEmail("user@example.com")
        val messy = DeterministicCodeGenerator.generateCodeFromEmail("  USER@Example.COM  ")
        assertEquals(normalized, messy)
    }

    @Test
    fun `default code length is six`() {
        assertEquals(6, DeterministicCodeGenerator.generateCodeFromEmail("user@example.com").length)
    }

    @Test
    fun `custom code length is honored`() {
        assertEquals(10, DeterministicCodeGenerator.generateCodeFromEmail("user@example.com", length = 10).length)
    }

    @Test
    fun `every character belongs to the allowed charset`() {
        val code = DeterministicCodeGenerator.generateCodeFromEmail("someone@domain.org")
        code.forEach { assertTrue("Unexpected char '$it'", it in charSet) }
    }

    @Test
    fun `different emails produce different codes`() {
        val a = DeterministicCodeGenerator.generateCodeFromEmail("alice@example.com")
        val b = DeterministicCodeGenerator.generateCodeFromEmail("bob@example.com")
        assertNotEquals(a, b)
    }
}
