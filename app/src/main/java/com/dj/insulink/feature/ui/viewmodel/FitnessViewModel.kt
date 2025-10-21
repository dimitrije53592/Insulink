package com.dj.insulink.feature.ui.viewmodel

import androidx.compose.ui.unit.min
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FitnessViewModel @Inject constructor() : ViewModel() {

    private val _showAddSportsActivityDialog = MutableStateFlow(false)
    val showAddSportsActivityDialog = _showAddSportsActivityDialog.asStateFlow()

    private val _activityName = MutableStateFlow("")
    val activityName = _activityName.asStateFlow()

    private val _durationHours = MutableStateFlow("")
    val durationHours = _durationHours.asStateFlow()

    private val _durationMinutes = MutableStateFlow("")
    val durationMinutes = _durationMinutes.asStateFlow()

    private val _glucoseBefore = MutableStateFlow("")
    val glucoseBefore = _glucoseBefore.asStateFlow()

    private val _glucoseAfter = MutableStateFlow("")
    val glucoseAfter = _glucoseAfter.asStateFlow()

    fun setShowSportsActivityDialog(isVisible: Boolean) {
        _showAddSportsActivityDialog.value = isVisible
    }

    fun setActivityName(name: String) {
        _activityName.value = name
    }

    fun setDurationHours(hours: String) {
        _durationHours.value = hours
    }

    fun setDurationMinutes(minutes: String) {
        _durationMinutes.value = minutes
    }

    fun setGlucoseBefore(glucose: String) {
        _glucoseBefore.value = glucose
    }

    fun setGlucoseAfter(glucose: String) {
        _glucoseAfter.value = glucose
    }

    fun onAddExerciseClick() {

    }
}