package com.dj.insulink.feature.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dj.insulink.auth.data.AuthRepository
import com.dj.insulink.feature.data.repository.GlucoseReadingRepository
import com.dj.insulink.feature.domain.models.GlucoseReading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlucoseViewModel @Inject constructor(
    private val glucoseReadingRepository: GlucoseReadingRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _newGlucoseReadingTimestamp = MutableStateFlow(System.currentTimeMillis())
    val newGlucoseReadingTimestamp = _newGlucoseReadingTimestamp.asStateFlow()

    private val _newGlucoseReadingValue = MutableStateFlow("")
    val newGlucoseReadingValue = _newGlucoseReadingValue.asStateFlow()

    private val _newGlucoseReadingComment = MutableStateFlow("")
    val newGlucoseReadingComment = _newGlucoseReadingComment.asStateFlow()

    private val _showAddGlucoseReadingDialog = MutableStateFlow(false)
    val showAddGlucoseReadingDialog = _showAddGlucoseReadingDialog.asStateFlow()

    private val _selectedTimespan = MutableStateFlow(GlucoseReadingTimespan.ALL_READINGS)
    val selectedTimespan = _selectedTimespan.asStateFlow()


    @OptIn(ExperimentalCoroutinesApi::class)
    val allGlucoseReadings: StateFlow<List<GlucoseReading>> = combine(
        authRepository.getCurrentUserFlow(),
        _selectedTimespan
    ) { userId, timespan ->
        userId to timespan
    }.flatMapLatest { (userId, timespan) ->
        if (userId != null) {
            glucoseReadingRepository.getAllGlucoseReadingsForUser(userId)
                .map { readings -> filterGlucoseReadingsByTimespan(readings, timespan) }
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val latestGlucoseReading: StateFlow<GlucoseReading?> = allGlucoseReadings
        .map { it.firstOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun submitNewGlucoseReading() {
        viewModelScope.launch(Dispatchers.IO) {
            glucoseReadingRepository.insert(
                userId = authRepository.getCurrentUser()?.uid!!,
                reading = GlucoseReading(
                    id = 0,
                    userId = authRepository.getCurrentUser()?.uid!!,
                    timestamp = newGlucoseReadingTimestamp.value,
                    value = newGlucoseReadingValue.value.toInt(),
                    comment = newGlucoseReadingComment.value
                )
            )
            resetNewReadingDialogFields()
        }
    }

    fun deleteGlucoseReading(glucoseReading: GlucoseReading) {
        viewModelScope.launch(Dispatchers.IO) {
            glucoseReadingRepository.delete(
                userId = authRepository.getCurrentUser()?.uid!!,
                reading = glucoseReading
            )
        }
    }

    fun setNewGlucoseReadingTimestamp(newTimestamp: Long) {
        _newGlucoseReadingTimestamp.value = newTimestamp
    }

    fun setNewGlucoseReadingValue(newValue: String) {
        _newGlucoseReadingValue.value = newValue
    }

    fun setNewGlucoseReadingComment(comment: String) {
        if (comment.length <= COMMENT_MAXIMUM_LENGTH) {
            _newGlucoseReadingComment.value = comment
        }
    }

    fun setShowAddGlucoseReadingDialog(isVisible: Boolean) {
        _showAddGlucoseReadingDialog.value = isVisible
    }

    fun setSelectedTimespan(newTimespan: GlucoseReadingTimespan) {
        _selectedTimespan.value = newTimespan
    }

    private fun filterGlucoseReadingsByTimespan(
        allGlucoseReadings: List<GlucoseReading>,
        timespan: GlucoseReadingTimespan
    ): List<GlucoseReading> {
        if (timespan == GlucoseReadingTimespan.ALL_READINGS) {
            return allGlucoseReadings
        }

        val cutoffTime = System.currentTimeMillis() - timespan.milliseconds
        return allGlucoseReadings.filter { it.timestamp >= cutoffTime }
    }

    private fun resetNewReadingDialogFields() {
        _newGlucoseReadingTimestamp.value = System.currentTimeMillis()
        _newGlucoseReadingValue.value = ""
        _newGlucoseReadingComment.value = ""
    }
}

enum class GlucoseReadingTimespan(val displayName: String, val milliseconds: Long) {
    ALL_READINGS("All readings", Long.MAX_VALUE),
    LAST_DAY("Last 24 hours", 24 * 60 * 60 * 1000L),
    LAST_3_DAYS("Last 3 days", 72 * 60 * 60 * 1000L),
    LAST_WEEK("Last week", 7 * 24 * 60 * 60 * 1000L),
    LAST_MONTH("Last month", 30 * 24 * 60 * 60 * 1000L);

    companion object {
        fun fromDisplayName(displayName: String): GlucoseReadingTimespan? {
            return entries.find { it.displayName == displayName }
        }
    }
}

private const val COMMENT_MAXIMUM_LENGTH = 20