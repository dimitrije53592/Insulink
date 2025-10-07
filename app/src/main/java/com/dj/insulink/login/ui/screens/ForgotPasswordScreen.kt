package com.dj.insulink.login.ui.screens

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dj.insulink.R
import com.dj.insulink.login.ui.viewmodel.PasswordResetState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ForgotPasswordScreen(
    params: ForgotPasswordScreenParams
) {
    val passwordResetButtonGradientColorStart = colorResource(params.passwordResetButtonGradientColorStart)
    val passwordResetButtonGradientColorEnd = colorResource(params.passwordResetButtonGradientColorEnd)
    val emailState = params.emailState.collectAsState().value
    val resetState = params.resetState.collectAsState().value


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.33f)
                    .background(Color.Blue).padding(24.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_insulink_logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(100.dp)
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
                    .background(Color.White)
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .align(Alignment.TopCenter)
                .offset(y = 250.dp)
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(24.dp)
                ),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(24.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.forgot_password_title),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 24.dp, top = 24.dp),
                    color = Color.Black
                )

                Text(
                    text = stringResource(R.string.forgot_password_email_description),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp),
                    color = Color.Gray
                )

                Text(
                    text = stringResource(R.string.login_email_text),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth().align(Alignment.Start).padding(start = 0.dp, bottom = 4.dp)
                )
                OutlinedTextField(
                    value = emailState,
                    onValueChange = params.onEmailChange,
                    placeholder = { Text(stringResource(R.string.login_enter_email_text), color = Color.Black) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_email),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                listOf(passwordResetButtonGradientColorStart, passwordResetButtonGradientColorEnd)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable(enabled = !resetState.isLoading) {
                            params.onSendPasswordReset()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (resetState.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(stringResource(R.string.forgot_password_send_reset_link), color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
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
