package com.example.charactermatchingapp.presentation.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.charactermatchingapp.domain.gallery.repository.GalleryRepository
import com.google.firebase.auth.FirebaseAuth

class GalleryViewModelFactory(
    private val galleryRepository: GalleryRepository,
    private val auth: FirebaseAuth
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GalleryViewModel(galleryRepository, auth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
