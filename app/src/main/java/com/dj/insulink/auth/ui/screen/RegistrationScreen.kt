package com.dj.insulink.auth.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.dj.insulink.R
import com.dj.insulink.core.ui.theme.InsulinkTheme

@Composable
fun RegistrationScreen(
    params: RegistrationScreenParams
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.size(InsulinkTheme.dimens.commonSpacing20))
        Image(
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = "",
            modifier = Modifier.size(InsulinkTheme.dimens.registrationMainIconSize)
        )
        Spacer(modifier = Modifier.size(InsulinkTheme.dimens.commonSpacing20))
        Text(
            text = stringResource(R.string.registration_screen_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.size(InsulinkTheme.dimens.commonSpacing24))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = InsulinkTheme.dimens.commonPadding24)
        ) {
            OutlinedTextField(
                value = params.firstName.value,
                onValueChange = {
                    params.setFirstName(it)
                },
                label = {
                    Text(text = stringResource(R.string.registration_screen_first_name_label))
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
            Spacer(modifier = Modifier.size(InsulinkTheme.dimens.commonSpacing12))
            OutlinedTextField(
                value = params.lastName.value,
                onValueChange = {
                    params.setLastName(it)
                },
                label = {
                    Text(
                        text = stringResource(R.string.registration_screen_last_name_label)
                    )
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
        }
        Spacer(modifier = Modifier.size(InsulinkTheme.dimens.commonSpacing12))
        OutlinedTextField(
            value = params.emailAddress.value,
            onValueChange = {
                params.setEmailAddress(it)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_email),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = null,
                    modifier = Modifier.size(InsulinkTheme.dimens.textFieldIconSize)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.registration_screen_email_address_label)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = InsulinkTheme.dimens.commonPadding24),
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
        Spacer(modifier = Modifier.size(InsulinkTheme.dimens.commonSpacing12))
        OutlinedTextField(
            value = params.password.value,
            onValueChange = {
                params.setPassword(it)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_password),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = null,
                    modifier = Modifier.size(InsulinkTheme.dimens.textFieldIconSize)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.registration_screen_password_label)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = InsulinkTheme.dimens.commonPadding24),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
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
        Spacer(modifier = Modifier.size(InsulinkTheme.dimens.commonSpacing12))
        OutlinedTextField(
            value = params.confirmPassword.value,
            onValueChange = {
                params.setConfirmPassword(it)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_password),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = null,
                    modifier = Modifier.size(InsulinkTheme.dimens.textFieldIconSize)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.registration_screen_confirm_password_label)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = InsulinkTheme.dimens.commonPadding24),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
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
        Spacer(modifier = Modifier.size(InsulinkTheme.dimens.commonSpacing12))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = InsulinkTheme.dimens.commonPadding12)
        ) {
            Checkbox(
                checked = params.termsOfServiceAccepted.value,
                onCheckedChange = {
                    params.setTermsOfServiceAccepted(it)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.outline,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary
                )
            )
            Text(
                text = stringResource(R.string.registration_screen_terms_of_service_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.size(InsulinkTheme.dimens.commonSpacing24))
        Button(
            onClick = {
                params.onSubmit()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(InsulinkTheme.dimens.commonButtonHeight50)
                .padding(horizontal = InsulinkTheme.dimens.commonPadding24),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSurface,
                contentColor = MaterialTheme.colorScheme.surface
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = InsulinkTheme.dimens.commonElevation0)
        ) {
            if (params.isLoading.value) {
                CircularProgressIndicator(
                    modifier = Modifier.size(InsulinkTheme.dimens.commonProgressIndicatorSize25),
                    color = MaterialTheme.colorScheme.surface
                )
            } else {
                Text(
                    text = stringResource(R.string.registration_screen_submit_button_label),
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }
        Spacer(modifier = Modifier.size(InsulinkTheme.dimens.commonSpacing24))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.registration_screen_existing_account_label),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.size(InsulinkTheme.dimens.commonSpacing4))
            Text(
                text = stringResource(R.string.registration_screen_sign_in_redirect_label),
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    params.navigateToLogin()
                }
            )
        }
    }
}

data class RegistrationScreenParams(
    val firstName: State<String>,
    val setFirstName: (String) -> Unit,
    val lastName: State<String>,
    val setLastName: (String) -> Unit,
    val emailAddress: State<String>,
    val setEmailAddress: (String) -> Unit,
    val password: State<String>,
    val setPassword: (String) -> Unit,
    val confirmPassword: State<String>,
    val setConfirmPassword: (String) -> Unit,
    val termsOfServiceAccepted: State<Boolean>,
    val setTermsOfServiceAccepted: (Boolean) -> Unit,
    val isLoading: State<Boolean>,
    val onSubmit: () -> Unit,
    val navigateToLogin: () -> Unit
)