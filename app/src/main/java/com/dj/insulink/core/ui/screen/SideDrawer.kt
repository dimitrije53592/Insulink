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
import com.dj.insulink.core.ui.theme.dimens
import com.dj.insulink.feature.ui.components.SideDrawerListItem

@Composable
fun SideDrawer(
    params: SideDrawerParams
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth(SIDE_DRAWER_WIDTH_RATIO)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing80))
            Text(
                text = "${params.currentUser.value?.firstName ?: ""} ${params.currentUser.value?.lastName ?: ""}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${params.currentUser.value?.email}",
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing12))
            HorizontalDivider(modifier = Modifier.fillMaxWidth(SIDE_DRAWER_WIDTH_RATIO))
            Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing32))
            SideDrawerListItem(
                title = stringResource(R.string.side_drawer_reminders_title),
                subtitle = stringResource(R.string.side_drawer_reminders_subtitle),
                icon = painterResource(R.drawable.ic_reminders),
                onClick = params.navigateToReminders
            )
            Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing16))
            SideDrawerListItem(
                title = stringResource(R.string.side_drawer_friends_title),
                subtitle = stringResource(R.string.side_drawer_friends_subtitle),
                icon = painterResource(R.drawable.ic_friends),
                onClick = params.navigateToFriends
            )
            Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing16))
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
                shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEDEDED)
                ),
                border = BorderStroke(MaterialTheme.dimens.commonButtonBorder1, Color(0xFFB2B2B2)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.commonPadding48)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    tint = Color(0xFFB2B2B2),
                    contentDescription = ""
                )
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
                Text(
                    text = stringResource(R.string.side_drawer_sign_out_button_label),
                    color = Color(0xFF8A5CF5)
                )
            }
            Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing80))
        }
    }
}

data class SideDrawerParams(
    val currentUser: State<User?>,
    val navigateToReminders: () -> Unit,
    val navigateToFriends: () -> Unit,
    val navigateToReports: () -> Unit,
    val onSignOutClick: () -> Unit,
)

private const val SIDE_DRAWER_WIDTH_RATIO = 0.8f