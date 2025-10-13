package com.dj.insulink.feature.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dj.insulink.feature.data.repository.GlucoseReadingRepository
import com.dj.insulink.feature.domain.models.GlucoseReading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlucoseViewModel @Inject constructor(
    private val glucoseReadingRepository: GlucoseReadingRepository
) : ViewModel() {

    private val _newGlucoseReadingTimestamp = MutableStateFlow(System.currentTimeMillis())
    val newGlucoseReadingTimestamp = _newGlucoseReadingTimestamp.asStateFlow()

    private val _newGlucoseReadingValue = MutableStateFlow("")
    val newGlucoseReadingValue = _newGlucoseReadingValue.asStateFlow()

    private val _newGlucoseReadingComment = MutableStateFlow("")
    val newGlucoseReadingComment = _newGlucoseReadingComment.asStateFlow()

    private val _showAddGlucoseReadingDialog = MutableStateFlow(false)
    val showAddGlucoseReadingDialog = _showAddGlucoseReadingDialog.asStateFlow()

    val allGlucoseReadings: StateFlow<List<GlucoseReading>> = glucoseReadingRepository
        .getAllGlucoseReadings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun submitNewGlucoseReading() {
        viewModelScope.launch(Dispatchers.IO) {
            glucoseReadingRepository.insert(
                GlucoseReading(
                    id = 0,
                    timestamp = newGlucoseReadingTimestamp.value,
                    value = newGlucoseReadingValue.value.toInt(),
                    comment = newGlucoseReadingComment.value
                )
            )
            resetNewReadingDialogFields()
        }
    }

    fun setNewGlucoseReadingTimestamp(newTimestamp: Long) {
        _newGlucoseReadingTimestamp.value = newTimestamp
    }

    fun setNewGlucoseReadingValue(newValue: String) {
        _newGlucoseReadingValue.value = newValue
    }

    fun setNewGlucoseReadingComment(comment: String) {
        _newGlucoseReadingComment.value = comment
    }

    fun setShowAddGlucoseReadingDialog(isVisible: Boolean) {
        _showAddGlucoseReadingDialog.value = isVisible
    }

    private fun resetNewReadingDialogFields() {
        _newGlucoseReadingTimestamp.value = System.currentTimeMillis()
        _newGlucoseReadingValue.value = ""
        _newGlucoseReadingComment.value = ""
    }
}