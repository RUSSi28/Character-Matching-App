package com.example.charactermatchingapp.data.matching.repository

import android.util.Log
import com.example.charactermatchingapp.data.matching.model.CharacterData
import com.example.charactermatchingapp.domain.auth.service.CurrentUserProvider
import com.example.charactermatchingapp.domain.matching.model.CharacterInfo
import com.example.charactermatchingapp.domain.matching.repository.CharacterMatchingRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await

class CharacterMatchingRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val currentUserProvider: CurrentUserProvider
) : CharacterMatchingRepository {
    val userId = currentUserProvider.getCurrentUserId()

    override suspend fun getMatchingCharactersInfo(): List<CharacterInfo> {
        return try {
            if (userId != null) {
                val artworkQuerySnapshot = firestore.collection("artworks").get().await()
                val accountQuerySnapshot = firestore.collection("accounts").document(userId)
                    .collection("likes").get().await()
                val allDocumentIds = artworkQuerySnapshot.documents.mapNotNull { it.id }
                val likesDocumentIds = accountQuerySnapshot.documents.mapNotNull { it.id }
                val restDocumentIds = allDocumentIds - likesDocumentIds

                val matchingCharacters =
                    restDocumentIds.shuffled().take(10).mapNotNull { documentId ->
                        getMatchingCharacterInfo(documentId)
                    }
                matchingCharacters
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("CharacterMatchingRepoImpl", "Error fetching matching characters", e)
            emptyList()
        }
    }

    override suspend fun getMatchingCharacterInfo(documentId: String): CharacterInfo? {
        return try {
            val querySnapshot =
                firestore.collection("artworks").document(documentId).get().await()
            val artwork = querySnapshot.let { document ->
                val characterData = document.toObject(CharacterData::class.java)
                characterData?.let {
                    CharacterInfo(
                        id = it.id,
                        name = it.name,
                        image = it.imageUrl ?: "",
                        description = it.description,
                        tags = it.tags,
                        userName = it.userName,
                        likes = it.likes,
                        postedAt = it.postedAt
                    )
                }
            }
            Log.d("CharacterMatchingRepoImpl", "Fetched artwork: $artwork")
            artwork
        } catch (e: Exception) {
            Log.e("CharacterMatchingRepoImpl", "Error fetching matching character", e)
            null
        }
    }

    override suspend fun likeCharacterInfo(characterInfo: CharacterInfo) {
        try {
            // ユーザーのいいねコレクションにキャラクター情報を保存
            if (userId != null) {
                firestore.collection("accounts").document(userId)
                    .collection("likes").document(characterInfo.id).set(characterInfo).await()
            }
            // キャラクターのいいね数を更新
            firestore.collection("artworks").document(characterInfo.id)
                .update("likes", characterInfo.likes + 1).await()
        } catch (e: Exception) {
            Log.e("CharacterMatchingRepoImpl", "Error liking character", e)
        }
    }

    override fun bookmarkCharacterInfo(characterInfo: CharacterInfo) {
        TODO("Not yet implemented")
    }

    override fun dislikeCharacterInfo(characterInfo: CharacterInfo) {
        TODO("Not yet implemented")
    }

    override suspend fun onCardLiked(userId: String, likedArtworkTags: List<String>): Result<Unit> {
        return try {
            firestore.collection("accounts").document(userId)
                .update("likesTags", FieldValue.arrayUnion(*likedArtworkTags.toTypedArray()))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("CharacterMatchingRepoImpl", "Error adding likesTags", e)
            Result.failure(e)
        }
    }
}