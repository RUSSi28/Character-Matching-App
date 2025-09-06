package com.example.charactermatchingapp.presentation.sns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.charactermatchingapp.data.PostRepository
import com.example.charactermatchingapp.data.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore

class AccountViewModelFactory(
    private val accountId: String,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            val profileRepository = ProfileRepository()
            val postRepository = PostRepository()

            @Suppress("UNCHECKED_CAST")
            return AccountViewModel(accountId, profileRepository, postRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}