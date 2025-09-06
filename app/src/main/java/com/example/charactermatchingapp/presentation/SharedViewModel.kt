package com.example.charactermatchingapp.presentation

import androidx.lifecycle.ViewModel
import com.example.charactermatchingapp.domain.auth.service.CurrentUserProvider

class SharedViewModel(
    val currentUserProvider: CurrentUserProvider
) : ViewModel()