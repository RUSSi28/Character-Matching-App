package com.example.charactermatchingapp.ui.sns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.charactermatchingapp.data.PostPagingSource
import kotlinx.coroutines.flow.Flow

class SnsViewModel : ViewModel() {

    val postPagingFlow: Flow<PagingData<Post>> = Pager(
        // Pagingの設定（1ページに何件読み込むかなど）
        config = PagingConfig(pageSize = 10),
        // どのPagingSourceを使うかを指定
        pagingSourceFactory = { PostPagingSource() }
    )
        .flow
        .cachedIn(viewModelScope) // データをキャッシュして画面回転などでも保持する
}