package com.example.charactermatchingapp.presentation.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charactermatchingapp.domain.auth.service.CurrentUserProvider
import com.example.charactermatchingapp.domain.gallery.repository.GalleryRepository
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val galleryRepository: GalleryRepository,
    private val currentUserProvider: CurrentUserProvider
) : ViewModel() {

    val galleryItems = galleryRepository.galleryItems
    val isLoading = galleryRepository.isLoading
    val canLoadNext = galleryRepository.canLoadNext
    val canLoadPrevious = galleryRepository.canLoadPrevious

    init {
        val currentUserId = currentUserProvider.getCurrentUserId()
        if (currentUserId != null) {
            galleryRepository.setUserId(currentUserId)
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            galleryRepository.loadNextPage()
        }
    }

    fun loadPreviousPage() {
        viewModelScope.launch {
            galleryRepository.loadPreviousPage()
        }
    }
}
