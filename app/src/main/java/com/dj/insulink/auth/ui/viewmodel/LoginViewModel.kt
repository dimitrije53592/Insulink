package com.dj.insulink.auth.ui.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    private val _passwordResetState = MutableStateFlow(PasswordResetState())
    val passwordResetState: StateFlow<PasswordResetState> = _passwordResetState.asStateFlow()

    fun login() {
        Log.d(TAG, "Attempting login with: $email and $password")
        if (_email.value.isBlank() || _password.value.isBlank()) {
            Log.d(TAG, "Email or password is empty")
            return
        }
        viewModelScope.launch {
            try {
                val result = auth.signInWithEmailAndPassword(_email.value, _password.value).await()
                val user = result.user
                println("Login Success: User ${user?.uid}")
            } catch (e: Exception) {
                println("Login Failed: ${e.message}")
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

        _passwordResetState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )
        }

        viewModelScope.launch {
            auth.sendPasswordResetEmail(_email.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Password reset link sent to $email.",
                            Toast.LENGTH_LONG
                        ).show()
                        _passwordResetState.update {
                            it.copy(isLoading = false, successMessage = "Password reset link sent.")
                        }
                    } else {
                        val msg = task.exception?.message ?: "An unknown error occurred."
                        Toast.makeText(context, "Error: $msg", Toast.LENGTH_LONG).show()
                        _passwordResetState.update {
                            it.copy(isLoading = false, errorMessage = msg)
                        }
                    }
                }
        }

    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun signInWithGoogle() {
        Log.d(TAG, "Attempting login with: Google")
    }

    private val TAG = LoginViewModel::class.java.simpleName
}

data class PasswordResetState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
