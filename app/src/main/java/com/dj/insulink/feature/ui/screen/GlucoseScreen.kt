package com.dj.insulink.feature.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.dj.insulink.R
import com.dj.insulink.core.ui.theme.dimens
import com.dj.insulink.feature.ui.components.AddGlucoseReadingDialog
import com.dj.insulink.feature.ui.components.GlucoseDropdownMenu

@Composable
fun GlucoseScreen(
    params: GlucoseScreenParams
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.commonPadding12)
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
                Column(
                    modifier = Modifier
                        .padding(vertical = MaterialTheme.dimens.commonPadding16)
                        .padding(start = MaterialTheme.dimens.commonPadding24)
                ) {
                    Text(
                        text = stringResource(R.string.glucose_screen_latest_reading_label),
                        color = Color.White
                    )
                    Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
                    Text(
                        text = "0 mg/dL",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
                    Text(
                        text = "TIME PLACEHOLDER",
                        color = Color.White
                    )
                    Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
                    Row {
                        Icon(
                            imageVector = Icons.Filled.Circle,
                            tint = Color.Yellow,
                            contentDescription = ""
                        )
                        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
                        Text(
                            text = "LEVEL PLACEHOLDER",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            GlucoseDropdownMenu(
                items = listOf("24h", "48h", "72h"),
                selectedItem = "24h",
                onItemSelected = {

                },
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.commonPadding12)
            )
        }
        FloatingActionButton(
            onClick = {
                params.setShowAddGlucoseReadingDialog(true)
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
    if (params.showAddGlucoseReadingDialog.value) {
        AddGlucoseReadingDialog(
            newGlucoseReadingTimestamp = params.newGlucoseReadingTimestamp,
            setNewGlucoseReadingTimestamp = params.setNewGlucoseReadingTimestamp,
            newGlucoseReadingValue = params.newGlucoseReadingValue,
            setNewGlucoseReadingValue = params.setNewGlucoseReadingValue,
            newGlucoseReadingComment = params.newGlucoseReadingComment,
            setNewGlucoseReadingComment = params.setNewGlucoseReadingComment,
            onDismissRequest = {
                params.setShowAddGlucoseReadingDialog(false)
            },
            onSaveClicked = {
                params.submitNewGlucoseReading()
            }
        )
    }
}

data class GlucoseScreenParams(
    val newGlucoseReadingTimestamp: State<Long>,
    val setNewGlucoseReadingTimestamp: (Long) -> Unit,
    val newGlucoseReadingValue: State<String>,
    val setNewGlucoseReadingValue: (String) -> Unit,
    val newGlucoseReadingComment: State<String>,
    val setNewGlucoseReadingComment: (String) -> Unit,
    val showAddGlucoseReadingDialog: State<Boolean>,
    val setShowAddGlucoseReadingDialog: (Boolean) -> Unit,
    val submitNewGlucoseReading: () -> Unit
)