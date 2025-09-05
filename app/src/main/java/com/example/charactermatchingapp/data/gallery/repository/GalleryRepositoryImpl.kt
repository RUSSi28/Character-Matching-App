package com.example.charactermatchingapp.data.gallery.repository

import com.example.charactermatchingapp.domain.gallery.model.GalleryItem
import com.example.charactermatchingapp.domain.gallery.repository.GalleryRepository
import com.example.charactermatchingapp.data.gallery.remote.model.ArtworkDto
import com.google.firebase.firestore.FirebaseFirestore
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
            querySnapshot.documents.map { document ->
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
            }.filterNotNull()
        } catch (e: Exception) {
            // Log the exception or handle it appropriately
            emptyList() // Return an empty list on error
        }
    }
}