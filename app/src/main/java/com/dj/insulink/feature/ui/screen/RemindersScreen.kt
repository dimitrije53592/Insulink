package com.dj.insulink.feature.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dj.insulink.R
import com.dj.insulink.core.ui.theme.dimens
import com.dj.insulink.core.utils.combineTimeWithDate
import com.dj.insulink.feature.domain.models.Reminder
import com.dj.insulink.feature.domain.models.ReminderType
import com.dj.insulink.feature.ui.components.GlucoseDropdownMenu
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RemindersScreen(
    params: RemindersScreenParams
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing12))

        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(params.reminders, key = { item -> item.id }) {
                    ReminderListItem(
                        reminder = it,
                        onSwipeFromStartToEnd = params.onSwipeFromStartToEnd
                    )
                    Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
                }
            }

            FloatingActionButton(
                onClick = {
                    params.setReminderTime(System.currentTimeMillis())
                    params.setShowAddReminderDialog(true)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(MaterialTheme.dimens.commonPadding16),
                containerColor = Color(0xFF4A7BF6)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    tint = Color.White,
                    contentDescription = ""
                )
            }
        }
    }
    if (params.showAddReminderDialog.value) {
        AddReminderDialog(
            setShowAddReminderDialog = params.setShowAddReminderDialog,
            reminderTitle = params.reminderTitle,
            setReminderTitle = params.setReminderTitle,
            reminderType = params.reminderType,
            setReminderType = params.setReminderType,
            reminderTime = params.reminderTime,
            setReminderTime = params.setReminderTime,
            onAddReminderClick = params.onAddReminderClick
        )
    }
}

@Composable
private fun ReminderListItem(
    reminder: Reminder,
    onSwipeFromStartToEnd: (Reminder) -> Unit
) {
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    var hasBeenSwiped by remember { mutableStateOf(false) }
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.StartToEnd && !hasBeenSwiped) {
                hasBeenSwiped = true
                onSwipeFromStartToEnd(reminder)
                true
            } else {
                false
            }
        },
        positionalThreshold = { it * 0.25f }
    )

    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        enableDismissFromEndToStart = false,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.commonPadding12),
        backgroundContent = {}
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.commonPadding12)
                .clip(RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12))
                .border(
                    BorderStroke(MaterialTheme.dimens.commonButtonBorder1, Color.LightGray),
                    RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12)
                )
        ) {
            Icon(
                painter = painterResource(reminder.reminderType.icon),
                tint = Color.Unspecified,
                contentDescription = "",
                modifier = Modifier.padding(MaterialTheme.dimens.commonPadding8)
            )
            Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing12))
            Column {
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = timeFormatter.format(Date(reminder.time)),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
            Spacer(Modifier.weight(1f))
            Icon(
                painter = if (reminder.isDoneForToday) {
                    painterResource(R.drawable.ic_done)
                } else {
                    painterResource(R.drawable.ic_upcoming)
                },
                tint = Color.Unspecified,
                contentDescription = "",
                modifier = Modifier
                    .size(MaterialTheme.dimens.reminderIconSize)
                    .padding(MaterialTheme.dimens.commonPadding16)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddReminderDialog(
    setShowAddReminderDialog: (Boolean) -> Unit,
    reminderTitle: State<String>,
    setReminderTitle: (String) -> Unit,
    reminderType: State<ReminderType>,
    setReminderType: (ReminderType) -> Unit,
    reminderTime: State<Long>,
    setReminderTime: (Long) -> Unit,
    onAddReminderClick: () -> Unit
) {
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val timeString = remember(reminderTime.value) {
        timeFormatter.format(
            Date(reminderTime.value)
        )
    }
    var showTimePicker by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { setShowAddReminderDialog(false) }) {
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
                        text = stringResource(R.string.reminders_screen_add_new_reminder_dialog_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing24))
                OutlinedTextField(
                    value = reminderTitle.value,
                    onValueChange = { newValue ->
                        setReminderTitle(newValue)
                    },
                    label = { Text(stringResource(R.string.reminders_screen_add_new_reminder_title_label)) },
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
                    text = stringResource(R.string.reminders_screen_add_new_reminder_select_type_label)
                )
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing4))
                GlucoseDropdownMenu(
                    items = ReminderType.entries.map { it.displayName },
                    selectedItem = reminderType.value.displayName,
                    onItemSelected = {
                        setReminderType(
                            ReminderType.fromDisplayName(it) ?: ReminderType.MEAL_REMINDER
                        )
                    }
                )
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing16))
                Text(
                    text = stringResource(R.string.reminders_screen_add_new_reminder_select_time_label)
                )
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing4))
                TextButton(
                    onClick = {
                        showTimePicker = true
                    },
                    border = BorderStroke(
                        MaterialTheme.dimens.commonButtonBorder1,
                        Color.Black
                    ),
                    shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$timeString",
                        color = Color.Black
                    )
                }
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing16))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { setShowAddReminderDialog(false) }) {
                        Text(
                            text = stringResource(R.string.new_reading_cancel),
                            color = Color.Black
                        )
                    }
                    Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
                    Button(
                        onClick = {
                            onAddReminderClick()
                            setShowAddReminderDialog(false)
                        },
                        enabled = reminderTitle.value.isNotEmpty(),
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
    if (showTimePicker) {
        val calendar = Calendar.getInstance().apply { timeInMillis = reminderTime.value }
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
                        text = stringResource(R.string.reminders_screen_add_new_reminder_select_time_label),
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
                                setReminderTime(
                                    combineTimeWithDate(
                                        hour = timePickerState.hour,
                                        minute = timePickerState.minute,
                                        existingTimestamp = reminderTime.value
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
}

data class RemindersScreenParams(
    val reminders: List<Reminder>,
    val showAddReminderDialog: State<Boolean>,
    val setShowAddReminderDialog: (Boolean) -> Unit,
    val reminderTitle: State<String>,
    val setReminderTitle: (String) -> Unit,
    val reminderType: State<ReminderType>,
    val setReminderType: (ReminderType) -> Unit,
    val reminderTime: State<Long>,
    val setReminderTime: (Long) -> Unit,
    val onSwipeFromStartToEnd: (Reminder) -> Unit,
    val onAddReminderClick: () -> Unit
)