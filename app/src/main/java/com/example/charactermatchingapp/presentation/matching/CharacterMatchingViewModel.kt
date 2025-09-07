package com.example.charactermatchingapp.presentation.matching

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charactermatchingapp.domain.matching.model.CharacterInfo
import com.example.charactermatchingapp.domain.matching.repository.CharacterMatchingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterMatchingViewModel(
    private val characterMatchingRepository: CharacterMatchingRepository
) : ViewModel() {
    private val _matchingCharacters = MutableStateFlow<List<CharacterInfo>>(emptyList())
    val matchingCharacters: StateFlow<List<CharacterInfo>> = _matchingCharacters.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadMatchingCharacters()
    }

    fun loadMatchingCharacters() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _matchingCharacters.value = characterMatchingRepository.getMatchingCharactersInfo()
            } catch (e: Exception) {
                Log.e("CharacterMatchingViewModel", "Error loading matching characters", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun likeCharacterInfo(characterInfo: CharacterInfo) {
        viewModelScope.launch {
            characterMatchingRepository.likeCharacterInfo(characterInfo)
            removeLastItem()
        }
    }

    fun removeLastItem() {
        val updatedList = _matchingCharacters.value.dropLast(1)
        _matchingCharacters.value = updatedList
    }
}