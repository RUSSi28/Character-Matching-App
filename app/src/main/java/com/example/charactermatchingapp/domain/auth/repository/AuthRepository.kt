package com.example.charactermatchingapp.domain.auth.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val mail: Flow<String>
    val password: Flow<String>

    suspend fun signUp(email: String, password: String, displayName: String): Result<String>
    suspend fun login(email: String, password: String): Result<Unit>
    fun getCurrentUserUid(): String?
    fun signOut()
}
