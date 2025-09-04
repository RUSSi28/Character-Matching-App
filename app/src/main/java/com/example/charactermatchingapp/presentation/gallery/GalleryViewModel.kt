package com.example.charactermatchingapp.presentation.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charactermatchingapp.domain.gallery.model.GalleryItem
import com.example.charactermatchingapp.domain.gallery.usecase.GetGalleryItemsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GalleryViewModel(private val getGalleryItemsUseCase: GetGalleryItemsUseCase) : ViewModel() {

    private val _galleryItemList = MutableStateFlow<List<GalleryItem>>(emptyList())
    val galleryItemList: StateFlow<List<GalleryItem>> = _galleryItemList.asStateFlow()

    init {
        loadGalleryItems()
    }

    private fun loadGalleryItems() {
        viewModelScope.launch {
            _galleryItemList.value = getGalleryItemsUseCase()
        }
    }
}
