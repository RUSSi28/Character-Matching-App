package com.example.charactermatchingapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charactermatchingapp.domain.auth.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Generic UI state holder for Auth screens
data class AuthUiState(
    val isLoading: Boolean = false,
    val signUpError: String? = null,
    val isSignUpSuccess: Boolean = false,
    val loginError: String? = null,
    val isLoginSuccess: Boolean = false
)

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, signUpError = null, loginError = null) }
            val result = authRepository.signUp(email, password)
            result.fold(
                onSuccess = { uid ->
                    authRepository.saveUser(uid, email)
                    _uiState.update {
                        it.copy(isLoading = false, isSignUpSuccess = true)
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(isLoading = false, signUpError = exception.message)
                    }
                }
            )
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, signUpError = null, loginError = null) }
            val result = authRepository.login(email, password)
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(isLoading = false, isLoginSuccess = true)
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(isLoading = false, loginError = exception.message)
                    }
                }
            )
        }
    }

    fun resetAuthStates() {
        _uiState.update {
            it.copy(
                isLoginSuccess = false,
                isSignUpSuccess = false,
                loginError = null,
                signUpError = null
            )
        }
    }

    fun isUserLoggedIn(): Boolean {
        return authRepository.getCurrentUserUid() != null
    }

    fun signOut() {
        authRepository.signOut()
        resetAuthStates()
    }
}