package com.dj.insulink.feature.ui.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.dj.insulink.R
import com.dj.insulink.core.ui.theme.dimens

@Composable
fun AddSportsActivityDialog(
    setShowAddExerciseDialog: (Boolean) -> Unit,
    sportName: State<String>,
    setSportName: (String) -> Unit,
    durationHours: State<String>,
    setDurationHours: (String) -> Unit,
    durationMinutes: State<String>,
    setDurationMinutes: (String) -> Unit,
    glucoseBefore: State<String>,
    setGlucoseBefore: (String) -> Unit,
    glucoseAfter: State<String>,
    setGlucoseAfter: (String) -> Unit,
    onAddExerciseClick: () -> Unit
) {
    Dialog(onDismissRequest = { setShowAddExerciseDialog(false) }) {
        Card(
            shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12)
        ) {
            Column(
                modifier = Modifier.padding(MaterialTheme.dimens.commonPadding24),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.fitness_screen_add_new_exercise_dialog_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing24))
                OutlinedTextField(
                    value = sportName.value,
                    onValueChange = { newValue ->
                        setSportName(newValue)
                    },
                    label = { Text(stringResource(R.string.fitness_screen_add_sport_name_label)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
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
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing16))
                Text(
                    text = stringResource(R.string.fitness_screen_add_duration_label)
                )
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing4))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.commonSpacing8)
                ) {
                    OutlinedTextField(
                        value = durationHours.value,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                                setDurationHours(newValue)
                            }
                        },
                        label = { Text(stringResource(R.string.fitness_screen_hours_label)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
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
                    OutlinedTextField(
                        value = durationMinutes.value,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) {
                                val minutes = newValue.toIntOrNull() ?: 0
                                if (minutes <= 59) {
                                    setDurationMinutes(newValue)
                                }
                            }
                        },
                        label = { Text(stringResource(R.string.fitness_screen_minutes_label)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
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
                }
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing16))
                OutlinedTextField(
                    value = glucoseBefore.value,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= 5) {
                            setGlucoseBefore(newValue)
                        }
                    },
                    label = { Text(stringResource(R.string.fitness_screen_glucose_before_label)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
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
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing16))
                OutlinedTextField(
                    value = glucoseAfter.value,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= 5) {
                            setGlucoseAfter(newValue)
                        }
                    },
                    label = { Text(stringResource(R.string.fitness_screen_glucose_after_label)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
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
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing16))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { setShowAddExerciseDialog(false) }) {
                        Text(
                            text = stringResource(R.string.new_reading_cancel),
                            color = Color.Black
                        )
                    }
                    Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
                    Button(
                        onClick = {
                            onAddExerciseClick()
                            setShowAddExerciseDialog(false)
                        },
                        enabled = sportName.value.isNotEmpty() &&
                                glucoseBefore.value.isNotEmpty() &&
                                glucoseAfter.value.isNotEmpty() &&
                                (durationHours.value.isNotEmpty() || durationMinutes.value.isNotEmpty()),
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