package com.dj.insulink.auth.ui.wrapper

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dj.insulink.auth.ui.RegistrationScreen
import com.dj.insulink.auth.ui.RegistrationScreenParams
import com.dj.insulink.auth.ui.viewmodel.RegistrationViewModel

@Composable
fun RegistrationWrapper(
    fetchUser: () -> Unit,
    navigateToMainScreen: () -> Unit,
    navigateToLogin: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: RegistrationViewModel = hiltViewModel()

    val firstName = viewModel.firstName.collectAsStateWithLifecycle()
    val lastName = viewModel.lastName.collectAsStateWithLifecycle()
    val emailAddress = viewModel.emailAddress.collectAsStateWithLifecycle()
    val password = viewModel.password.collectAsStateWithLifecycle()
    val confirmPassword = viewModel.confirmPassword.collectAsStateWithLifecycle()
    val termsOfServiceAccepted = viewModel.termsOfServiceAccepted.collectAsStateWithLifecycle()
    val showErrorMessage = viewModel.showErrorMessage.collectAsStateWithLifecycle()
    val errorMessage = viewModel.errorMessage.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    val registrationSuccess = viewModel.registrationSuccess.collectAsStateWithLifecycle()

    LaunchedEffect(showErrorMessage.value) {
        if (showErrorMessage.value) {
            Toast.makeText(context, errorMessage.value, Toast.LENGTH_LONG).show()
            viewModel.setShowErrorMessage(false)
        }
    }

    LaunchedEffect(registrationSuccess.value) {
        if (registrationSuccess.value) {
            fetchUser()
            navigateToMainScreen()
        }
    }

    RegistrationScreen(
        params = RegistrationScreenParams(
            firstName = firstName,
            setFirstName = viewModel::setFirstName,
            lastName = lastName,
            setLastName = viewModel::setLastName,
            emailAddress = emailAddress,
            setEmailAddress = viewModel::setEmailAddress,
            password = password,
            setPassword = viewModel::setPassword,
            confirmPassword = confirmPassword,
            setConfirmPassword = viewModel::setConfirmPassword,
            termsOfServiceAccepted = termsOfServiceAccepted,
            setTermsOfServiceAccepted = viewModel::setTermsOfServiceAccepted,
            isLoading = isLoading,
            onSubmit = viewModel::onCreateAccountSubmit,
            navigateToLogin = navigateToLogin
        )
    )
}