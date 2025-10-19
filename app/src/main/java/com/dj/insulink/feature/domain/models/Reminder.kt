package com.dj.insulink.feature.domain.models

import com.dj.insulink.R

data class Reminder(
    val title: String,
    val reminderType: ReminderType,
    val isDoneForToday: Boolean,
    val time: String
)

enum class ReminderType(val displayName: String, val icon: Int) {
    MEAL_REMINDER("Meal reminder", R.drawable.ic_utensils),
    INSULIN_REMINDER("Insulin dose reminder", R.drawable.ic_syringe),
    BLOOD_SUGAR_CHECK_REMINDER("Blood sugar check reminder", R.drawable.ic_blood);

    companion object {
        fun fromDisplayName(displayName: String): ReminderType? {
            return ReminderType.entries.find { it.displayName == displayName }
        }
    }
}
