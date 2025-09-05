package com.example.charactermatchingapp.domain.auth.service

interface CurrentUserProvider {
    fun getCurrentUserId(): String?
}
