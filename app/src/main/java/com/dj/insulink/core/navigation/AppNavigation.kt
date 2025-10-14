package com.dj.insulink.core.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dj.insulink.R
import com.dj.insulink.auth.ui.screen.ForgotPasswordScreen
import com.dj.insulink.auth.ui.screen.ForgotPasswordScreenParams
import com.dj.insulink.auth.ui.screen.LoginScreen
import com.dj.insulink.auth.ui.screen.LoginScreenParams
import com.dj.insulink.auth.ui.viewmodel.LoginViewModel
import com.dj.insulink.auth.ui.screen.RegistrationScreen
import com.dj.insulink.auth.ui.screen.RegistrationScreenParams
import com.dj.insulink.auth.ui.viewmodel.RegistrationViewModel
import com.dj.insulink.core.ui.screen.SideDrawer
import com.dj.insulink.core.ui.screen.SideDrawerParams
import com.dj.insulink.core.ui.viewmodel.SharedViewModel
import com.dj.insulink.core.utils.navigateTo
import com.dj.insulink.home.ui.screen.FitnessScreen
import com.dj.insulink.home.ui.screen.GlucoseScreen
import com.dj.insulink.home.ui.screen.MealsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val sharedViewModel: SharedViewModel = hiltViewModel()

    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestinationRoute = currentBackStackEntry?.destination?.route
    val currentDestination = Screen.findDestinationByRoute(currentDestinationRoute)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        sharedViewModel.getCurrentUser()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentDestinationRoute in Screen.bottomBarDestinations.map { it.route },
        drawerContent = {
            val currentUser = sharedViewModel.currentUser.collectAsState()

            SideDrawer(
                params = SideDrawerParams(
                    currentUser = currentUser,
                    onSignOutClick = {
                        sharedViewModel.signOut(drawerState)
                        navController.navigateTo(Screen.Login.route)
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                )
            )
        }
    ) {
        Scaffold(
            topBar = {
                if (currentDestinationRoute in Screen.bottomBarDestinations.map { it.route }) {
                    CenterAlignedTopAppBar(
                        title = {
                            currentDestination?.title?.let {
                                Text(
                                    text = it,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        drawerState.open()
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_menu),
                                    tint = Color(0xFFB2B2B2),
                                    contentDescription = ""
                                )
                            }
                        }
                    )
                }
            },
            bottomBar = {
                if (currentDestinationRoute in Screen.bottomBarDestinations.map { it.route }) {
                    NavigationBar {
                        Screen.bottomBarDestinations.forEach { destination ->
                            destination.icon?.let {
                                NavigationBarItem(
                                    selected = currentDestinationRoute == destination.route,
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
                startDestination = if (sharedViewModel.isUserLoggedIn()) Screen.Glucose.route else Screen.Login.route,
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
                            navigateToLogin = {
                                navController.navigateTo(Screen.Login.route)
                            }
                        )
                    )
                }
                composable(Screen.Login.route) {
                    val viewModel: LoginViewModel = hiltViewModel()

                    val email = viewModel.email.collectAsState()
                    val password = viewModel.password.collectAsState()

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
