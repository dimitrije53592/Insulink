package com.dj.insulink.feature.domain.models

import com.dj.insulink.R

data class Reminder(
    val title: String,
    val reminderType: ReminderType,
    val isDone: Boolean,
    val time: String
)

enum class ReminderType(val icon: Int) {
    MEAL_REMINDER(R.drawable.ic_utensils),
    INSULIN_REMINDER(R.drawable.ic_syringe),
    BLOOD_SUGAR_CHECK_REMINDER(R.drawable.ic_blood)
}
