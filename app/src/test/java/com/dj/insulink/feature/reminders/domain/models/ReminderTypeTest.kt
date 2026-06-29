package com.dj.insulink.feature.reminders.domain.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReminderTypeTest {

    @Test
    fun `fromName returns matching type for valid names`() {
        assertEquals(ReminderType.MEAL_REMINDER, ReminderType.fromName("MEAL_REMINDER"))
        assertEquals(ReminderType.INSULIN_REMINDER, ReminderType.fromName("INSULIN_REMINDER"))
        assertEquals(ReminderType.BLOOD_SUGAR_CHECK_REMINDER, ReminderType.fromName("BLOOD_SUGAR_CHECK_REMINDER"))
    }

    @Test
    fun `fromName returns null for unknown name`() {
        assertNull(ReminderType.fromName("NOT_A_TYPE"))
    }

    @Test
    fun `fromName returns null for null input`() {
        assertNull(ReminderType.fromName(null))
    }
}
