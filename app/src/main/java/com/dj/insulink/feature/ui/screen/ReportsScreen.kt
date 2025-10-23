package com.dj.insulink.feature.ui.screen

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.dj.insulink.R
import com.dj.insulink.core.ui.theme.dimens
import com.dj.insulink.feature.domain.models.GlucoseReading
import com.dj.insulink.feature.ui.viewmodel.PdfGenerationState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ReportsScreen(
    params: ReportsScreenParams
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val shareFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { /* Handle result if needed */ }


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

    LaunchedEffect(params.pdfGenerationState.value) {
        when (params.pdfGenerationState.value) {
            is PdfGenerationState.Success -> {
                snackbarHostState.showSnackbar("PDF generated successfully!")
            }

            is PdfGenerationState.Error -> {
                snackbarHostState.showSnackbar("Error: ${(params.pdfGenerationState.value as PdfGenerationState.Error).message}")
            }

            else -> { /* No action needed */
            }
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var isFirstOpen by remember { mutableStateOf(false) }
    var showPdfPreview by remember { mutableStateOf(false) }

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
                        params.filterReadingsByCurrentDateRange()
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
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing32))
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
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing32))
        Button(
            onClick = {
                if (params.pdfGenerationState.value is PdfGenerationState.Success) {
                    sharePdfFile(
                        context,
                        (params.pdfGenerationState.value as PdfGenerationState.Success).file,
                        shareFileLauncher
                    )
                } else {
                    params.generatePdfReport()
                }
            },
            shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
            border = BorderStroke(MaterialTheme.dimens.commonButtonBorder1, Color.LightGray),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEDEDED)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.dimens.commonPadding12
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.dimens.commonPadding4)
            ) {
                if (params.pdfGenerationState.value is PdfGenerationState.Generating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        strokeWidth = 2.dp,
                        color = Color(0xFFB2B2B2)
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_download),
                        tint = Color(0xFFB2B2B2),
                        contentDescription = "",
                        modifier = Modifier.size(MaterialTheme.dimens.buttonIconSize)
                    )
                    Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
                    Text(
                        text = when (params.pdfGenerationState.value) {
                            is PdfGenerationState.Generating -> "Generating..."
                            else -> stringResource(R.string.report_screen_preview_report_button_label)
                        },
                        color = Color(0xFFB2B2B2)
                    )
                }
            }
        }
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing20))
        Button(
            onClick = {
                if (params.pdfGenerationState.value is PdfGenerationState.Success) {
                    showPdfPreview = true
                }
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
                    painter = painterResource(R.drawable.ic_eye),
                    tint = Color.White,
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
    if (showPdfPreview && params.pdfGenerationState.value is PdfGenerationState.Success) {
        PdfPreviewDialog(
            pdfFile = (params.pdfGenerationState.value as PdfGenerationState.Success).file,
            onDismiss = { showPdfPreview = false }
        )
    }
}

data class ReportsScreenParams(
    val minDate: State<Long?>,
    val maxDate: State<Long?>,
    val selectedMinDate: State<Long?>,
    val selectedMaxDate: State<Long?>,
    val updateDateRange: (Long, Long) -> Unit,
    val filteredReadings: State<List<GlucoseReading>>,
    val pdfGenerationState: State<PdfGenerationState>,
    val filterReadingsByCurrentDateRange: () -> Unit,
    val generatePdfReport: () -> Unit
)

private fun sharePdfFile(
    context: android.content.Context,
    file: File,
    launcher: androidx.activity.result.ActivityResultLauncher<Intent>
) {
    try {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Glucose Report")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        launcher.launch(Intent.createChooser(shareIntent, "Share PDF Report"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
private fun PdfPreviewDialog(
    pdfFile: File,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(pdfFile) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            }

            context.startActivity(Intent.createChooser(intent, "Open PDF"))
            onDismiss()
        } catch (e: Exception) {
            e.printStackTrace()
            onDismiss()
        }
    }
}