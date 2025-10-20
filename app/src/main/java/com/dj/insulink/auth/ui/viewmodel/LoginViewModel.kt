package com.dj.insulink.auth.ui.viewmodel

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dj.insulink.auth.data.AuthRepository
import com.dj.insulink.auth.domain.models.UserLogin
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    // Login state management
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val _showErrorMessage = MutableStateFlow(false)
    val showErrorMessage: StateFlow<Boolean> = _showErrorMessage.asStateFlow()

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    private val _showErrorMessage = MutableStateFlow(false)
    val showErrorMessage = _showErrorMessage.asStateFlow()

    fun setShowErrorMessage(show: Boolean) {
        _showErrorMessage.value = show
    }

    private val _passwordResetState = MutableStateFlow(PasswordResetState())
    val passwordResetState: StateFlow<PasswordResetState> = _passwordResetState.asStateFlow()

    fun loginUser() {
        Log.d(TAG, "Attempting login with: ${_email.value} and ${_password.value}")
        if (_email.value.isBlank() || _password.value.isBlank()) {
            Log.d(TAG, "Email or password is empty")
            _errorMessage.value = "Please enter both email and password"
            _showErrorMessage.value = true
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _showErrorMessage.value = false

            try {
                val result = auth.signInWithEmailAndPassword(_email.value, _password.value).await()
                val user = result.user
                Log.d(TAG, "Login Success: User ${user?.uid}")
                _loginSuccess.value = true
            } catch (e: Exception) {
                Log.e(TAG, "Login Failed: ${e.message}")
                _errorMessage.value = e.message ?: "Login failed. Please try again."
                _showErrorMessage.value = true
            }
        }
    }


    fun sendPasswordReset() {
        Log.d(TAG, "sendPasswordReset: uslo u fju za kliknuto dugme")
        if (_email.value.isBlank()) {
            Toast.makeText(context, "Please enter your email address.", Toast.LENGTH_SHORT).show()
            _passwordResetState.update { it.copy(errorMessage = "Please enter your email address.") }
            return
        }

        viewModelScope.launch {
            _passwordResetState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    successMessage = null
                )
            }
            try {
                authRepository.sendPasswordResetEmail(_email.value)
                _passwordResetState.update {
                    it.copy(isLoading = false, successMessage = "Password reset link sent to ${_email.value}.")
                }
            } catch (e: Exception) {
                val msg = e.message ?: "An unknown error occurred."
                _passwordResetState.update {
                    it.copy(isLoading = false, errorMessage = msg)
                }
                Log.e(TAG, "sendPasswordReset failed", e)
            }
        }

    }

    fun signInWithGoogle(credential: com.google.firebase.auth.AuthCredential) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                authRepository.signInWithGoogle(credential)

                _loginSuccess.value = true

    fun signInWithGoogle(credential: AuthCredential) {
        Log.d(TAG, "Attempting login with: Google")
        viewModelScope.launch {
            try {
                val result = auth.signInWithCredential(credential).await()
                val user = result.user
                Log.d(TAG, "Google Sign-In Success: User ${user?.uid}")
                _loginSuccess.value = true
            } catch (e: Exception) {
                Log.e(TAG, "Google Sign-In Failed: ${e.message}")
                _errorMessage.value = e.message ?: "Google Sign-In failed. Please try again."
                _showErrorMessage.value = true
            }
        }
    }



    private val TAG = LoginViewModel::class.java.simpleName
}

data class PasswordResetState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
