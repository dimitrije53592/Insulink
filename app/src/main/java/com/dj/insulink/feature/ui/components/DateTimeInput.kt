package com.dj.insulink.feature.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.dj.insulink.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeInput(
    selectedTimestamp: Long,
    onTimestampSelected: (Long) -> Unit
) {
    // State to manage dialog visibility
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // State to hold the temporary date selection before time is picked
    var tempDateSelection by remember { mutableStateOf(selectedTimestamp) }

    // Formatters for display
    val dateFormatter = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    // Convert the current Long timestamp to Date objects for display
    val date = Date(selectedTimestamp)

    // --- Display Row ---
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Date Input
        InputChip(
            selected = false,
            onClick = { showDatePicker = true },
            label = { Text(dateFormatter.format(date)) },
            leadingIcon = { Icon(Icons.Filled.CalendarToday, contentDescription = stringResource(R.string.select_date_label)) }
        )

        Spacer(Modifier.size(8.dp))

        // Time Input
        InputChip(
            selected = false,
            onClick = { showTimePicker = true },
            label = { Text(timeFormatter.format(date)) },
            leadingIcon = { Icon(Icons.Filled.Schedule, contentDescription = stringResource(R.string.select_time_label)) }
        )
    }

    // --- 1. Date Picker Dialog ---
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedTimestamp
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis ?: selectedTimestamp

                        // 1. Get the selected date part (day/month/year)
                        val calendar = Calendar.getInstance().apply { timeInMillis = selectedDateMillis }

                        // 2. Apply the existing time part from the original timestamp
                        val originalCalendar = Calendar.getInstance().apply { timeInMillis = selectedTimestamp }
                        calendar.set(Calendar.HOUR_OF_DAY, originalCalendar.get(Calendar.HOUR_OF_DAY))
                        calendar.set(Calendar.MINUTE, originalCalendar.get(Calendar.MINUTE))
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MILLISECOND, 0)

                        // Store the new date + old time, then open the time picker next
                        tempDateSelection = calendar.timeInMillis
                        showDatePicker = false
                        showTimePicker = true
                    }
                ) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // --- 2. Time Picker Dialog ---
    if (showTimePicker) {
        // Initialize Time Picker with the time component of the temporary selection
        val initialCalendar = Calendar.getInstance().apply { timeInMillis = tempDateSelection }
        val timePickerState = rememberTimePickerState(
            initialHour = initialCalendar.get(Calendar.HOUR_OF_DAY),
            initialMinute = initialCalendar.get(Calendar.MINUTE)
        )

        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val finalCalendar = Calendar.getInstance().apply { timeInMillis = tempDateSelection }

                        // Apply the selected hour and minute to the selected date
                        finalCalendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        finalCalendar.set(Calendar.MINUTE, timePickerState.minute)
                        finalCalendar.set(Calendar.SECOND, 0)
                        finalCalendar.set(Calendar.MILLISECOND, 0)

                        // Finalize and update the ViewModel's state
                        onTimestampSelected(finalCalendar.timeInMillis)
                        showTimePicker = false
                    }
                ) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

// A simple wrapper for TimePicker because it doesn't have a Dialog component in M3 yet
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit = {},
    dismissButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                stringResource(R.string.time_picker_title),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = content,
        confirmButton = confirmButton,
        dismissButton = dismissButton
    )
}