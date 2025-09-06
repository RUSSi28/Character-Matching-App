package com.example.charactermatchingapp.data.gallery.repository

import android.util.Log
import com.example.charactermatchingapp.data.gallery.remote.model.ArtworkDto
import com.example.charactermatchingapp.domain.gallery.model.GalleryItem
import com.example.charactermatchingapp.domain.gallery.repository.GalleryRepository
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val PAGE_SIZE = 1L

class GalleryRepositoryImpl(
    private val db: FirebaseFirestore
) : GalleryRepository {

    private val _galleryItems = MutableStateFlow<List<GalleryItem>>(emptyList())
    override val galleryItems: StateFlow<List<GalleryItem>> = _galleryItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _canLoadNext = MutableStateFlow(false)
    override val canLoadNext: StateFlow<Boolean> = _canLoadNext.asStateFlow()

    private val _canLoadPrevious = MutableStateFlow(false)
    override val canLoadPrevious: StateFlow<Boolean> = _canLoadPrevious.asStateFlow()

    private var userId: String? = null
    private val pageKeys = mutableListOf<String?>(null)
    private var currentPage = 1

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    override fun setUserId(userId: String) {
        this.userId = userId
        resetAndLoadFirstPage()
    }

    private fun resetAndLoadFirstPage() {
        pageKeys.clear()
        pageKeys.add(null)
        currentPage = 1
        repositoryScope.launch {
            loadPage(1)
        }
    }

    override suspend fun loadNextPage() {
        if (canLoadNext.value && !isLoading.value) {
            loadPage(currentPage + 1)
        }
    }

    override suspend fun loadPreviousPage() {
        if (canLoadPrevious.value && !isLoading.value) {
            loadPage(currentPage - 1)
        }
    }

    private suspend fun loadPage(page: Int) {
        val currentUserId = userId
        if (currentUserId == null) {
            _galleryItems.value = emptyList()
            _canLoadNext.value = false
            _canLoadPrevious.value = false
            return
        }

        _isLoading.value = true
        val startAfterId = pageKeys.getOrNull(page - 1)

        try {
            val fetchedItems = fetchLikedGalleryItems(currentUserId, PAGE_SIZE + 1, startAfterId)
            val newItemsForDisplay = fetchedItems.take(PAGE_SIZE.toInt())
            _galleryItems.value = newItemsForDisplay

            if (newItemsForDisplay.size.toLong() == PAGE_SIZE && pageKeys.size <= page) {
                pageKeys.add(newItemsForDisplay.lastOrNull()?.artworkId)
            }

            val hasMore = fetchedItems.size.toLong() > PAGE_SIZE
            _canLoadNext.value = hasMore
            _canLoadPrevious.value = page > 1
            currentPage = page

        } catch (e: Exception) {
            Log.e("GalleryRepoImpl", "Error loading gallery items for page $page", e)
            // Consider exposing error state
        } finally {
            _isLoading.value = false
        }
    }

    private suspend fun fetchLikedGalleryItems(
        userId: String,
        limit: Long,
        startAfterArtworkId: String?
    ): List<GalleryItem> {
        return try {
            var query = db.collection("accounts").document(userId)
                .collection("likes")
                .orderBy(FieldPath.documentId())
                .limit(limit)

            if (startAfterArtworkId != null) {
                query = query.startAfter(startAfterArtworkId)
            }

            val likedArtworkIds = query.get().await().documents.mapNotNull { it.id }

            if (likedArtworkIds.isEmpty()) {
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
                    artworkDto?.let { dto ->
                        if (!dto.imageUrl.isNullOrBlank()) {
                            GalleryItem(
                                artworkId = dto.artworkId,
                                authorId = dto.authorId,
                                authorName = dto.authorName,
                                characterName = dto.characterName,
                                characterDescription = dto.characterDescription,
                                imageUrl = dto.imageUrl,
                                thumbUrl = dto.thumbUrl,
                                tags = dto.tags,
                                likeCount = dto.likeCount,
                                postedAt = dto.postedAt
                            )
                        } else {
                            Log.w("GalleryRepoImpl", "Artwork with id ${dto.artworkId} has null or blank imageUrl.")
                            null // This will filter out the item
                        }
                    }
                }
            }
            allLikedItems
        } catch (e: Exception) {
            Log.e("GalleryRepoImpl", "Error fetching liked gallery items", e)
            emptyList()
        }
    }

    override suspend fun getGalleryItems(): List<GalleryItem> {
        return try {
            val querySnapshot = db.collection("artworks").get().await()
            querySnapshot.documents.mapNotNull { document ->
                val artworkDto = document.toObject(ArtworkDto::class.java)
                artworkDto?.let { dto ->
                    if (!dto.imageUrl.isNullOrBlank()) {
                        GalleryItem(
                            artworkId = dto.artworkId,
                            authorId = dto.authorId,
                            authorName = dto.authorName,
                            characterName = dto.characterName,
                            characterDescription = dto.characterDescription,
                            imageUrl = dto.imageUrl,
                            thumbUrl = dto.thumbUrl,
                            tags = dto.tags,
                            likeCount = dto.likeCount,
                            postedAt = dto.postedAt
                        )
                    } else {
                        Log.w("GalleryRepoImpl", "Artwork with id ${dto.artworkId} has null or blank imageUrl.")
                        null
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("GalleryRepoImpl", "Error fetching all gallery items", e)
            emptyList()
        }
    }
}
