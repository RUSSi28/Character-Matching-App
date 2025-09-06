package com.example.charactermatchingapp.data.auth.repository

import com.example.charactermatchingapp.domain.auth.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

import com.google.firebase.Timestamp

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun signUp(email: String, password: String, displayName: String): Result<String> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid
            if (uid != null) {
                val accountData = hashMapOf(
                    "email" to email,
                    "displayName" to displayName,
                    "iconImageUrl" to null,
                    "headerImageUrl" to null,
                    "bio" to null,
                    "likedCount" to 0,
                    "postsCount" to 0,
                    "createdAt" to Timestamp.now()
                )
                firestore.collection("accounts").document(uid).set(accountData).await()
                Result.success(uid)
            } else {
                Result.failure(Exception("SignUp succeeded but UID is null."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUserUid(): String? {
        return auth.currentUser?.uid
    }

    override fun signOut() {
        auth.signOut()
    }
}