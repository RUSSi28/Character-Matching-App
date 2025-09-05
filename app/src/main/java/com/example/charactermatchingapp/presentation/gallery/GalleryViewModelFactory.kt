package com.example.charactermatchingapp.presentation.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.charactermatchingapp.domain.gallery.repository.GalleryRepository
import com.example.charactermatchingapp.domain.auth.service.CurrentUserProvider

class GalleryViewModelFactory(
    private val galleryRepository: GalleryRepository,
    private val currentUserProvider: CurrentUserProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GalleryViewModel(galleryRepository, currentUserProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
