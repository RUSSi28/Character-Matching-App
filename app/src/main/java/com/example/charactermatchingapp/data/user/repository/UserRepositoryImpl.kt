package com.example.charactermatchingapp.data.user.repository

import com.example.charactermatchingapp.domain.user.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(private val firestore: FirebaseFirestore) : UserRepository {
    override suspend fun getLikedTags(userId: String): Result<List<String>> {
        return try {
            val document = firestore.collection("accounts").document(userId).get().await()
            if (document.exists()) {
                @Suppress("UNCHECKED_CAST")
                val tags = document.get("likesTags") as? List<String> ?: emptyList()
                Result.success(tags)
            } else {
                Result.success(emptyList()) // ドキュメントが存在しない場合は空のリストを返す
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
