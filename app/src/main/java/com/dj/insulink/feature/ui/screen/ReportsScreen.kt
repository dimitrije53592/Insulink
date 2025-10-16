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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.dj.insulink.R
import com.dj.insulink.core.ui.theme.dimens

@Composable
fun ReportsScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxHeight()
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
                    text = "01-01-2024",
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
                    text = "01-01-2024",
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