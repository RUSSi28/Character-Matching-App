package com.example.charactermatchingapp.domain.gallery.usecase

import com.example.charactermatchingapp.domain.gallery.model.GalleryItem
import com.example.charactermatchingapp.domain.gallery.repository.GalleryRepository

import com.google.firebase.auth.FirebaseAuth

class GetGalleryItemsUseCase(
    private val repository: GalleryRepository,
    private val auth: FirebaseAuth
) {
    suspend operator fun invoke(): List<GalleryItem> {
        val userId = auth.currentUser?.uid
        return if (userId != null) {
            repository.getLikedGalleryItems(userId)
        } else {
            emptyList()
        }
    }
}
