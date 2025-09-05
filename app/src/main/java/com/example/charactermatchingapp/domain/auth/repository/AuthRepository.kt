package com.example.charactermatchingapp.domain.auth.repository

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Result<String>
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun saveUser(uid: String, email: String): Result<Unit>
    fun getCurrentUserUid(): String?
    fun signOut()
}
