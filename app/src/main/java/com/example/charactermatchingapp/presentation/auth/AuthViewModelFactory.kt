package com.example.charactermatchingapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.charactermatchingapp.domain.auth.repository.AuthRepository
import com.example.charactermatchingapp.domain.auth.usecase.LoginUseCase
import com.example.charactermatchingapp.domain.auth.usecase.SignUpUseCase

class AuthViewModelFactory(
    private val authRepository: AuthRepository,
    private val loginUseCase: LoginUseCase,
    private val signUpUseCase: SignUpUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
                        return AuthViewModel(authRepository, loginUseCase, signUpUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}