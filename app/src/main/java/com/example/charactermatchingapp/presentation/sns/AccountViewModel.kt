package com.example.charactermatchingapp.presentation.sns

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.charactermatchingapp.domain.matching.model.Post
import com.example.charactermatchingapp.domain.matching.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.charactermatchingapp.data.PostRepository
import com.example.charactermatchingapp.data.ProfileRepository

class AccountViewModel(
    private val accountId: String,
    private val profileRepository: ProfileRepository, // Repositoryを注入
    private val postRepository: PostRepository      // Repositoryを注入
) : ViewModel() {

    // プロフィール情報を管理するStateFlow
    private val _profileState = MutableStateFlow<Profile?>(null)
    val profileState: StateFlow<Profile?> = _profileState.asStateFlow()
    private val _postsState = MutableStateFlow<List<Post>>(emptyList())
    val postsState: StateFlow<List<Post>> = _postsState.asStateFlow()

    init {
        // 初期化時にプロフィールと投稿の両方を取得開始
        loadProfile()
        loadPosts()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            // Repository経由でプロフィールを取得
            _profileState.value = profileRepository.getProfile(accountId)
        }
    }
    private fun loadPosts() {
        viewModelScope.launch {
            _postsState.value = postRepository.getUserArtworks(accountId)
        }
    }
}