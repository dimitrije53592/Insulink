package com.dj.insulink.feature.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dj.insulink.feature.data.repository.GlucoseReadingRepository
import com.dj.insulink.feature.domain.models.GlucoseReading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val glucoseRepository: GlucoseReadingRepository
) : ViewModel() {

    private val _minDate = MutableStateFlow<Long?>(null)
    val minDate: StateFlow<Long?> = _minDate.asStateFlow()

    private val _maxDate = MutableStateFlow<Long?>(null)
    val maxDate: StateFlow<Long?> = _maxDate.asStateFlow()

    private val _selectedMinDate = MutableStateFlow<Long?>(null)
    val selectedMinDate: StateFlow<Long?> = _selectedMinDate.asStateFlow()

    private val _selectedMaxDate = MutableStateFlow<Long?>(null)
    val selectedMaxDate: StateFlow<Long?> = _selectedMaxDate.asStateFlow()

    private val _filteredReadings = MutableStateFlow<List<GlucoseReading>>(emptyList())
    val filteredReadings: StateFlow<List<GlucoseReading>> = _filteredReadings.asStateFlow()

    fun initializeDateRange(userId: String) {
        viewModelScope.launch {
            try {
                val (earliestDate, latestDate) = glucoseRepository.getDateRange(userId)
                _minDate.value = earliestDate
                _maxDate.value = latestDate
                _selectedMinDate.value = earliestDate
                _selectedMaxDate.value = latestDate

                if (earliestDate != null && latestDate != null) {
                    filterReadingsByDateRange(userId, earliestDate, latestDate)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun filterReadingsByCurrentDateRange(userId: String) {
        val currentMinDate = _minDate.value
        val currentMaxDate = _maxDate.value

        if (currentMinDate != null && currentMaxDate != null) {
            filterReadingsByDateRange(userId, currentMinDate, currentMaxDate)
        }
    }

    fun filterReadingsByDateRange(userId: String, startDate: Long, endDate: Long) {
        viewModelScope.launch {
            try {
                glucoseRepository.getGlucoseReadingsByDateRange(userId, startDate, endDate)
                    .collect { readings ->
                        _filteredReadings.value = readings.sortedBy { it.timestamp }
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateDateRange(startDate: Long, endDate: Long) {
        _selectedMinDate.value = startDate
        _selectedMaxDate.value = endDate
    }

    fun resetDateRange(userId: String) {
        viewModelScope.launch {
            val (earliestDate, latestDate) = glucoseRepository.getDateRange(userId)
            _minDate.value = earliestDate
            _maxDate.value = latestDate

            if (earliestDate != null && latestDate != null) {
                filterReadingsByDateRange(userId, earliestDate, latestDate)
            }
        }
    }

}