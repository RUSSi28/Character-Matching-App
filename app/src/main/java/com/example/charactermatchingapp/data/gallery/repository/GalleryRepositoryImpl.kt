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
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
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

    override suspend fun getLikedGalleryItems(userId: String): List<GalleryItem> {
        Log.d("GalleryRepoImpl", "Fetching liked gallery items for userId: $userId")
        return try {
            val likedArtworkIds = db.collection("accounts").document(userId)
                .collection("likes")
                .get()
                .await()
                .documents
                .mapNotNull { it.id }

            Log.d("GalleryRepoImpl", "Liked artwork IDs: $likedArtworkIds")

            if (likedArtworkIds.isEmpty()) {
                Log.d("GalleryRepoImpl", "No liked artwork IDs found.")
                return emptyList()
            }

            // Firestore's whereIn query has a limit of 10. If more, you need to split queries.
            // For simplicity, assuming less than 10 for now or handling in batches if needed.
            val artworksQuerySnapshot = db.collection("artworks")
                .whereIn(FieldPath.documentId(), likedArtworkIds) // FIX: Query by document ID
                .get()
                .await()

            val likedItems = artworksQuerySnapshot.documents.mapNotNull { document ->
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
            Log.d("GalleryRepoImpl", "Fetched ${likedItems.size} liked gallery items.")
            likedItems
        } catch (e: Exception) {
            Log.e("GalleryRepoImpl", "Error fetching liked gallery items", e)
            emptyList()
        }
    }
}