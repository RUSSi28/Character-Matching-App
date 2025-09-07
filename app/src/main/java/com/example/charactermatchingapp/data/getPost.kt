package com.example.charactermatchingapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.charactermatchingapp.domain.matching.model.Post
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class PostRepository {
    private val firestore = FirebaseFirestore.getInstance()

    /**
     * タイムライン用の全投稿を一度に取得する
     * @return Postのリスト
     */
    suspend fun getAllArtworks(): List<Post> {
        return try {
            val query = firestore.collection("artworks")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(100) // 念のため上限を設定

            val documents = query.get().await()

            // 取得したドキュメントをPostオブジェクトのリストに変換
            return documents.map { document ->
                Post(
                    id = document.id,
                    userName = document.getString("authorName") ?: "",
                    userIconResId = document.getString("iconImageUrl") ?: "",
                    characterName = document.getString("characterName") ?: "",
                    characterText = document.getString("characterDescription") ?: "",
                    postImageResId = document.getString("imageUrl") ?: "",
                    posttags = document.get("tags") as? List<String> ?: emptyList()
                )
            }
        } catch (e: Exception) {
            println("Error getting all artworks: ${e.message}")
            emptyList() // エラー時は空のリストを返す
        }
    }

    /**
     * 特定ユーザーの投稿をページングで取得する
     * @param accountId 取得したいユーザーのID
     * @return PostのPagingDataを含むFlow
     */
    fun getUserArtworks(accountId: String): Flow<PagingData<Post>> {
        val query = firestore.collection("artworks")
            .whereEqualTo("authorId", accountId)
            .orderBy("createdAt", Query.Direction.DESCENDING)

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PostPagingSource(query) }
        ).flow
    }
    /**
     * 特定ユーザーがいいねした投稿をすべて取得する
     * @param accountId いいね一覧を取得したいユーザーのID
     * @return Postのリスト
     */
    suspend fun getFavoriteArtworks(accountId: String): List<Post> {
        return try {
            // 1. ユーザーの 'likes' サブコレクションから参照のリストを取得
            val likesSnapshot = firestore.collection("accounts").document(accountId)
                .collection("likes")
                .get()
                .await()

            if (likesSnapshot.isEmpty) {
                return emptyList() // いいねした投稿がない場合は空のリストを返す
            }

            // DocumentReferenceからartworkのIDのみを抽出する
            val artworkIds = likesSnapshot.documents.mapNotNull { doc ->
                doc.getDocumentReference("artworkRef")?.id
            }

            if (artworkIds.isEmpty()) {
                return emptyList()
            }

            // 2. 取得したIDリストを使って、artworksを一括取得する (whereInクエリ)
            // 注意: whereInで一度に指定できるIDは最大30個です。
            // 30個以上を考慮する場合は、artworkIdsを30個ずつのチャンクに分割して複数回クエリを実行する必要があります。
            val artworksSnapshot = firestore.collection("artworks")
                .whereIn(FieldPath.documentId(), artworkIds)
                .get()
                .await()

            // 取得したドキュメントをPostオブジェクトのリストに変換
            return artworksSnapshot.map { document ->
                Post(
                    id = document.id,
                    userName = document.getString("authorName") ?: "",
                    userIconResId = document.getString("iconImageUrl") ?: "",
                    characterName = document.getString("characterName") ?: "",
                    characterText = document.getString("characterDescription") ?: "",
                    postImageResId = document.getString("imageUrl") ?: "",
                    posttags = document.get("tags") as? List<String> ?: emptyList()
                )
            }
        } catch (e: Exception) {
            println("Error getting favorite artworks: ${e.message}")
            emptyList()
        }
    }
}