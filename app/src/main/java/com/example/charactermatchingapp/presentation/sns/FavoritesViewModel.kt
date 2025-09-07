package com.example.charactermatchingapp.presentation.sns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.charactermatchingapp.data.PostRepository
import com.example.charactermatchingapp.domain.matching.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * お気に入り画面用のViewModel
 */
class FavoritesViewModel(
    private val accountId: String,
    private val postRepository: PostRepository
) : ViewModel() {

    // いいねした投稿のリストを保持するStateFlow
    private val _favoritesState = MutableStateFlow<List<Post>>(emptyList())
    val favoritesState: StateFlow<List<Post>> = _favoritesState.asStateFlow()

    init {
        // ViewModelの初期化時にいいねリストを読み込む
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            // リポジトリからいいねした投稿のリストを取得してStateを更新
            _favoritesState.value = postRepository.getFavoriteArtworks(accountId)
        }
    }
}

/**
 * FavoritesViewModelにaccountIdとリポジトリを渡すためのFactory
 */
class FavoritesViewModelFactory(
    private val accountId: String,
    private val postRepository: PostRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(accountId, postRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}