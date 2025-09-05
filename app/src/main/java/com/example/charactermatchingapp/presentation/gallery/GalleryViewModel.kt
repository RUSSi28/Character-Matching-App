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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val PAGE_SIZE = 10L // Define page size

class GalleryViewModel(
    private val galleryRepository: GalleryRepository,
    private val currentUserProvider: CurrentUserProvider
) : ViewModel() {

    private val _galleryItemList = MutableStateFlow<List<GalleryItem>>(emptyList())
    val galleryItemList: StateFlow<List<GalleryItem>> = _galleryItemList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasMoreItems = MutableStateFlow(true)
    val hasMoreItems: StateFlow<Boolean> = _hasMoreItems.asStateFlow()

    private val _currentPage = MutableStateFlow(1) // Start from page 1
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    // Stores the artworkId of the last item of the previous page for each page
    private val _pageKeys = mutableListOf<String?>(null) // pageKeys[0] is null for page 1

    init {
        loadGalleryItems(1)
    }

    fun loadGalleryItems(page: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val currentUserId = currentUserProvider.getCurrentUserId()

            if (currentUserId == null) {
                _galleryItemList.value = emptyList()
                _isLoading.value = false
                _hasMoreItems.value = false
                return@launch
            }

            val startAfterId = _pageKeys.getOrNull(page - 1) // Get key for the requested page

            try {
                val fetchedItems = galleryRepository.getLikedGalleryItems(
                    userId = currentUserId,
                    limit = PAGE_SIZE + 1, // Fetch one more item to check for next page
                    startAfterArtworkId = startAfterId
                )

                val newItemsForDisplay = fetchedItems.take(PAGE_SIZE.toInt()) // Items to display on current page

                _galleryItemList.value = newItemsForDisplay // Replace list with new page items

                // Update pageKeys for next page if full page is loaded and not already stored
                // Store the artworkId of the last item of the current page
                if (newItemsForDisplay.size.toLong() == PAGE_SIZE && _pageKeys.size <= page) {
                    _pageKeys.add(newItemsForDisplay.lastOrNull()?.artworkId)
                }

                _hasMoreItems.value = fetchedItems.size.toLong() > PAGE_SIZE

                _currentPage.value = page

            } catch (e: Exception) {
                Log.e("GalleryViewModel", "Error loading gallery items for page $page", e)
                // Handle error state in UI if needed
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadNextPage() {
        if (!_isLoading.value && _hasMoreItems.value) {
            loadGalleryItems(_currentPage.value + 1)
        }
    }

    fun loadPreviousPage() {
        if (!_isLoading.value && _currentPage.value > 1) {
            loadGalleryItems(_currentPage.value - 1)
        }
    }
}
