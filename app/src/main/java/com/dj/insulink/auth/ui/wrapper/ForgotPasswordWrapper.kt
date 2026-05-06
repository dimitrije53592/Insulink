package com.dj.insulink.auth.ui.wrapper

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dj.insulink.auth.ui.ForgotPasswordScreen
import com.dj.insulink.auth.ui.ForgotPasswordScreenParams
import com.dj.insulink.auth.ui.viewmodel.LoginViewModel

@Composable
fun ForgotPasswordWrapper() {
    val context = LocalContext.current

    val viewModel: LoginViewModel = hiltViewModel()

    val resetState = viewModel.passwordResetState.collectAsStateWithLifecycle()

    LaunchedEffect(resetState.value.successMessage) {
        if (resetState.value.successMessage != null) {
            Toast.makeText(
                context,
                resetState.value.successMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(resetState.value.errorMessage) {
        resetState.value.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    ForgotPasswordScreen(
        params = ForgotPasswordScreenParams(
            emailState = viewModel.email,
            onEmailChange = viewModel::setEmail,
            onSendPasswordReset = viewModel::sendPasswordReset,
            resetState = viewModel.passwordResetState,
        )
    )

}
