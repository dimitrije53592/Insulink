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

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _showErrorMessage = MutableStateFlow(false)
    val showErrorMessage = _showErrorMessage.asStateFlow()

    private val _passwordResetState = MutableStateFlow(PasswordResetState())
    val passwordResetState: StateFlow<PasswordResetState> = _passwordResetState.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setShowErrorMessage(isVisible: Boolean) {
        _showErrorMessage.value = isVisible
    }

    private fun isPasswordLongEnough(): Boolean {
        return _password.value.length >= 8
    }

    private fun isEmailValid(): Boolean {
        if (_email.value.isEmpty()) {
            return false
        }

        return Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
    }
    private fun isLoginFormValid(): Boolean {
        if (!isEmailValid()) {
            _errorMessage.value = "Email address you entered is not a valid email address."
            return false
        } else if (!isPasswordLongEnough()) {
            _errorMessage.value = "Password you entered has to have at least 8 characters."
            return false
        } else {
            return true
        }
    }
    fun loginUser() {
        if(isLoginFormValid()){
            login()
        } else {
            setShowErrorMessage(true)
        }
    }

    private fun login() {
        viewModelScope.launch {
            _isLoading.value = true
            _showErrorMessage.value = false

            try {
                Log.d(TAG, "Attempting login with email: ${_email.value}")
                Log.d(TAG, "Attempting login with password: ${_password.value}")

                val userLogin = UserLogin(
                    email = _email.value,
                    password = _password.value
                )
                authRepository.loginUser(userLogin)

                Log.d(TAG, "Login successful, setting success flag.")
                _loginSuccess.value = true

            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login failed with exception.", e)
                _errorMessage.value = "Login failed: ${e.message}"
                _showErrorMessage.value = true

                _loginSuccess.value = false

            } finally {
                _isLoading.value = false
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

            } catch (e: Exception) {
                Log.e(TAG, "Google Sign-In failed with exception.", e)
                _errorMessage.value = "Google Sign-In failed: ${e.message}"
                _showErrorMessage.value = true
                _loginSuccess.value = false
            } finally {
                _isLoading.value = false
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
