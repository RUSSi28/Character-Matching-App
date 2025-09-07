package com.example.charactermatchingapp.presentation.sns

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charactermatchingapp.domain.auth.service.CurrentUserProvider
import com.example.charactermatchingapp.domain.matching.model.Profile
import com.example.charactermatchingapp.domain.user.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// UIの状態をすべて保持するデータクラス
data class AccountSettingsUiState(
    val headerImageUrl: String? = null,
    val iconImageUrl: String? = null,
    val profileText: String? = null,
    val isLoading: Boolean = true
)

class AccountSettingsViewModel(
    private val userRepository: UserRepository,
    private val currentUserProvider: CurrentUserProvider
) : ViewModel() {

    // UIの状態を保持するStateFlow
    private val _uiState = MutableStateFlow(AccountSettingsUiState())
    val uiState: StateFlow<AccountSettingsUiState> = _uiState.asStateFlow()

    // private val db = FirebaseFirestore.getInstance() // No longer needed

    /**
     * 指定されたaccountIdのプロフィール情報をFirestoreから取得する
     */
    fun loadProfile(accountId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val userId = currentUserProvider.getCurrentUserId()
            if (userId == null) {
                _uiState.update { it.copy(isLoading = false) } // Not logged in
                return@launch
            }

            userRepository.getUserProfile(userId)
                .onSuccess { profile ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            headerImageUrl = profile.headerImageUrl,
                            iconImageUrl = profile.iconImageUrl,
                            profileText = profile.bio,
                            isLoading = false
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false) } // Error loading profile
                }
        }
    }

    // --- UIからのイベントを処理する関数 ---

    fun onProfileTextChange(newText: String) {
        _uiState.update { it.copy(profileText = newText) }
    }

    fun onHeaderImageSelected(uri: Uri?) {
        _uiState.update { it.copy(headerImageUrl = uri?.toString() ?: it.headerImageUrl) }
    }

    fun onIconImageSelected(uri: Uri?) {
        _uiState.update { it.copy(iconImageUrl = uri?.toString() ?: it.iconImageUrl) }
    }

    /**
     * 更新処理：現在のUIの状態をFirestoreに保存する
     */
    fun updateProfile(accountId: String) {
        viewModelScope.launch {
            val userId = currentUserProvider.getCurrentUserId()
            if (userId == null) return@launch

            val currentState = _uiState.value

            userRepository.getUserProfile(userId)
                .onSuccess { currentProfile ->
                    val updatedProfile = currentProfile.copy(
                        headerImageUrl = currentState.headerImageUrl,
                        iconImageUrl = currentState.iconImageUrl,
                        bio = currentState.profileText
                    )
                    userRepository.updateUserProfile(userId, updatedProfile)
                        .onSuccess { /* Success feedback */ }
                        .onFailure { /* Error feedback */ }
                }
                .onFailure { /* Error fetching current profile */ }
        }
    }
}