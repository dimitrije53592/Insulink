package com.dj.insulink.feature.reports.ui.wrapper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dj.insulink.auth.domain.models.User
import com.dj.insulink.feature.reports.ui.ReportsScreen
import com.dj.insulink.feature.reports.ui.ReportsScreenParams
import com.dj.insulink.feature.reports.ui.viewmodel.ReportsViewModel

@Composable
fun ReportsWrapper(
    currentUser: User?
) {
    val viewModel: ReportsViewModel = hiltViewModel()

    val minDate = viewModel.minDate.collectAsStateWithLifecycle()
    val maxDate = viewModel.maxDate.collectAsStateWithLifecycle()
    val selectedMinDate = viewModel.selectedMinDate.collectAsStateWithLifecycle()
    val selectedMaxDate = viewModel.selectedMaxDate.collectAsStateWithLifecycle()
    val pdfGenerationState = viewModel.pdfGenerationState.collectAsStateWithLifecycle()

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let {
            viewModel.initializeDateRange(it)
        }
    }

    currentUser?.let {
        ReportsScreen(
            params = ReportsScreenParams(
                minDate = minDate.value,
                maxDate = maxDate.value,
                selectedMinDate = selectedMinDate.value,
                selectedMaxDate = selectedMaxDate.value,
                updateDateRange = viewModel::updateDateRange,
                pdfGenerationState = pdfGenerationState.value,
                filterReadingsByCurrentDateRange = {
                    viewModel.filterReadingsByCurrentDateRange(it.uid)
                },
                generatePdfReport = {
                    viewModel.generatePdfReport(it.uid)
                }
            )
        )
    }
}