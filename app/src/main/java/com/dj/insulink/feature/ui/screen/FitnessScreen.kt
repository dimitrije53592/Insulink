package com.dj.insulink.feature.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.dj.insulink.R
import com.dj.insulink.core.ui.theme.dimens
import com.dj.insulink.feature.domain.models.Sport
import com.dj.insulink.feature.ui.components.AddSportsActivityDialog

@Composable
fun FitnessScreen(
    params: FitnessScreenParams
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimens.commonPadding16)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MaterialTheme.dimens.commonPadding16),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.fitness_screen_sports_list_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.commonSpacing8)
            ) {
                items(params.sports.value) { sport ->
                    SportActivityItem(sport = sport)
                }
            }
        }

        FloatingActionButton(
            onClick = {
                params.setShowSportsActivityDialog(true)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(MaterialTheme.dimens.commonPadding16),
            containerColor = Color(0xFF4A7BF6),
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = ""
            )
        }
    }
    Spacer(modifier = Modifier.size(MaterialTheme.dimens.commonSpacing16))
    if(params.showAddSportsActivityDialog.value){
        AddSportsActivityDialog(
            setShowAddExerciseDialog = params.setShowSportsActivityDialog,
            sportName = params.sportName,
            setSportName = params.setSportName,
            durationHours = params.durationHours,
            setDurationHours = params.setDurationHours,
            durationMinutes = params.durationMinutes,
            setDurationMinutes = params.setDurationMinutes,
            glucoseBefore = params.glucoseBefore,
            setGlucoseBefore = params.setGlucoseBefore,
            glucoseAfter = params.glucoseAfter,
            setGlucoseAfter = params.setGlucoseAfter,
            onAddExerciseClick = params.onAddExerciseClick
        )
    }
}

@Composable
private fun SportActivityItem(
    sport: Sport,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.dimens.commonElevation2)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.commonPadding16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = sport.name,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.size(MaterialTheme.dimens.commonSpacing12))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.commonSpacing8)
                ) {
                    MetricCard(
                        label = stringResource(R.string.fitness_screen_average_drop_label),
                        value = sport.avgDropPerHour,
                        labelColor = Color(0xFFA20000),
                        valueColor = Color(0xFFFF1B1B),
                        backgroundColor = Color(0xFFFFC2C2),
                        modifier = Modifier.weight(1f)
                    )

                    MetricCard(
                        label = stringResource(R.string.fitness_screen_last_drop_label),
                        value = sport.lastDropPerHour,
                        labelColor = Color(0xFFBD5F00),
                        valueColor = Color(0xFFF1861B),
                        backgroundColor = Color(0xFFFEDFC3),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    label: String,
    value: Int,
    labelColor: Color,
    valueColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.dimens.commonElevation0)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.commonPadding16),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = labelColor,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.labelLarge,
                color = valueColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

data class FitnessScreenParams(
    val sports: State<List<Sport>>,
    val showAddSportsActivityDialog: State<Boolean>,
    val setShowSportsActivityDialog: (Boolean) -> Unit,
    val sportName: State<String>,
    val setSportName: (String) -> Unit,
    val durationHours: State<String>,
    val setDurationHours: (String) -> Unit,
    val durationMinutes: State<String>,
    val setDurationMinutes: (String) -> Unit,
    val glucoseBefore: State<String>,
    val setGlucoseBefore: (String) -> Unit,
    val glucoseAfter: State<String>,
    val setGlucoseAfter: (String) -> Unit,
    val onAddExerciseClick: () -> Unit
)