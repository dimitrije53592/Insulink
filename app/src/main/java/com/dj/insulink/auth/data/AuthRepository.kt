package com.dj.insulink.auth.data

import android.util.Log
import com.dj.insulink.auth.domain.models.User
import com.dj.insulink.auth.domain.models.UserRegistration
import com.dj.insulink.core.utils.DeterministicCodeGenerator
import com.dj.insulink.feature.domain.models.Friend
import com.dj.insulink.feature.domain.models.GlucoseReading
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun registerUser(userRegistration: UserRegistration): User {
        val friendCode = DeterministicCodeGenerator.generateCodeFromEmail(userRegistration.email)

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
            "userId" to user.uid,
            "readings" to emptyList<GlucoseReading>(),
            "friendCode" to friendCode,
            "friends" to emptyList<Friend>()
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
            friendCode = friendCode,
            isEmailVerified = user.isEmailVerified
        )
    }

    fun getCurrentUserFlow(): Flow<String?> = flow {
        emit(getCurrentUser()?.uid)
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
            friendCode = userDoc.getString("friendCode") ?: "",
            isEmailVerified = firebaseUser.isEmailVerified
        )
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

}