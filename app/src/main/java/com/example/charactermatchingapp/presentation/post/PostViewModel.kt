package com.example.charactermatchingapp.presentation.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charactermatchingapp.data.post.repository.PostRepository
import com.example.charactermatchingapp.domain.post.model.PostInfo
import kotlinx.coroutines.launch

class PostViewModel(
    private val postRepository: PostRepository
) : ViewModel() {

    fun savePost(postInfo: PostInfo) {
        viewModelScope.launch {
            postRepository.savePost(postInfo)
        }
    }
}