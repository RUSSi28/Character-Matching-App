package com.example.charactermatchingapp.ui.sns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.charactermatchingapp.data.PostPagingSource
import com.example.charactermatchingapp.domain.matching.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow

class SnsViewModel : ViewModel() {

    // ★★★ 1. Firestoreのインスタンスを取得 ★★★
    private val db = FirebaseFirestore.getInstance()

    // ★★★ 2. 取得したいデータのクエリを作成 ★★★
    // 例：「posts」コレクションを「createdAt」フィールドの降順（新しい順）で取得
    private val baseQuery = db.collection("posts")
        .orderBy("createdAt", Query.Direction.DESCENDING)


    val postPagingFlow: Flow<PagingData<Post>> = Pager(
        config = PagingConfig(pageSize = 10),
        // ★★★ 3. PagingSourceに作成したクエリを渡す ★★★
        pagingSourceFactory = { PostPagingSource(query = baseQuery) }
    )
        .flow
        .cachedIn(viewModelScope)
}