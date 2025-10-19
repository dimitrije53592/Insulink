package com.dj.insulink.feature.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.dj.insulink.feature.domain.models.ReminderType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ReminderViewModel @Inject constructor() : ViewModel() {

    private val _showAddReminderDialog = MutableStateFlow(false)
    val showAddReminderDialog = _showAddReminderDialog.asStateFlow()

    private val _reminderTitle = MutableStateFlow("")
    val reminderTitle = _reminderTitle.asStateFlow()

    private val _reminderType = MutableStateFlow(ReminderType.MEAL_REMINDER)
    val reminderType = _reminderType.asStateFlow()

    private val _reminderTime = MutableStateFlow(0L)
    val reminderTime = _reminderTime.asStateFlow()

    fun setShowAddReminderDialog(isVisible: Boolean) {
        _showAddReminderDialog.value = isVisible
    }

    fun setReminderTitle(title: String) {
        if(title.length <= 20) {
            _reminderTitle.value = title
        }
    }

    fun setReminderType(type: ReminderType) {
        _reminderType.value = type
    }

    fun setReminderTime(time: Long) {
        _reminderTime.value = time
    }

}