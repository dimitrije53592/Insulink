package com.dj.insulink.feature.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.dj.insulink.R
import com.dj.insulink.core.ui.theme.dimens
import com.dj.insulink.feature.domain.models.Reminder

@Composable
fun RemindersScreen(
    params: RemindersScreenParams
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            ReminderSection(
                reminders = params.todayReminders,
                areUpcoming = false
            )
            Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing24))
            ReminderSection(
                reminders = params.upcomingReminders,
                areUpcoming = true
            )
        }
        FloatingActionButton(
            onClick = {

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


@Composable
private fun ReminderSection(
    reminders: List<Reminder>,
    areUpcoming: Boolean
) {
    Column {
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing12))
        Text(
            text = if (areUpcoming) {
                stringResource(R.string.reminders_screen_upcoming_label)
            } else {
                stringResource(R.string.reminders_screen_today_label)
            },
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = MaterialTheme.dimens.commonPadding16)
        )
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing12))
        reminders.forEach {
            ReminderListItem(
                reminder = it,
                isUpcoming = areUpcoming
            )
            Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
        }
    }
}

@Composable
private fun ReminderListItem(
    reminder: Reminder,
    isUpcoming: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.commonPadding12)
            .clip(RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12))
            .background(Color.Gray.copy(alpha = if (isUpcoming) 0.2f else 0f))
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
                text = reminder.time,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Spacer(Modifier.weight(1f))
        Icon(
            painter = if (reminder.isDone) {
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

data class RemindersScreenParams(
    val todayReminders: List<Reminder>,
    val upcomingReminders: List<Reminder>
)