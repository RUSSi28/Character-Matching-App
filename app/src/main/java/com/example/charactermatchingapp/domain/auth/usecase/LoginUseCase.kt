package com.example.charactermatchingapp.domain.auth.usecase

import com.example.charactermatchingapp.domain.auth.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.login(email, password)
    }
}