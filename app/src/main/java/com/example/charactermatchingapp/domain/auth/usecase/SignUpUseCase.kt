package com.example.charactermatchingapp.domain.auth.usecase

import com.example.charactermatchingapp.domain.auth.repository.AuthRepository

class SignUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        val signUpResult = authRepository.signUp(email, password)
        return signUpResult.fold(
            onSuccess = { uid ->
                // Save user data to Firestore after successful signup
                authRepository.saveUser(uid, email)
                Result.success(uid)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }
}