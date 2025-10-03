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
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    fun checkIfUserIsLoggedIn() {
        if (auth.currentUser != null) {
            Log.d(TAG, "checkIfUserIsLoggedIn: User is Logged In")
        }
    }
    fun signInWithGoogle() {
        Log.d(TAG,"Attempting login with: Google")
    }

    companion object{
        private val TAG = LoginViewModel::class.java.simpleName
    }
}