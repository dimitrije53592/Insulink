package com.dj.insulink.feature.ui.screen

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.dj.insulink.R
import com.dj.insulink.core.ui.theme.dimens
import com.dj.insulink.feature.domain.models.Friend
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
                params.setShowAddNewFriendDialog(true)
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
                params.friendsList.value.size
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
            params.friendsList.value.forEach {
                FriendsListItem(friend = it)
            }
        }
    }
    if (params.showAddNewFriendDialog.value) {
        AddNewFriendDialog(
            onDismissRequest = {
                params.setShowAddNewFriendDialog(false)
            },
            usersFriendCode = params.usersFriendCode,
            enteredCode = params.enteredCode,
            setEnteredCode = params.setEnteredCode,
            onAddFriendClick = params.onAddFriendClick
        )
    }
}

@Composable
private fun AddNewFriendDialog(
    onDismissRequest: () -> Unit,
    usersFriendCode: String,
    enteredCode: State<String>,
    setEnteredCode: (String) -> Unit,
    onAddFriendClick: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12)
        ) {
            Column(
                modifier = Modifier.padding(MaterialTheme.dimens.commonPadding24),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(R.string.friends_screen_friend_code_label)
                )
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing24))
                Text(
                    text = usersFriendCode,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing24))
                OutlinedTextField(
                    value = enteredCode.value,
                    onValueChange = {
                        setEnteredCode(it)
                    },
                    label = { Text(stringResource(R.string.friends_screen_enter_code_label)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        disabledTextColor = Color.Black,
                        errorTextColor = Color.Black,

                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        disabledLabelColor = Color.Black,
                        errorLabelColor = Color.Red,

                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        disabledBorderColor = Color.Black,
                        errorBorderColor = Color.Red,

                        cursorColor = Color.Black,
                        errorCursorColor = Color.Red,

                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,

                        focusedPlaceholderColor = Color.Black,
                        unfocusedPlaceholderColor = Color.Black,
                        disabledPlaceholderColor = Color.Black,
                        errorPlaceholderColor = Color.Black
                    )
                )
                Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing24))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(
                            text = stringResource(R.string.new_reading_cancel),
                            color = Color.Black
                        )
                    }
                    Spacer(Modifier.size(MaterialTheme.dimens.commonSpacing8))
                    Button(
                        onClick = {
                            onAddFriendClick()
                        },
                        modifier = Modifier.background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF4A7BF6),
                                    Color(0xFF8A5CF5)
                                )
                            ),
                            shape = RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius12)
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.friends_screen_add_friend_label),
                            color = Color.White
                        )
                    }
                }
            }
        }

    }
}

data class FriendsScreenParams(
    val friendsList: State<List<Friend>>,
    val showAddNewFriendDialog: State<Boolean>,
    val setShowAddNewFriendDialog: (Boolean) -> Unit,
    val usersFriendCode: String,
    val enteredCode: State<String>,
    val setEnteredCode: (String) -> Unit,
    val onAddFriendClick: () -> Unit
)