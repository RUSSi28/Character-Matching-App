package com.example.charactermatchingapp.presentation.sns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.charactermatchingapp.data.PostRepository
import com.example.charactermatchingapp.domain.matching.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * タイムライン画面用のViewModel
 * @param accountId 表示する投稿の所有者ID
 * @param initialPostId 初期表示時にスクロールする対象の投稿ID
 * @param postRepository 投稿データを取得するためのリポジトリ
 */
class TimelineViewModel(
    private val accountId: String,
    private val initialPostId: String,
    private val postRepository: PostRepository
) : ViewModel() {

    val posts: Flow<PagingData<Post>> = postRepository.getUserArtworks(accountId)
        .cachedIn(viewModelScope)
    // ★★★ 初期スクロール位置のインデックスを保持するStateFlow ★★★
    private val _initialScrollIndex = MutableStateFlow(-1) // -1: 未計算
    val initialScrollIndex: StateFlow<Int> = _initialScrollIndex.asStateFlow()

    // 最初のインデックス検索を一度だけ実行するためのフラグ
    private var isInitialIndexSearched = false

    /**
     * UI側から投稿リストが渡された際に、initialPostIdに一致する投稿のインデックスを検索し、
     * initialScrollIndexを更新する。
     * 検索は一度しか実行されない。
     * @param posts 現在表示されている投稿のリスト（スナップショット）
     */
    fun findInitialPostIndex(posts: List<Post?>) {
        // 既に検索済みか、スクロール対象のIDがなければ何もしない
        if (isInitialIndexSearched || initialPostId.isBlank()) {
            return
        }

        val index = posts.indexOfFirst { it?.id == initialPostId }
        if (index != -1) {
            _initialScrollIndex.value = index
            isInitialIndexSearched = true // 検索済みフラグを立てる
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
            return TimelineViewModel(accountId, initialPostId , postRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}