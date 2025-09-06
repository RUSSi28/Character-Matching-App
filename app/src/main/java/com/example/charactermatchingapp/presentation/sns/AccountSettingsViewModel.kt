package com.example.charactermatchingapp.presentation.sns

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// UIの状態をすべて保持するデータクラス
data class AccountSettingsUiState(
    val headerImageUrl: String = "",
    val iconImageUrl: String = "",
    val profileText: String = "",
    val isLoading: Boolean = true
)

class AccountSettingsViewModel : ViewModel() {

    // UIの状態を保持するStateFlow
    private val _uiState = MutableStateFlow(AccountSettingsUiState())
    val uiState: StateFlow<AccountSettingsUiState> = _uiState.asStateFlow()

    private val db = FirebaseFirestore.getInstance()

    /**
     * 指定されたaccountIdのプロフィール情報をFirestoreから取得する
     */
    fun loadProfile(accountId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val document = db.collection("accounts").document(accountId).get().await()
                if (document.exists()) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            headerImageUrl = document.getString("headerImageUrl") ?: "",
                            iconImageUrl = document.getString("iconImageUrl") ?: "",
                            profileText = document.getString("bio") ?: "",
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) } // データがない場合
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) } // エラーの場合
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
            val currentState = _uiState.value
            val profileData = hashMapOf(
                "headerImageUrl" to currentState.headerImageUrl,
                "iconImageUrl" to currentState.iconImageUrl,
                "bio" to currentState.profileText
            )
            db.collection("accounts").document(accountId).set(profileData).await()
            // ここで成功・失敗のUIフィードバックを行うことも可能
        }
    }
}