package com.dj.insulink.auth.data

import android.util.Log
import com.dj.insulink.auth.domain.models.User
import com.dj.insulink.auth.domain.models.UserRegistration
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun registerUser(userRegistration: UserRegistration): User {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(
            userRegistration.email,
            userRegistration.password
        ).await()

        val user = authResult.user
            ?: throw Exception("User creation failed")

        val displayName = "${userRegistration.firstName} ${userRegistration.lastName}"
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()

        user.updateProfile(profileUpdates).await()

        val userData = hashMapOf(
            "firstName" to userRegistration.firstName,
            "lastName" to userRegistration.lastName,
            "email" to userRegistration.email,
            "createdAt" to Timestamp.now(),
            "userId" to user.uid
        )

        firestore.collection("users")
            .document(user.uid)
            .set(userData)
            .await()

        user.sendEmailVerification().await()

        return User(
            uid = user.uid,
            firstName = userRegistration.firstName,
            lastName = userRegistration.lastName,
            email = userRegistration.email,
            isEmailVerified = user.isEmailVerified
        )
    }

    suspend fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser ?: return null

        val userDoc = firestore.collection("users")
            .document(firebaseUser.uid)
            .get()
            .await()

        return User(
            uid = firebaseUser.uid,
            firstName = userDoc.getString("firstName") ?: "",
            lastName = userDoc.getString("lastName") ?: "",
            email = firebaseUser.email ?: "",
            isEmailVerified = firebaseUser.isEmailVerified
        )
    }

    suspend fun signOut() {
        firebaseAuth.signOut()
    }

    suspend fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

}