package com.dj.insulink.feature.fitness.ui.wrapper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dj.insulink.auth.domain.models.User
import com.dj.insulink.feature.fitness.ui.FitnessScreen
import com.dj.insulink.feature.fitness.ui.FitnessScreenParams
import com.dj.insulink.feature.fitness.ui.viewmodel.FitnessViewModel

@Composable
fun FitnessWrapper(
    currentUser: User?
) {
    val viewModel: FitnessViewModel = hiltViewModel()

    val calculatedSports = viewModel.calculatedSports.collectAsStateWithLifecycle()
    val showAddSportsActivityDialog = viewModel.showAddSportsActivityDialog.collectAsStateWithLifecycle()
    val activityName = viewModel.activityName.collectAsStateWithLifecycle()
    val durationHours = viewModel.durationHours.collectAsStateWithLifecycle()
    val durationMinutes = viewModel.durationMinutes.collectAsStateWithLifecycle()
    val glucoseBefore = viewModel.glucoseBefore.collectAsStateWithLifecycle()
    val glucoseAfter = viewModel.glucoseAfter.collectAsStateWithLifecycle()

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let {
            viewModel.fetchAllExercisesForUserAndUpdateDatabase(it)
        }
    }

    FitnessScreen(
        params = FitnessScreenParams(
            sports = calculatedSports,
            showAddSportsActivityDialog = showAddSportsActivityDialog,
            setShowSportsActivityDialog = viewModel::setShowSportsActivityDialog,
            sportName = activityName,
            setSportName = viewModel::setActivityName,
            durationHours = durationHours,
            setDurationHours = viewModel::setDurationHours,
            durationMinutes = durationMinutes,
            setDurationMinutes = viewModel::setDurationMinutes,
            glucoseBefore = glucoseBefore,
            setGlucoseBefore = viewModel::setGlucoseBefore,
            glucoseAfter = glucoseAfter,
            setGlucoseAfter = viewModel::setGlucoseAfter,
            onAddExerciseClick = {
                viewModel.onAddExerciseClick(currentUser?.uid)
            }
        )
    )
}