package com.dj.insulink.auth.ui.wrapper

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dj.insulink.R
import com.dj.insulink.auth.ui.LoginScreen
import com.dj.insulink.auth.ui.LoginScreenParams
import com.dj.insulink.auth.ui.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginWrapper(
    fetchUser: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToRegistration: () -> Unit,
    navigateToForgotPassword: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: LoginViewModel = hiltViewModel()

    val email = viewModel.email.collectAsStateWithLifecycle()
    val password = viewModel.password.collectAsStateWithLifecycle()
    val errorMessage = viewModel.errorMessage.collectAsStateWithLifecycle()
    val showErrorMessage = viewModel.showErrorMessage.collectAsStateWithLifecycle()
    val loginSuccess = viewModel.loginSuccess.collectAsStateWithLifecycle()

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
                val credential =
                    GoogleAuthProvider.getCredential(account.idToken!!, null)
                viewModel.signInWithGoogle(credential)
            } catch (e: ApiException) {
                Log.w("AppNavigation", "Google sign in failed", e)
                Toast.makeText(
                    context,
                    "Google Sign-In failed.",
                    Toast.LENGTH_SHORT
                ).show()
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
            fetchUser()
            navigateToHome()
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
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            },
            onForgotPasswordClicked = {
                navigateToForgotPassword()
            },
            navigateToRegistration = {
                navigateToRegistration()
            },
            navigateToHome = {
                navigateToHome()
            }
        )
    )
}