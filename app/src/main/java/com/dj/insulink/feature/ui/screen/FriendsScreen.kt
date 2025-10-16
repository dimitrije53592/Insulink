package com.dj.insulink.feature.ui.screen

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.dj.insulink.R
import com.dj.insulink.core.ui.theme.dimens
import com.dj.insulink.feature.domain.models.GlucoseReading
import com.dj.insulink.feature.ui.components.FriendsListItem

@Composable
fun FriendsScreen(
    params: FriendsScreenParams
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Button(
            onClick = {

            },
            shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.dimens.commonPadding12,
                    vertical = MaterialTheme.dimens.commonPadding24
                )
                .shadow(
                    elevation = MaterialTheme.dimens.commonPadding8,
                    shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12),
                    clip = false
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.dimens.commonPadding12)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_new_friends),
                    tint = Color.Unspecified,
                    contentDescription = ""
                )
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing12))
                Text(
                    text = stringResource(R.string.friends_screen_add_new_friend_button_label),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF4A7BF6),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing12))
        Text(
            text = stringResource(
                R.string.friends_screen_friends_list_label,
                params.friendsList.size
            ),
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.commonPadding12)
        )
        Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing12))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = MaterialTheme.dimens.commonPadding12)
        ) {
            params.friendsList.forEach {
                FriendsListItem(
                    friend = it,
                    glucoseReading = GlucoseReading(
                        id = 1,
                        userId = "1",
                        timestamp = 0L,
                        value = 155,
                        comment = ""
                    )
                )
            }
        }
    }
}

data class FriendsScreenParams(
    val friendsList: List<String>
)