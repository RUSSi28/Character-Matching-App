package com.example.charactermatchingapp.presentation.recommendation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charactermatchingapp.domain.auth.service.CurrentUserProvider
import com.example.charactermatchingapp.domain.recommendation.model.Recommendation
import com.example.charactermatchingapp.domain.recommendation.repository.RecommendationRepository
import com.example.charactermatchingapp.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RecommendationUiState(
    val hasStarted: Boolean = false,
    val isLoading: Boolean = false,
    val recommendations: List<Recommendation> = emptyList(),
    val searchSuggestions: String? = null,
    val error: String? = null
)

class RecommendationViewModel(
    private val userRepository: UserRepository,
    private val recommendationRepository: RecommendationRepository,
    private val currentUserProvider: CurrentUserProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecommendationUiState())
    val uiState: StateFlow<RecommendationUiState> = _uiState.asStateFlow()

    fun fetchRecommendations() {
        viewModelScope.launch {
            _uiState.update { it.copy(hasStarted = true, isLoading = true, error = null) }

            val userId = currentUserProvider.getCurrentUserId()
            if (userId == null) {
                _uiState.update { it.copy(isLoading = false, error = "ログインしていません。") }
                return@launch
            }

            userRepository.getLikedTags(userId)
                .onSuccess { tags ->
                    if (tags.isEmpty()) {
                        _uiState.update { it.copy(isLoading = false, error = "いいねしたタグがありません。") }
                        return@onSuccess
                    }
                    recommendationRepository.getRecommendations(tags)
                        .onSuccess { result ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    recommendations = result.recommendations,
                                    searchSuggestions = result.searchSuggestions
                                )
                            }
                        }
                        .onFailure { e ->
                            _uiState.update { it.copy(isLoading = false, error = "おすすめの取得に失敗しました: ${e.message}") }
                        }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = "タグの取得に失敗しました: ${e.message}") }
                }
        }
    }
}