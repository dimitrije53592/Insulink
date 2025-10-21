package com.dj.insulink.auth.domain.models

data class User(
    val uid: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val friendCode: String,
    val isEmailVerified: Boolean = false
)
