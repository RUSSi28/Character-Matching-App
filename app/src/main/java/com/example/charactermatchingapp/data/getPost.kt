package com.example.charactermatchingapp.data

import com.example.charactermatchingapp.domain.matching.model.Post
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
     * 特定ユーザーの投稿を一度にすべて取得する
     * @param accountId 取得したいユーザーのID
     * @return Postのリスト
     */
    suspend fun getUserArtworks(accountId: String): List<Post> {
        return try {
            val query = firestore.collection("artworks")
                .whereEqualTo("authorId", accountId)
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
            println("Error getting user artworks: ${e.message}")
            emptyList()
        }
    }
}