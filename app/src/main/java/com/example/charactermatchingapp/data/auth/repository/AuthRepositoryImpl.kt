package com.example.charactermatchingapp.data.auth.repository

import com.example.charactermatchingapp.domain.auth.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun signUp(email: String, password: String): Result<String> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid
            if (uid != null) {
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

    override suspend fun saveUser(uid: String, email: String): Result<Unit> {
        return try {
            val userMap = hashMapOf(
                "email" to email,
                "createdAt" to System.currentTimeMillis()
            )
            firestore.collection("users").document(uid).set(userMap).await()
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