package com.example.charactermatchingapp.domain.gallery.repository

import com.example.charactermatchingapp.domain.gallery.model.GalleryItem

interface GalleryRepository {
    suspend fun getGalleryItems(): List<GalleryItem>
    suspend fun getLikedGalleryItems(userId: String): List<GalleryItem>
}
