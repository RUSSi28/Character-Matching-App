package com.example.charactermatchingapp.presentation.gallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charactermatchingapp.domain.gallery.model.GalleryItem
import com.example.charactermatchingapp.domain.gallery.repository.GalleryRepository
import com.example.charactermatchingapp.domain.auth.service.CurrentUserProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val galleryRepository: GalleryRepository,
    private val currentUserProvider: CurrentUserProvider
) : ViewModel() {

    private val _galleryItemList = MutableStateFlow<List<GalleryItem>>(emptyList())
    val galleryItemList: StateFlow<List<GalleryItem>> = _galleryItemList.asStateFlow()

    init {
        loadGalleryItems()
    }

    private fun loadGalleryItems() {
        viewModelScope.launch {
            val currentUserId = currentUserProvider.getCurrentUserId()
            _galleryItemList.value = if (currentUserId != null) {
                galleryRepository.getLikedGalleryItems(currentUserId)
            } else {
                emptyList()
            }
            Log.d("GalleryViewModel", "Gallery items loaded: ${_galleryItemList.value.size}")
        }
    }
}
