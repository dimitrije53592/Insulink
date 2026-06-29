package com.dj.insulink.core.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class DateUtilsTest {

    private fun calendarOf(
        year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int = 30, millis: Int = 500
    ): Calendar = Calendar.getInstance().apply {
        clear()
        set(year, month, day, hour, minute, second)
        set(Calendar.MILLISECOND, millis)
    }

    @Test
    fun `combineDateAndTime keeps the time of day but moves to the new date`() {
        val newDate = calendarOf(2024, Calendar.MARCH, 15, 0, 0)
        val existing = calendarOf(2020, Calendar.JANUARY, 1, 14, 45)

        val result = Calendar.getInstance().apply {
            timeInMillis = combineDateAndTime(newDate.timeInMillis, existing.timeInMillis)
        }

        assertEquals(2024, result.get(Calendar.YEAR))
        assertEquals(Calendar.MARCH, result.get(Calendar.MONTH))
        assertEquals(15, result.get(Calendar.DAY_OF_MONTH))
        assertEquals(14, result.get(Calendar.HOUR_OF_DAY))
        assertEquals(45, result.get(Calendar.MINUTE))
        assertEquals(0, result.get(Calendar.SECOND))
        assertEquals(0, result.get(Calendar.MILLISECOND))
    }

    @Test
    fun `combineTimeWithDate sets hour and minute and zeroes seconds and millis`() {
        val existing = calendarOf(2022, Calendar.JULY, 9, 8, 0)

        val result = Calendar.getInstance().apply {
            timeInMillis = combineTimeWithDate(hour = 21, minute = 5, existingTimestamp = existing.timeInMillis)
        }

        assertEquals(2022, result.get(Calendar.YEAR))
        assertEquals(Calendar.JULY, result.get(Calendar.MONTH))
        assertEquals(9, result.get(Calendar.DAY_OF_MONTH))
        assertEquals(21, result.get(Calendar.HOUR_OF_DAY))
        assertEquals(5, result.get(Calendar.MINUTE))
        assertEquals(0, result.get(Calendar.SECOND))
        assertEquals(0, result.get(Calendar.MILLISECOND))
    }
}
