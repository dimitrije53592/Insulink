package com.dj.insulink.feature.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.dj.insulink.core.ui.theme.dimens
import com.dj.insulink.R
import com.dj.insulink.core.utils.combineDateAndTime
import com.dj.insulink.core.utils.combineTimeWithDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGlucoseReadingDialog(
    newGlucoseReadingTimestamp: State<Long>,
    setNewGlucoseReadingTimestamp: (Long) -> Unit,
    newGlucoseReadingValue: State<String>,
    setNewGlucoseReadingValue: (String) -> Unit,
    newGlucoseReadingComment: State<String>,
    setNewGlucoseReadingComment: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onSaveClicked: () -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    val dateString = remember(newGlucoseReadingTimestamp.value) {
        dateFormatter.format(
            Date(newGlucoseReadingTimestamp.value)
        )
    }
    val timeString = remember(newGlucoseReadingTimestamp.value) {
        timeFormatter.format(
            Date(newGlucoseReadingTimestamp.value)
        )
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis = newGlucoseReadingTimestamp.value,
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return utcTimeMillis <= System.currentTimeMillis()
                    }
                }
            )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            setNewGlucoseReadingTimestamp(
                                combineDateAndTime(
                                    dateMillis = millis,
                                    existingTimestamp = newGlucoseReadingTimestamp.value
                                )
                            )
                        }
                        showDatePicker = false
                    }
                ) {
                    Text(stringResource(R.string.new_reading_ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.new_reading_cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState, )
        }
    }

    if (showTimePicker) {
        val calendar =
            Calendar.getInstance().apply { timeInMillis = newGlucoseReadingTimestamp.value }
        val timePickerState = rememberTimePickerState(
            initialHour = calendar.get(Calendar.HOUR_OF_DAY),
            initialMinute = calendar.get(Calendar.MINUTE),
            is24Hour = true
        )

        Dialog(onDismissRequest = { showTimePicker = false }) {
            Card(
                shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12)
            ) {
                Column(
                    modifier = Modifier.padding(MaterialTheme.dimens.commonPadding24),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select Time",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing16))
                    TimePicker(state = timePickerState)
                    Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing16))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showTimePicker = false }) {
                            Text(
                                text = stringResource(R.string.new_reading_cancel),
                                color = Color.Black
                            )
                        }
                        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
                        TextButton(
                            onClick = {
                                setNewGlucoseReadingTimestamp(
                                    combineTimeWithDate(
                                        hour = timePickerState.hour,
                                        minute = timePickerState.minute,
                                        existingTimestamp = newGlucoseReadingTimestamp.value
                                    )
                                )
                                showTimePicker = false
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.new_reading_ok),
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.commonPadding16),
            shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
        ) {
            Column(
                modifier = Modifier.padding(MaterialTheme.dimens.commonPadding24),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.new_reading_dialog_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing24))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            showDatePicker = true
                        },
                        border = BorderStroke(
                            MaterialTheme.dimens.commonButtonBorder1,
                            Color.Black
                        ),
                        shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius4),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "$dateString",
                            color = Color.Black
                        )
                    }
                    Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
                    TextButton(
                        onClick = {
                            showTimePicker = true
                        },
                        border = BorderStroke(
                            MaterialTheme.dimens.commonButtonBorder1,
                            Color.Black
                        ),
                        shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius4),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "$timeString",
                            color = Color.Black
                        )
                    }
                }
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing12))
                OutlinedTextField(
                    value = newGlucoseReadingValue.value,
                    onValueChange = { newValue ->
                        setNewGlucoseReadingValue(newValue.filter { it.isDigit() })
                    },
                    label = { Text(stringResource(R.string.new_reading_text_field_label)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        disabledTextColor = Color.Black,
                        errorTextColor = Color.Black,

                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        disabledLabelColor = Color.Black,
                        errorLabelColor = Color.Red,

                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        disabledBorderColor = Color.Black,
                        errorBorderColor = Color.Red,

                        cursorColor = Color.Black,
                        errorCursorColor = Color.Red,

                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,

                        focusedPlaceholderColor = Color.Black,
                        unfocusedPlaceholderColor = Color.Black,
                        disabledPlaceholderColor = Color.Black,
                        errorPlaceholderColor = Color.Black
                    )
                )
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing12))
                OutlinedTextField(
                    value = newGlucoseReadingComment.value,
                    onValueChange = { newValue ->
                        setNewGlucoseReadingComment(newValue)
                    },
                    label = { Text(stringResource(R.string.new_reading_comment_label)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        disabledTextColor = Color.Black,
                        errorTextColor = Color.Black,

                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        disabledLabelColor = Color.Black,
                        errorLabelColor = Color.Red,

                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        disabledBorderColor = Color.Black,
                        errorBorderColor = Color.Red,

                        cursorColor = Color.Black,
                        errorCursorColor = Color.Red,

                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,

                        focusedPlaceholderColor = Color.Black,
                        unfocusedPlaceholderColor = Color.Black,
                        disabledPlaceholderColor = Color.Black,
                        errorPlaceholderColor = Color.Black
                    )
                )
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing24))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(
                            text = stringResource(R.string.new_reading_cancel),
                            color = Color.Black
                        )
                    }
                    Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
                    Button(
                        onClick = {
                            if (newGlucoseReadingValue.value.isNotEmpty()) {
                                onSaveClicked()
                                onDismissRequest()
                            }
                        },
                        enabled = newGlucoseReadingValue.value.isNotEmpty(),
                        modifier = Modifier.background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF4A7BF6),
                                    Color(0xFF8A5CF5)
                                )
                            ),
                            shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12)
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.new_reading_save),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}