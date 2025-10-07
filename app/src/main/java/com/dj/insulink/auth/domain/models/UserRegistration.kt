package com.dj.insulink.auth.domain.models

data class UserRegistration(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)