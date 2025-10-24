package com.dj.insulink.core.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.dj.insulink.R
import com.dj.insulink.auth.domain.models.User
import com.dj.insulink.core.ui.theme.InsulinkTheme
import com.dj.insulink.feature.ui.components.SideDrawerListItem

@Composable
fun SideDrawer(
    params: SideDrawerParams
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth(SIDE_DRAWER_WIDTH_RATIO)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.size(InsulinkTheme.dimens.commonSpacing80))
            Text(
                text = "${params.currentUser?.firstName ?: ""} ${params.currentUser?.lastName ?: ""}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = params.currentUser?.email ?: "",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.size(InsulinkTheme.dimens.commonSpacing12))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(SIDE_DRAWER_WIDTH_RATIO),
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(Modifier.size(InsulinkTheme.dimens.commonSpacing32))
            SideDrawerListItem(
                title = stringResource(R.string.side_drawer_reminders_title),
                subtitle = stringResource(R.string.side_drawer_reminders_subtitle),
                icon = painterResource(R.drawable.ic_reminders),
                onClick = params.navigateToReminders
            )
            Spacer(Modifier.size(InsulinkTheme.dimens.commonSpacing16))
            SideDrawerListItem(
                title = stringResource(R.string.side_drawer_friends_title),
                subtitle = stringResource(R.string.side_drawer_friends_subtitle),
                icon = painterResource(R.drawable.ic_friends),
                onClick = params.navigateToFriends
            )
            Spacer(Modifier.size(InsulinkTheme.dimens.commonSpacing16))
            SideDrawerListItem(
                title = stringResource(R.string.side_drawer_reports_title),
                subtitle = stringResource(R.string.side_drawer_reports_subtitle),
                icon = painterResource(R.drawable.ic_reports),
                onClick = params.navigateToReports
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    params.onSignOutClick()
                },
                shape = RoundedCornerShape(InsulinkTheme.dimens.commonButtonRadius12),
                colors = ButtonDefaults.buttonColors(
                    containerColor = InsulinkTheme.colors.backgroundSecondary
                ),
                border = BorderStroke(InsulinkTheme.dimens.commonButtonBorder1, MaterialTheme.colorScheme.outline),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = InsulinkTheme.dimens.commonPadding48)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    tint = MaterialTheme.colorScheme.outline,
                    contentDescription = ""
                )
                Spacer(Modifier.size(InsulinkTheme.dimens.commonSpacing8))
                Text(
                    text = stringResource(R.string.side_drawer_sign_out_button_label),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.size(InsulinkTheme.dimens.commonSpacing80))
        }
    }
}

data class SideDrawerParams(
    val currentUser: User?,
    val navigateToReminders: () -> Unit,
    val navigateToFriends: () -> Unit,
    val navigateToReports: () -> Unit,
    val onSignOutClick: () -> Unit,
)

private const val SIDE_DRAWER_WIDTH_RATIO = 0.8f