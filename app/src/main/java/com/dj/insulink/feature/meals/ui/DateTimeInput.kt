package com.dj.insulink.feature.meals.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.dj.insulink.R
import com.dj.insulink.core.ui.theme.InsulinkTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeInput(
    selectedTimestamp: Long,
    onTimestampSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var tempDateSelection by remember { mutableStateOf(selectedTimestamp) }

    val dateFormatter = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val date = Date(selectedTimestamp)

    val chipShape = RoundedCornerShape(InsulinkTheme.dimens.commonButtonRadius12)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(InsulinkTheme.dimens.commonSpacing12)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .height(InsulinkTheme.dimens.commonButtonHeight50)
                .clip(chipShape)
                .border(
                    InsulinkTheme.dimens.commonButtonBorder1,
                    MaterialTheme.colorScheme.outline,
                    chipShape
                )
                .clickable { showDatePicker = true },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Filled.CalendarToday,
                contentDescription = "",
                modifier = Modifier.size(InsulinkTheme.dimens.commonIconSize16),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.size(InsulinkTheme.dimens.commonSpacing8))
            Text(
                text = dateFormatter.format(date),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .height(InsulinkTheme.dimens.commonButtonHeight50)
                .clip(chipShape)
                .border(
                    InsulinkTheme.dimens.commonButtonBorder1,
                    MaterialTheme.colorScheme.outline,
                    chipShape
                )
                .clickable { showTimePicker = true },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Filled.Schedule,
                contentDescription = "",
                modifier = Modifier.size(InsulinkTheme.dimens.commonIconSize16),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.size(InsulinkTheme.dimens.commonSpacing8))
            Text(
                text = timeFormatter.format(date),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }

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
                        val calendar = Calendar.getInstance().apply { timeInMillis = selectedDateMillis }
                        val originalCalendar = Calendar.getInstance().apply { timeInMillis = selectedTimestamp }
                        calendar.set(Calendar.HOUR_OF_DAY, originalCalendar.get(Calendar.HOUR_OF_DAY))
                        calendar.set(Calendar.MINUTE, originalCalendar.get(Calendar.MINUTE))
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MILLISECOND, 0)

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

    if (showTimePicker) {
        val initialCalendar = Calendar.getInstance().apply { timeInMillis = tempDateSelection }
        val timePickerState = rememberTimePickerState(
            initialHour = initialCalendar.get(Calendar.HOUR_OF_DAY),
            initialMinute = initialCalendar.get(Calendar.MINUTE),
            is24Hour = true
        )

        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val finalCalendar = Calendar.getInstance().apply { timeInMillis = tempDateSelection }
                        finalCalendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        finalCalendar.set(Calendar.MINUTE, timePickerState.minute)
                        finalCalendar.set(Calendar.SECOND, 0)
                        finalCalendar.set(Calendar.MILLISECOND, 0)

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
