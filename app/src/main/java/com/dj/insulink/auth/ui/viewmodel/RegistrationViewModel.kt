package com.dj.insulink.auth.ui.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dj.insulink.auth.data.AuthRepository
import com.dj.insulink.auth.domain.models.UserRegistration
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _firstName = MutableStateFlow("")
    val firstName = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName = _lastName.asStateFlow()

    private val _emailAddress = MutableStateFlow("")
    val emailAddress = _emailAddress.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    private val _termsOfServiceAccepted = MutableStateFlow(false)
    val termsOfServiceAccepted = _termsOfServiceAccepted.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess = _registrationSuccess.asStateFlow()

    private val _showErrorMessage = MutableStateFlow(false)
    val showErrorMessage = _showErrorMessage.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    fun setFirstName(firstName: String) {
        _firstName.value = firstName
    }

    fun setLastName(lastName: String) {
        _lastName.value = lastName
    }

    fun setEmailAddress(emailAddress: String) {
        _emailAddress.value = emailAddress
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setConfirmPassword(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }

    fun setTermsOfServiceAccepted(termsOfServiceAccepted: Boolean) {
        _termsOfServiceAccepted.value = termsOfServiceAccepted
    }

    fun setShowErrorMessage(isVisible: Boolean) {
        _showErrorMessage.value = isVisible
    }

    fun onCreateAccountSubmit() {
        if (isRegistrationFormValid()) {
            createUserAccount()
        } else {
            setShowErrorMessage(true)
        }
    }

    private fun createUserAccount() {
        viewModelScope.launch {
            _isLoading.value = true
            _showErrorMessage.value = false

            try {
                val userRegistration = UserRegistration(
                    firstName = _firstName.value,
                    lastName = _lastName.value,
                    email = _emailAddress.value,
                    password = _password.value
                )

                val user = authRepository.registerUser(userRegistration)

                Log.d("RegistrationViewModel", "User registered successfully: ${user.uid}")

                _registrationSuccess.value = true
                _isLoading.value = false

            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = when {
                    e.message?.contains("email address is already in use") == true ->
                        "This email is already registered. Please try logging in."
                    e.message?.contains("network error") == true ->
                        "Network error. Please check your connection."
                    e.message?.contains("password") == true ->
                        "Password is too weak. Please use a stronger password."
                    else ->
                        "Registration failed: ${e.message}"
                }
                _showErrorMessage.value = true
                Log.e("RegistrationViewModel", "Registration error", e)
            }
        }
    }

    private fun isRegistrationFormValid(): Boolean {
        if (_firstName.value.isEmpty() || _lastName.value.isEmpty()) {
            _errorMessage.value = "Some required fields are left empty."
            return false
        } else if (!isEmailValid()) {
            _errorMessage.value = "Email address you entered is not a valid email address."
            return false
        } else if (!isPasswordLongEnough()) {
            _errorMessage.value = "Password you entered has to have at least 8 characters."
            return false
        } else if (!doPasswordsMatch()) {
            _errorMessage.value = "Passwords do not match."
            return false
        } else if (!areTermsOfServiceAccepted()) {
            _errorMessage.value = "You must accept Terms of Service and Privacy Policy"
            return false
        } else {
            return true
        }
    }

    private fun isEmailValid(): Boolean {
        if (_emailAddress.value.isEmpty()) {
            return false
        }

        return Patterns.EMAIL_ADDRESS.matcher(_emailAddress.value).matches()
    }

    private fun isPasswordLongEnough(): Boolean {
        return _password.value.length >= 8
    }

    private fun doPasswordsMatch(): Boolean {
        return _password.value == _confirmPassword.value
    }

    private fun areTermsOfServiceAccepted(): Boolean {
        return _termsOfServiceAccepted.value
    }
}