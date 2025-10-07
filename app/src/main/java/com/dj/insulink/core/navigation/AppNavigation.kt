package com.dj.insulink.core.navigation

import androidx.compose.runtime.Composable
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

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Registration.route) {
            RegistrationScreen()
        }
        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                params = LoginScreenParams(
                    emailState = viewModel.email,
                    passwordState = viewModel.password,
                    onEmailChange = viewModel::onEmailChange,
                    onPasswordChange = viewModel::onPasswordChange,
                    onLogin = viewModel::login,
                    onSignInWithGoogle = viewModel::signInWithGoogle,
                    onForgotPasswordClicked = { navController.navigate(Screen.ForgotPassword.route) },
                    onCheckIfUserIsLoggedIn = viewModel::checkIfUserIsLoggedIn
                )
            )
        }
        composable(Screen.ForgotPassword.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            ForgotPasswordScreen(
                params = ForgotPasswordScreenParams(
                    emailState = viewModel.email,
                    onEmailChange = viewModel::onEmailChange,
                    onSendPasswordReset = viewModel::login,
                    resetState = viewModel.passwordResetState
                )
            )
        }
    }
}