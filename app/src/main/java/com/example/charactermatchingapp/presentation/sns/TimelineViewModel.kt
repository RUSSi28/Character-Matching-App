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
 * タイムライン画面用のViewModel
 * @param accountId 表示する投稿の所有者ID
 * @param initialPostId 最初に表示したい投稿のID
 * @param postRepository 投稿データを取得するためのリポジトリ
 */
class TimelineViewModel(
    private val accountId: String,
    private val initialPostId: String,
    private val postRepository: PostRepository
) : ViewModel() {

    // 投稿リストの状態を保持するStateFlow
    private val _postsState = MutableStateFlow<List<Post>>(emptyList())
    val postsState: StateFlow<List<Post>> = _postsState.asStateFlow()

    // 初期スクロール位置のインデックスを保持するStateFlow
    private val _initialScrollIndex = MutableStateFlow(0)
    val initialScrollIndex: StateFlow<Int> = _initialScrollIndex.asStateFlow()

    init {
        loadUserPosts()
    }

    private fun loadUserPosts() {
        viewModelScope.launch {
            // 指定されたアカウントの投稿をすべて取得
            val posts = postRepository.getUserArtworks(accountId)
            _postsState.value = posts

            // 取得した投稿リストの中から、最初に表示したい投稿のインデックスを探す
            val index = posts.indexOfFirst { it.id == initialPostId }
            if (index != -1) {
                // インデックスが見つかれば、初期スクロール位置として設定
                _initialScrollIndex.value = index
            }
        }
    }
}

/**
 * TimelineViewModelにリポジトリやIDを渡すためのFactoryクラス
 */
class TimelineViewModelFactory(
    private val accountId: String,
    private val initialPostId: String,
    private val postRepository: PostRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimelineViewModel::class.java)) {
            return TimelineViewModel(accountId, initialPostId, postRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}