package com.dj.insulink.auth.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dj.insulink.R
import com.dj.insulink.auth.ui.viewmodel.PasswordResetState
import com.dj.insulink.core.ui.theme.InsulinkTheme
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ForgotPasswordScreen(
    params: ForgotPasswordScreenParams
) {
    val emailState = params.emailState.collectAsState().value
    val resetState = params.resetState.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.33f)
                    .background(
                        brush = Brush
                            .verticalGradient(
                                colors = listOf(
                                    InsulinkTheme.colors.insulinkBlue,
                                    InsulinkTheme.colors.insulinkPurple
                                )
                            )
                    )
                    .padding(InsulinkTheme.dimens.commonPadding24)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_insulink_logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(InsulinkTheme.dimens.commonButtonRadius12))
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.login_insulink),
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = stringResource(R.string.login_description),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.67f)
                    .background(MaterialTheme.colorScheme.background)
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .align(Alignment.TopCenter)
                .offset(y = 230.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(InsulinkTheme.dimens.commonPadding24)
                ),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(InsulinkTheme.dimens.commonPadding24),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(InsulinkTheme.dimens.commonPadding20),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.forgot_password_title),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = InsulinkTheme.dimens.commonPadding24, top = InsulinkTheme.dimens.commonPadding24),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = stringResource(R.string.forgot_password_email_description),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = InsulinkTheme.dimens.commonPadding24),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = emailState,
                    onValueChange = params.onEmailChange,
                    label = {
                        Text(
                            stringResource(R.string.login_enter_email_text)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = InsulinkTheme.dimens.commonPadding24),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_email),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            contentDescription = null,
                            modifier = Modifier.size(InsulinkTheme.dimens.textFieldIconSize)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        errorTextColor = MaterialTheme.colorScheme.error,

                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        errorLabelColor = MaterialTheme.colorScheme.error,

                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                        errorBorderColor = MaterialTheme.colorScheme.error,

                        cursorColor = MaterialTheme.colorScheme.primary,
                        errorCursorColor = MaterialTheme.colorScheme.error
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    InsulinkTheme.colors.insulinkBlue,
                                    InsulinkTheme.colors.insulinkPurple
                                )
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable(enabled = !resetState.isLoading) {
                            params.onSendPasswordReset()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (resetState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(InsulinkTheme.dimens.textFieldIconSize)
                        )
                    } else {
                        Text(
                            stringResource(R.string.forgot_password_send_reset_link),
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(InsulinkTheme.dimens.commonPadding24))
            }
        }
    }
}

data class ForgotPasswordScreenParams(
    val emailState: StateFlow<String>,
    val onEmailChange: (String) -> Unit,
    val onSendPasswordReset: () -> Unit,
    val resetState: StateFlow<PasswordResetState>,
    val passwordResetButtonGradientColorStart: Int = R.color.purple_500,
    val passwordResetButtonGradientColorEnd: Int = R.color.purple_200
)