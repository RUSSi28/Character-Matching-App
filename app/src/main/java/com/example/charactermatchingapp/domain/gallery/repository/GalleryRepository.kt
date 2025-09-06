package com.example.charactermatchingapp.domain.gallery.repository

import com.example.charactermatchingapp.domain.gallery.model.GalleryItem
import kotlinx.coroutines.flow.StateFlow

interface GalleryRepository {
    val galleryItems: StateFlow<List<GalleryItem>>
    val isLoading: StateFlow<Boolean>
    val canLoadNext: StateFlow<Boolean>
    val canLoadPrevious: StateFlow<Boolean>

    fun setUserId(userId: String)
    suspend fun loadNextPage()
    suspend fun loadPreviousPage()

    // This method seems to be for a different purpose, so we keep it.
    suspend fun getGalleryItems(): List<GalleryItem>
}
