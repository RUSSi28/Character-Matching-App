package com.example.charactermatchingapp.data.gallery.repository

import android.util.Log
import com.example.charactermatchingapp.domain.gallery.model.GalleryItem
import com.example.charactermatchingapp.domain.gallery.repository.GalleryRepository
import com.example.charactermatchingapp.data.gallery.remote.model.ArtworkDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldPath // Import FieldPath
import kotlinx.coroutines.tasks.await

/**
 * [GalleryRepositoryImpl] fetches a list of [GalleryItem] from Firestore
 */
class GalleryRepositoryImpl(
    private val db: FirebaseFirestore
) : GalleryRepository {
    override suspend fun getGalleryItems(): List<GalleryItem> {
        return try {
            val querySnapshot = db.collection("artworks").get().await()
            querySnapshot.documents.mapNotNull { document ->
                val artworkDto = document.toObject(ArtworkDto::class.java)
                artworkDto?.let {
                    GalleryItem(
                        artworkId = it.artworkId,
                        authorId = it.authorId,
                        authorName = it.authorName,
                        characterName = it.characterName,
                        characterDescription = it.characterDescription,
                        imageUrl = it.imageUrl,
                        thumbUrl = it.thumbUrl,
                        tags = it.tags,
                        likeCount = it.likeCount,
                        postedAt = it.postedAt
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("GalleryRepoImpl", "Error fetching all gallery items", e)
            emptyList()
        }
    }

    override suspend fun getLikedGalleryItems(
        userId: String,
        limit: Long,
        startAfterArtworkId: String?
    ): List<GalleryItem> {
        Log.d("GalleryRepoImpl", "Fetching liked gallery items for userId: $userId, limit: $limit, startAfter: $startAfterArtworkId")
        return try {
            var query = db.collection("accounts").document(userId)
                .collection("likes")
                .orderBy(FieldPath.documentId())
                .limit(limit + 1) // Fetch one more item to check for next page

            if (startAfterArtworkId != null) {
                query = query.startAfter(startAfterArtworkId)
            }

            val likedArtworkIds = query.get().await().documents.mapNotNull { it.id }

            Log.d("GalleryRepoImpl", "Liked artwork IDs for page: $likedArtworkIds")

            if (likedArtworkIds.isEmpty()) {
                Log.d("GalleryRepoImpl", "No liked artwork IDs found for current page.")
                return emptyList()
            }

            val allLikedItems = mutableListOf<GalleryItem>()
            val batchSize = 10 // Firestore's whereIn query limit

            likedArtworkIds.chunked(batchSize).forEach { batch ->
                val artworksQuerySnapshot = db.collection("artworks")
                    .whereIn(FieldPath.documentId(), batch)
                    .get()
                    .await()

                artworksQuerySnapshot.documents.mapNotNullTo(allLikedItems) { document ->
                    val artworkDto = document.toObject(ArtworkDto::class.java)
                    artworkDto?.let {
                        GalleryItem(
                            artworkId = it.artworkId,
                            authorId = it.authorId,
                            authorName = it.authorName,
                            characterName = it.characterName,
                            characterDescription = it.characterDescription,
                            imageUrl = it.imageUrl,
                            thumbUrl = it.thumbUrl,
                            tags = it.tags,
                            likeCount = it.likeCount,
                            postedAt = it.postedAt
                        )
                    }
                }
            }
            Log.d("GalleryRepoImpl", "Fetched ${allLikedItems.size} liked gallery items for page.")
            allLikedItems
        } catch (e: Exception) {
            Log.e("GalleryRepoImpl", "Error fetching liked gallery items", e)
            emptyList()
        }
    }
}