package com.example.charactermatchingapp.data.user.repository

import com.example.charactermatchingapp.domain.user.repository.UserRepository
import com.example.charactermatchingapp.domain.matching.model.Profile
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

    override suspend fun getUserProfile(userId: String): Result<Profile> {
        return try {
            val document = firestore.collection("accounts").document(userId).get().await()
            if (document.exists()) {
                val profile = document.toObject(Profile::class.java)
                if (profile != null) {
                    Result.success(profile)
                } else {
                    Result.failure(Exception("Failed to parse user profile."))
                }
            } else {
                Result.failure(Exception("User profile not found."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(userId: String, profile: Profile): Result<Unit> {
        return try {
            val updates = hashMapOf<String, Any?>(
                "displayName" to profile.displayName,
                "iconImageUrl" to profile.iconImageUrl,
                "headerImageUrl" to profile.headerImageUrl,
                "bio" to profile.bio
            )
            firestore.collection("accounts").document(userId).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
