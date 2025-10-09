package com.dj.insulink.auth.ui.screen

import androidx.compose.foundation.Image
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.dj.insulink.core.ui.theme.dimens

@Composable
fun RegistrationScreen(
    params: RegistrationScreenParams
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.size(MaterialTheme.dimens.commonSpacing20))
        Image(
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = "",
            modifier = Modifier.size(MaterialTheme.dimens.registrationMainIconSize)
        )
        Spacer(modifier = Modifier.size(MaterialTheme.dimens.commonSpacing20))
        Text(
            text = stringResource(R.string.registration_screen_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(MaterialTheme.dimens.commonSpacing24))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.commonPadding24)
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
                singleLine = true
            )
            Spacer(modifier = Modifier.size(MaterialTheme.dimens.commonSpacing12))
            OutlinedTextField(
                value = params.lastName.value,
                onValueChange = {
                    params.setLastName(it)
                },
                label = {
                    Text(
                        text = stringResource(R.string.registration_screen_last_name_label),
                        color = Color.Black
                    )
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
        }
        Spacer(modifier = Modifier.size(MaterialTheme.dimens.commonSpacing12))
        OutlinedTextField(
            value = params.emailAddress.value,
            onValueChange = {
                params.setEmailAddress(it)
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_email),
                    contentDescription = null,
                    modifier = Modifier.size(MaterialTheme.dimens.textFieldIconSize)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.registration_screen_email_address_label),
                    color = Color.Black
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.commonPadding24),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        Spacer(modifier = Modifier.size(MaterialTheme.dimens.commonSpacing12))
        OutlinedTextField(
            value = params.password.value,
            onValueChange = {
                params.setPassword(it)
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_password),
                    contentDescription = null,
                    modifier = Modifier.size(MaterialTheme.dimens.textFieldIconSize)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.registration_screen_password_label),
                    color = Color.Black
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.commonPadding24),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Spacer(modifier = Modifier.size(MaterialTheme.dimens.commonSpacing12))
        OutlinedTextField(
            value = params.confirmPassword.value,
            onValueChange = {
                params.setConfirmPassword(it)
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_password),
                    contentDescription = null,
                    modifier = Modifier.size(MaterialTheme.dimens.textFieldIconSize)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.registration_screen_confirm_password_label),
                    color = Color.Black
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.commonPadding24),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Spacer(modifier = Modifier.size(MaterialTheme.dimens.commonSpacing12))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.commonPadding12)
        ) {
            Checkbox(
                checked = params.termsOfServiceAccepted.value,
                onCheckedChange = {
                    params.setTermsOfServiceAccepted(it)
                }
            )
            Text(
                text = stringResource(R.string.registration_screen_terms_of_service_label),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Spacer(modifier = Modifier.size(MaterialTheme.dimens.commonSpacing24))
        Button(
            onClick = {
                params.onSubmit()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.commonButtonHeight50)
                .padding(horizontal = MaterialTheme.dimens.commonPadding24),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.Black
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = MaterialTheme.dimens.commonElevation0)
        ) {
            if (params.isLoading.value) {
                CircularProgressIndicator(
                    modifier = Modifier.size(MaterialTheme.dimens.commonProgressIndicatorSize25),
                    color = Color.White
                )
            } else {
                Text(
                    text = stringResource(R.string.registration_screen_submit_button_label),
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.size(MaterialTheme.dimens.commonSpacing24))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.registration_screen_existing_account_label),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.size(MaterialTheme.dimens.commonSpacing4))
            Text(
                text = stringResource(R.string.registration_screen_sign_in_redirect_label),
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Blue),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    params.navigateToLogin()
                }
            )
        }
        Spacer(modifier = Modifier.size(MaterialTheme.dimens.commonSpacing24))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.commonPadding24)
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = DividerDefaults.Thickness, color = Color.Black
            )
            Text(
                text = stringResource(R.string.registration_screen_or_continue_with_label),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.commonPadding8)
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = DividerDefaults.Thickness, color = Color.Black
            )
        }
        Spacer(modifier = Modifier.size(MaterialTheme.dimens.commonSpacing24))
        Button(
            onClick = {

            },
            modifier = Modifier
                .height(MaterialTheme.dimens.commonButtonHeight50)
                .border(
                    MaterialTheme.dimens.commonButtonBorder1,
                    Color.LightGray,
                    RoundedCornerShape(MaterialTheme.dimens.commonButtonRadius8)
                )
                .padding(horizontal = MaterialTheme.dimens.commonPadding48),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = MaterialTheme.dimens.commonElevation0)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = "",
                    modifier = Modifier.size(MaterialTheme.dimens.textFieldIconSize)
                )
                Spacer(modifier = Modifier.width(MaterialTheme.dimens.commonSpacing8))
                Text(
                    text = stringResource(R.string.login_screen_google_label),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
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