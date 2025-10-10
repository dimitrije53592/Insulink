package com.dj.insulink.core.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dj.insulink.auth.ui.screen.ForgotPasswordScreen
import com.dj.insulink.auth.ui.screen.ForgotPasswordScreenParams
import com.dj.insulink.home.ui.screen.HomeScreen
import com.dj.insulink.auth.ui.screen.LoginScreen
import com.dj.insulink.auth.ui.screen.LoginScreenParams
import com.dj.insulink.auth.ui.viewmodel.LoginViewModel
import com.dj.insulink.auth.ui.screen.RegistrationScreen
import com.dj.insulink.auth.ui.screen.RegistrationScreenParams
import com.dj.insulink.auth.ui.viewmodel.RegistrationViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Registration.route) {
            val viewModel: RegistrationViewModel = hiltViewModel()

            val firstName = viewModel.firstName.collectAsState()
            val lastName = viewModel.lastName.collectAsState()
            val emailAddress = viewModel.emailAddress.collectAsState()
            val password = viewModel.password.collectAsState()
            val confirmPassword = viewModel.confirmPassword.collectAsState()
            val termsOfServiceAccepted = viewModel.termsOfServiceAccepted.collectAsState()
            val showErrorMessage = viewModel.showErrorMessage.collectAsState()
            val errorMessage = viewModel.errorMessage.collectAsState()
            val isLoading = viewModel.isLoading.collectAsState()
            val registrationSuccess = viewModel.registrationSuccess.collectAsState()

            LaunchedEffect(showErrorMessage.value) {
                if (showErrorMessage.value) {
                    Toast.makeText(context, errorMessage.value, Toast.LENGTH_LONG).show()
                    viewModel.setShowErrorMessage(false)
                }
            }

            LaunchedEffect(registrationSuccess.value) {
                if (registrationSuccess.value) {
                    navController.navigate(Screen.Home.route)
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
                    navigateToLogIn = {
                        navController.navigate(Screen.Login.route)
                    }
                )
            )
        }
        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()

            val email = viewModel.email.collectAsState()
            val password = viewModel.password.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.checkIfUserIsLoggedIn()
            }

            LoginScreen(
                params = LoginScreenParams(
                    emailState = email,
                    passwordState = password,
                    onEmailChange = viewModel::onEmailChange,
                    onPasswordChange = viewModel::onPasswordChange,
                    onLogin = viewModel::login,
                    onSignInWithGoogle = viewModel::signInWithGoogle,
                    onForgotPasswordClicked = { navController.navigate(Screen.ForgotPassword.route) },
                    navigateToRegistration = {
                        navController.navigate(Screen.Registration.route)
                    }
                )
            )
        }
        composable(Screen.ForgotPassword.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            ForgotPasswordScreen(
                params = ForgotPasswordScreenParams(
                    emailState = viewModel.email,
                    onEmailChange = viewModel::onEmailChange,
                    onSendPasswordReset = viewModel::sendPasswordReset,
                    resetState = viewModel.passwordResetState
                )
            )
        }

        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
}
