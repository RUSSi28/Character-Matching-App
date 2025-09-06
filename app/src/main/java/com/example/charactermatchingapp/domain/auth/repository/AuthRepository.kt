package com.example.charactermatchingapp.domain.auth.repository

interface AuthRepository {
    suspend fun signUp(email: String, password: String, displayName: String): Result<String>
    suspend fun login(email: String, password: String): Result<Unit>
    fun getCurrentUserUid(): String?
    fun signOut()
}
