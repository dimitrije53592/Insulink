package com.dj.insulink.feature.reminders.domain.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dj.insulink.R

data class Reminder(
    val id: Long,
    val userId: String,
    val title: String,
    val reminderType: ReminderType,
    val isDoneForToday: Boolean,
    val time: Long
)

enum class ReminderType(@StringRes val displayNameRes: Int, @DrawableRes val icon: Int) {
    MEAL_REMINDER(R.string.reminder_type_meal, R.drawable.ic_utensils),
    INSULIN_REMINDER(R.string.reminder_type_insulin, R.drawable.ic_syringe),
    BLOOD_SUGAR_CHECK_REMINDER(R.string.reminder_type_blood_sugar, R.drawable.ic_blood);

    companion object {
        fun fromName(name: String?): ReminderType? {
            return entries.find { it.name == name }
        }
    }
}
