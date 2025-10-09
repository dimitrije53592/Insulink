package com.dj.insulink.core.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dj.insulink.auth.ui.screen.ForgotPasswordScreen
import com.dj.insulink.auth.ui.screen.ForgotPasswordScreenParams
import com.dj.insulink.auth.ui.screen.LoginScreen
import com.dj.insulink.auth.ui.screen.LoginScreenParams
import com.dj.insulink.auth.ui.viewmodel.LoginViewModel
import com.dj.insulink.auth.ui.screen.RegistrationScreen
import com.dj.insulink.auth.ui.screen.RegistrationScreenParams
import com.dj.insulink.auth.ui.viewmodel.RegistrationViewModel
import com.dj.insulink.core.utils.navigateTo
import com.dj.insulink.home.ui.screen.FitnessScreen
import com.dj.insulink.home.ui.screen.GlucoseScreen
import com.dj.insulink.home.ui.screen.MealsScreen

@Composable
fun AppNavigation() {

    val context = LocalContext.current
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = currentBackStackEntry?.destination?.route

            if (bottomBarDestinations.map { it.route }.contains(currentDestination)) {
                NavigationBar {
                    bottomBarDestinations.forEach { destination ->
                        destination.icon?.let {
                            NavigationBarItem(
                                selected = currentDestination == destination.route,
                                label = {
                                    Text(text = destination.title)
                                },
                                icon = {
                                    Icon(imageVector = it, contentDescription = "")
                                },
                                onClick = {
                                    navController.navigateTo(destination.route)
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
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
                        navController.navigateTo(Screen.Glucose.route)
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
                            navController.navigateTo(Screen.Login.route)
                        }
                    )
                )
            }
            composable(Screen.Login.route) {
                val viewModel: LoginViewModel = hiltViewModel()

                val email = viewModel.email.collectAsState()
                val password = viewModel.password.collectAsState()

                LaunchedEffect(Unit) {
                    if (viewModel.isUserLoggedIn()) {
                        navController.navigateTo(Screen.Glucose.route)
                    }
                }

                LoginScreen(
                    params = LoginScreenParams(
                        emailState = email,
                        passwordState = password,
                        onEmailChange = viewModel::onEmailChange,
                        onPasswordChange = viewModel::onPasswordChange,
                        onLogin = viewModel::login,
                        onSignInWithGoogle = viewModel::signInWithGoogle,
                        onForgotPasswordClicked = { navController.navigateTo(Screen.ForgotPassword.route) },
                        navigateToRegistration = {
                            navController.navigateTo(Screen.Registration.route)
                        },
                        navigateToHome = {
                            navController.navigateTo(Screen.Glucose.route)
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

            composable(Screen.Glucose.route) {
                GlucoseScreen()
            }

            composable(Screen.Meals.route) {
                MealsScreen()
            }

            composable(Screen.Fitness.route) {
                FitnessScreen()
            }
        }

    }
}
