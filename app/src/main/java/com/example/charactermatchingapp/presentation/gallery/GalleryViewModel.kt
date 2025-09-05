package com.example.charactermatchingapp.presentation.gallery

import android.util.Log // Import Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charactermatchingapp.domain.gallery.model.GalleryItem
import com.example.charactermatchingapp.domain.gallery.usecase.GetGalleryItemsUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val getGalleryItemsUseCase: GetGalleryItemsUseCase,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _galleryItemList = MutableStateFlow<List<GalleryItem>>(emptyList())
    val galleryItemList: StateFlow<List<GalleryItem>> = _galleryItemList.asStateFlow()

    init {
        loadGalleryItems()
    }

    private fun loadGalleryItems() {
        viewModelScope.launch {
            val currentUserId = auth.currentUser?.uid
            Log.d("GalleryViewModel", "Current User ID: $currentUserId") // Log currentUserId
            if (currentUserId != null) {
                _galleryItemList.value = getGalleryItemsUseCase(currentUserId)
                Log.d("GalleryViewModel", "Gallery items loaded: ${_galleryItemList.value.size}") // Log size
            } else {
                // User not logged in, display empty list or handle error
                _galleryItemList.value = emptyList()
                Log.d("GalleryViewModel", "User not logged in, gallery items set to empty.")
            }
        }
    }
}
