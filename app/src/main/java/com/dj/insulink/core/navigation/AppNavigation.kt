package com.dj.insulink.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dj.insulink.login.ui.screens.ForgotPasswordScreen
import com.dj.insulink.login.ui.screens.ForgotPasswordScreenParams
import com.dj.insulink.login.ui.screens.LoginScreen
import com.dj.insulink.login.ui.screens.LoginScreenParams
import com.dj.insulink.login.ui.viewmodel.LoginViewModel
import com.dj.insulink.registration.ui.screens.RegistrationScreen
import com.dj.insulink.registration.ui.screens.RegistrationScreenParams
import com.dj.insulink.registration.ui.viewmodel.RegistrationViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

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
                    onSubmit = {},
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
    }
}