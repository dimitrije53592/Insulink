package com.dj.insulink.login.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
data class PasswordResetState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
class LoginViewModel @Inject constructor() : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }
    private val _passwordResetState = MutableStateFlow(PasswordResetState())
    val passwordResetState: StateFlow<PasswordResetState> = _passwordResetState
    fun login() {
        Log.d(TAG,"Attempting login with: $email and $password")
        if(email.isBlank() || password.isBlank()) {
            Log.d(TAG,"Email or password is empty")
            return
        }
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // PRIJAVA USPEŠNA
                            val user = auth.currentUser
                            println("Login Success: User ${user?.uid}")
                        } else {
                            println("Login Failed: ${task.exception?.message}")
                        }
                    }
            } catch (e: Exception) {
                println("Exception during login: ${e.message}")
            }
        }
    }
    fun sendPasswordReset() {
        if (email.isBlank()) {
            _passwordResetState.update { it.copy(errorMessage = "Please enter your email address.") }
            return
        }

        _passwordResetState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

        viewModelScope.launch {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    _passwordResetState.update {
                        if (task.isSuccessful) {
                            it.copy(
                                isLoading = false,
                                successMessage = "Password reset link sent to $email."
                            )
                        } else {
                            it.copy(
                                isLoading = false,
                                errorMessage = task.exception?.message ?: "An unknown error occurred."
                            )
                        }
                    }
                }
        }

    }
    fun checkIfUserIsLoggedIn() {
        if (auth.currentUser != null) {
            Log.d(TAG, "checkIfUserIsLoggedIn: User is Logged In")
        }
    }
    fun signInWithGoogle() {
        Log.d(TAG,"Attempting login with: Google")
    }
    fun resetPasswordMessages() {
        _passwordResetState.update { PasswordResetState() }
    }
    companion object{
        private val TAG = LoginViewModel::class.java.simpleName
    }
}