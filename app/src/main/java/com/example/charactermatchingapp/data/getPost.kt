package com.example.charactermatchingapp.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import androidx.paging.PagingData
import com.example.charactermatchingapp.domain.matching.model.Post
import androidx.paging.PagingConfig
import PostPagingSource
import androidx.paging.Pager

class PostRepository(
    private val firestore: FirebaseFirestore
) {
    // タイムライン用の全投稿を取得するPager（既存）
    fun getPostsPager(): Flow<PagingData<Post>> {
        val query = firestore.collection("artworks")
            .orderBy("createdAt", Query.Direction.DESCENDING) // 例：artworksコレクション、作成日時順

        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { PostPagingSource(query) }
        ).flow
    }

    // ★★★ 特定ユーザーの投稿を取得する新しいPagerを追加 ★★★
    fun getUserArtworksPager(accountId: String): Flow<PagingData<Post>> {
        // authorIdが指定されたaccountIdと一致する投稿を取得し、新しい順に並べるクエリ
        val query = firestore.collection("artworks")
            .whereEqualTo("authorId", accountId)
            .orderBy("createdAt", Query.Direction.DESCENDING)

        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { PostPagingSource(query) } // 同じPagingSourceをクエリを変えて再利用
        ).flow
    }
}