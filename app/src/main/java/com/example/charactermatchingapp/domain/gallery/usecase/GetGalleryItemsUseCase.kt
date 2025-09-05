package com.example.charactermatchingapp.domain.gallery.usecase

import com.example.charactermatchingapp.domain.gallery.model.GalleryItem
import com.example.charactermatchingapp.domain.gallery.repository.GalleryRepository

class GetGalleryItemsUseCase(private val repository: GalleryRepository) {
    suspend operator fun invoke(userId: String): List<GalleryItem> {
        return repository.getLikedGalleryItems(userId)
    }
}
