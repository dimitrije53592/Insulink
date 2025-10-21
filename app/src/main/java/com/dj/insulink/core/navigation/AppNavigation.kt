package com.dj.insulink.core.navigation

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.remember
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
import com.dj.insulink.auth.ui.screen.RegistrationScreen
import com.dj.insulink.auth.ui.screen.RegistrationScreenParams
import com.dj.insulink.auth.ui.viewmodel.LoginViewModel
import com.dj.insulink.auth.ui.viewmodel.RegistrationViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.dj.insulink.core.ui.screen.SideDrawer
import com.dj.insulink.core.ui.screen.SideDrawerParams
import com.dj.insulink.core.ui.viewmodel.SharedViewModel
import com.dj.insulink.core.utils.navigateTo
import com.dj.insulink.feature.domain.models.Reminder
import com.dj.insulink.feature.domain.models.ReminderType
import com.dj.insulink.feature.ui.screen.FitnessScreen
import com.dj.insulink.feature.ui.screen.FitnessScreenParams
import com.dj.insulink.feature.ui.screen.FriendsScreen
import com.dj.insulink.feature.ui.screen.FriendsScreenParams
import com.dj.insulink.feature.ui.screen.GlucoseScreen
import com.dj.insulink.feature.ui.screen.GlucoseScreenParams
import com.dj.insulink.feature.ui.screen.MealsScreen
import com.dj.insulink.feature.ui.screen.RemindersScreen
import com.dj.insulink.feature.ui.screen.RemindersScreenParams
import com.dj.insulink.feature.ui.screen.ReportsScreen
import com.dj.insulink.feature.ui.viewmodel.FriendViewModel
import com.dj.insulink.feature.ui.screen.getDummyMealsScreenParams
import com.dj.insulink.feature.ui.viewmodel.FitnessViewModel
import com.dj.insulink.feature.ui.viewmodel.GlucoseViewModel
import com.dj.insulink.feature.ui.viewmodel.ReminderViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val sharedViewModel: SharedViewModel = hiltViewModel()
    val currentUser = sharedViewModel.currentUser.collectAsState()

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
        gesturesEnabled = currentDestinationRoute in Screen.topBarAndSideDrawerDestinations.map { it.route },
        drawerContent = {
            SideDrawer(
                params = SideDrawerParams(
                    currentUser = currentUser.value,
                    navigateToReminders = {
                        navController.navigateTo(Screen.Reminders.route)
                        coroutineScope.launch { drawerState.close() }
                    },
                    navigateToFriends = {
                        navController.navigateTo(Screen.Friends.route)
                        coroutineScope.launch { drawerState.close() }
                    },
                    navigateToReports = {
                        navController.navigateTo(Screen.Report.route)
                        coroutineScope.launch { drawerState.close() }
                    },
                    onSignOutClick = {
                        sharedViewModel.signOut(context)
                        navController.navigateTo(Screen.Login.route)
                        coroutineScope.launch { drawerState.close() }
                    }
                )
            )
        }
    ) {
        Scaffold(
            topBar = {
                if (currentDestinationRoute in Screen.topBarAndSideDrawerDestinations.map { it.route }) {
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
                if (currentDestinationRoute in Screen.topBarAndSideDrawerDestinations.map { it.route }) {
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
                            sharedViewModel.getCurrentUser()
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
                    val errorMessage = viewModel.errorMessage.collectAsState()
                    val showErrorMessage = viewModel.showErrorMessage.collectAsState()
                    val loginSuccess = viewModel.loginSuccess.collectAsState()
                    val gso = remember {
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(context.getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build()
                    }
                    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

                    val googleSignInLauncher =
                        rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartActivityForResult()
                    ) { result ->
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        try {
                            val account = task.getResult(ApiException::class.java)!!
                            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                            viewModel.signInWithGoogle(credential)
                        } catch (e: ApiException) {
                            Log.w("AppNavigation", "Google sign in failed", e)
                            Toast.makeText(context, "Google Sign-In failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    LaunchedEffect(showErrorMessage.value) {
                        if (showErrorMessage.value) {
                            Toast.makeText(context, errorMessage.value, Toast.LENGTH_LONG).show()
                            viewModel.setShowErrorMessage(false)
                        }
                    }
                    LaunchedEffect(loginSuccess.value) {
                        if (loginSuccess.value) {
                            sharedViewModel.getCurrentUser()
                            navController.navigateTo(Screen.Glucose.route)
                        }
                    }
                    LoginScreen(
                        params = LoginScreenParams(
                            emailState = email,
                            passwordState = password,
                            setEmail = viewModel::setEmail,
                            setPassword = viewModel::setPassword,
                            onLogin = viewModel::loginUser,
                            onSignInWithGoogle = {
                                Log.d("TAG", "AppNavigation: google sign in ")
                                googleSignInLauncher.launch(googleSignInClient.signInIntent) },
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
                    val resetState = viewModel.passwordResetState.collectAsState()
                    LaunchedEffect(resetState.value.successMessage) {
                        if (resetState.value.successMessage != null) {
                            Toast.makeText(context, resetState.value.successMessage, Toast.LENGTH_LONG).show()
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
                composable(Screen.Glucose.route) {
                    val viewModel: GlucoseViewModel = hiltViewModel()

                    val allGlucoseReadings = viewModel.allGlucoseReadings.collectAsState()
                    val latestGlucoseReading = viewModel.latestGlucoseReading.collectAsState()
                    val newGlucoseReadingTimestamp =
                        viewModel.newGlucoseReadingTimestamp.collectAsState()
                    val newGlucoseReadingValue = viewModel.newGlucoseReadingValue.collectAsState()
                    val newGlucoseReadingComment =
                        viewModel.newGlucoseReadingComment.collectAsState()
                    val showAddGlucoseReadingDialog =
                        viewModel.showAddGlucoseReadingDialog.collectAsState()
                    val selectedTimespan = viewModel.selectedTimespan.collectAsState()

                    LaunchedEffect(currentUser.value) {
                        viewModel.fetchAllGlucoseReadingsForUserAndUpdateDatabase(currentUser.value?.uid)
                    }
                    GlucoseScreen(
                        params = GlucoseScreenParams(
                            allGlucoseReadings = allGlucoseReadings,
                            latestGlucoseReading = latestGlucoseReading,
                            selectedTimespan = selectedTimespan,
                            setSelectedTimespan = viewModel::setSelectedTimespan,
                            newGlucoseReadingTimestamp = newGlucoseReadingTimestamp,
                            setNewGlucoseReadingTimestamp = viewModel::setNewGlucoseReadingTimestamp,
                            newGlucoseReadingValue = newGlucoseReadingValue,
                            setNewGlucoseReadingValue = viewModel::setNewGlucoseReadingValue,
                            newGlucoseReadingComment = newGlucoseReadingComment,
                            setNewGlucoseReadingComment = viewModel::setNewGlucoseReadingComment,
                            showAddGlucoseReadingDialog = showAddGlucoseReadingDialog,
                            setShowAddGlucoseReadingDialog = viewModel::setShowAddGlucoseReadingDialog,
                            submitNewGlucoseReading = {
                                viewModel.submitNewGlucoseReading(currentUser.value?.uid)
                            },
                            deleteGlucoseReading = {
                                viewModel.deleteGlucoseReading(currentUser.value?.uid, it)
                            }
                        )
                    )
                }
                composable(Screen.Meals.route) {
                    MealsScreen()
                }
                composable(Screen.Fitness.route) {
                    val viewModel: FitnessViewModel = hiltViewModel()

                    val showAddSportsActivityDialog = viewModel.showAddSportsActivityDialog.collectAsState()
                    val activityName = viewModel.activityName.collectAsState()
                    val durationHours = viewModel.durationHours.collectAsState()
                    val durationMinutes = viewModel.durationMinutes.collectAsState()
                    val glucoseBefore = viewModel.glucoseBefore.collectAsState()
                    val glucoseAfter = viewModel.glucoseAfter.collectAsState()

                    FitnessScreen(
                        params = FitnessScreenParams(
                            sports = emptyList(),
                            showAddSportsActivityDialog = showAddSportsActivityDialog,
                            setShowSportsActivityDialog = viewModel::setShowSportsActivityDialog,
                            sportName = activityName,
                            setSportName = viewModel::setActivityName,
                            durationHours = durationHours,
                            setDurationHours = viewModel::setDurationHours,
                            durationMinutes = durationMinutes,
                            setDurationMinutes = viewModel::setDurationMinutes,
                            glucoseBefore = glucoseBefore,
                            setGlucoseBefore = viewModel::setGlucoseBefore,
                            glucoseAfter = glucoseAfter,
                            setGlucoseAfter = viewModel::setGlucoseAfter,
                            onAddExerciseClick = viewModel::onAddExerciseClick
                        )
                    )
                }
                composable(Screen.Reminders.route) {
                    val currentUser = sharedViewModel.currentUser.collectAsState()
                    val viewModel: ReminderViewModel = hiltViewModel()

                    val allRemindersForUser = viewModel.allRemindersForUser.collectAsState()
                    val showAddReminderDialog = viewModel.showAddReminderDialog.collectAsState()
                    val reminderTitle = viewModel.reminderTitle.collectAsState()
                    val reminderType = viewModel.reminderType.collectAsState()
                    val reminderTime = viewModel.reminderTime.collectAsState()

                    LaunchedEffect(currentUser.value) {
                        currentUser.value?.uid?.let {
                            viewModel.fetchReminderDataAndUpdateDatabase(it)
                        }
                    }

                    currentUser.value?.let {
                        RemindersScreen(
                            params = RemindersScreenParams(
                                reminders = allRemindersForUser.value,
                                showAddReminderDialog = showAddReminderDialog,
                                setShowAddReminderDialog = viewModel::setShowAddReminderDialog,
                                reminderTitle = reminderTitle,
                                setReminderTitle = viewModel::setReminderTitle,
                                reminderType = reminderType,
                                setReminderType = viewModel::setReminderType,
                                reminderTime = reminderTime,
                                setReminderTime = viewModel::setReminderTime,
                                onSwipeFromStartToEnd = {
                                    viewModel.deleteReminder(currentUser.value?.uid, it)
                                },
                                onAddReminderClick = {
                                    viewModel.addReminder(currentUser.value!!.uid)
                                }
                            )
                        )
                    }
                }
                composable(Screen.Friends.route) {
                    val viewModel: FriendViewModel = hiltViewModel()

                    val allFriendsForUser = viewModel.allFriendsForUser.collectAsState()
                    val showAddNewFriendDialog = viewModel.showAddNewFriendDialog.collectAsState()
                    val enteredCode = viewModel.enteredCode.collectAsState()

                    LaunchedEffect(currentUser.value) {
                        currentUser.value?.uid?.let {
                            viewModel.fetchFriendDataAndUpdateDatabase(it)
                        }
                    }

                    currentUser.value?.let {
                        FriendsScreen(
                            params = FriendsScreenParams(
                                friendsList = allFriendsForUser,
                                showAddNewFriendDialog = showAddNewFriendDialog,
                                setShowAddNewFriendDialog = viewModel::setShowAddNewFriendDialog,
                                usersFriendCode = currentUser.value!!.friendCode,
                                enteredCode = enteredCode,
                                setEnteredCode = viewModel::setEnteredCode,
                                onAddFriendClick = {
                                    viewModel.onAddFriendClick(userId = currentUser.value!!.uid)
                                }
                            )
                        )
                    }
                }
                composable(Screen.Report.route) {
                    ReportsScreen()
                }
            }
        }
    }
}
