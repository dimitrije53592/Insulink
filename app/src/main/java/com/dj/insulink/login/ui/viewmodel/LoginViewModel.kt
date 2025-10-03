package com.dj.insulink.login.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {
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
    }

    fun signInWithGoogle() {
        Log.d(TAG,"Attempting login with: Google")
    }

    companion object{
        private val TAG = LoginViewModel::class.java.simpleName
    }
}