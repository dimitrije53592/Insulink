package com.dj.insulink.feature.ui.viewmodel

import androidx.compose.ui.unit.min
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dj.insulink.auth.data.AuthRepository
import com.dj.insulink.feature.data.repository.ExerciseRepository
import com.dj.insulink.feature.domain.models.Exercise
import com.dj.insulink.feature.domain.models.Reminder
import com.dj.insulink.feature.domain.models.Sport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FitnessViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val allExercisesForUser: StateFlow<List<Exercise>> = authRepository.getCurrentUserFlow()
        .flatMapLatest { userId ->
            if (userId != null) {
                exerciseRepository.getAllExercisesForUser(userId)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val calculatedSports: StateFlow<List<Sport>> = allExercisesForUser
        .map { exercises ->
            calculateSportsFromExercises(exercises)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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

    fun onAddExerciseClick(userId: String?) {
        viewModelScope.launch {
            userId?.let {
                exerciseRepository.insert(
                    userId = userId,
                    exercise = Exercise(
                        id = 0,
                        userId = userId,
                        sportName = _activityName.value,
                        durationHours = _durationHours.value.toIntOrNull() ?: 0,
                        durationMinutes = _durationMinutes.value.toIntOrNull() ?: 0,
                        glucoseBefore = _glucoseBefore.value.toIntOrNull() ?: 0,
                        glucoseAfter = _glucoseAfter.value.toIntOrNull() ?: 0
                    )
                )
            }

            resetAddExerciseDialogFields()
        }
    }

    fun fetchAllExercisesForUserAndUpdateDatabase(userId: String) {
        viewModelScope.launch {
            exerciseRepository.fetchAllExercisesForUserAndUpdateDatabase(userId)
        }
    }

    private fun calculateSportsFromExercises(exercises: List<Exercise>): List<Sport> {
        return exercises
            .filter { it.sportName.isNotBlank() }
            .groupBy { it.sportName }
            .map { (sportName, exercisesForSport) ->
                val dropsPerHour = exercisesForSport.mapNotNull { exercise ->
                    calculateDropPerHour(exercise).takeIf { it.isFinite() }
                }

                val avgDropPerHour = if (dropsPerHour.isNotEmpty()) {
                    dropsPerHour.average().toInt()
                } else {
                    0
                }

                val lastDropPerHour = if (dropsPerHour.isNotEmpty()) {
                    dropsPerHour.last().toInt()
                } else {
                    0
                }

                Sport(
                    name = sportName,
                    avgDropPerHour = avgDropPerHour,
                    lastDropPerHour = lastDropPerHour
                )
            }
            .sortedByDescending { sport ->
                exercises.filter { it.sportName == sport.name }.size
            }
    }

    private fun calculateDropPerHour(exercise: Exercise): Double {
        val glucoseDrop = exercise.glucoseBefore - exercise.glucoseAfter
        val totalHours = exercise.durationHours + (exercise.durationMinutes / 60.0)

        return if (totalHours > 0) {
            glucoseDrop / totalHours
        } else {
            0.0
        }
    }

    private fun resetAddExerciseDialogFields() {
        _activityName.value = ""
        _durationHours.value = ""
        _durationMinutes.value = ""
        _glucoseBefore.value = ""
        _glucoseAfter.value = ""
    }
}