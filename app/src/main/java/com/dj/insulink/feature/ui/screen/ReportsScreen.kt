package com.dj.insulink.feature.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dj.insulink.R
import com.dj.insulink.core.ui.theme.dimens
import com.dj.insulink.feature.domain.models.GlucoseReading
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ReportsScreen(
    params: ReportsScreenParams
) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    val minDateString = remember(params.selectedMinDate.value) {
        params.selectedMinDate.value?.let {
            dateFormatter.format(Date(it))
        }
    }
    val maxDateString = remember(params.selectedMaxDate.value) {
        params.selectedMaxDate.value?.let {
            dateFormatter.format(Date(it))
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var isFirstOpen by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis = params.maxDate.value ?: System.currentTimeMillis(),
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        val minDateConstraint = params.minDate.value?.let { minTimestamp ->
                            val calendar = Calendar.getInstance().apply {
                                timeInMillis = minTimestamp
                                set(Calendar.HOUR_OF_DAY, 0)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }
                            calendar.timeInMillis
                        } ?: Long.MIN_VALUE

                        val maxDateConstraint = params.maxDate.value ?: System.currentTimeMillis()

                        return utcTimeMillis >= minDateConstraint &&
                                utcTimeMillis <= maxDateConstraint &&
                                utcTimeMillis <= System.currentTimeMillis()
                    }
                }
            )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            if (isFirstOpen) {
                                params.updateDateRange(millis, params.selectedMaxDate.value!!)
                            } else {
                                params.updateDateRange(params.selectedMinDate.value!!, millis)
                            }
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
            DatePicker(state = datePickerState)
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_generate_report),
                tint = Color.Unspecified,
                contentDescription = ""
            )
        }
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing12))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.report_screen_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing12))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.commonPadding48)
        ) {
            Text(
                text = stringResource(R.string.report_screen_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing64))
        Text(
            text = stringResource(R.string.report_screen_start_date_label),
            modifier = Modifier.padding(start = MaterialTheme.dimens.commonPadding16)
        )
        OutlinedButton(
            onClick = {
                showDatePicker = true
                isFirstOpen = true
            },
            shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.commonPadding12)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = minDateString ?: "",
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing12))
        Text(
            text = stringResource(R.string.report_screen_end_date_label),
            modifier = Modifier.padding(start = MaterialTheme.dimens.commonPadding16)
        )
        OutlinedButton(
            onClick = {
                showDatePicker = true
                isFirstOpen = false
            },
            shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.commonPadding12)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = maxDateString ?: "",
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing64))
        Button(
            onClick = {

            },
            shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
            border = BorderStroke(MaterialTheme.dimens.commonButtonBorder1, Color.LightGray),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEDEDED)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.commonPadding12)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_eye),
                    tint = Color(0xFFB2B2B2),
                    contentDescription = "",
                    modifier = Modifier.size(MaterialTheme.dimens.buttonIconSize)
                )
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
                Text(
                    text = stringResource(R.string.report_screen_preview_report_button_label),
                    color = Color(0xFFB2B2B2)
                )
            }
        }
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing20))
        Button(
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.commonPadding12)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF4A7BF6),
                            Color(0xFF8A5CF5)
                        )
                    ),
                    shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_download),
                    tint = Color.Unspecified,
                    contentDescription = ""
                )
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
                Text(
                    text = stringResource(R.string.report_screen_generate_button_label),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class ReportsScreenParams(
    val minDate: State<Long?>,
    val maxDate: State<Long?>,
    val selectedMinDate: State<Long?>,
    val selectedMaxDate: State<Long?>,
    val updateDateRange: (Long, Long) -> Unit,
    val filteredReadings: State<List<GlucoseReading>>
)