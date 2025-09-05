package com.example.charactermatchingapp.data.auth.service

import com.google.firebase.auth.FirebaseAuth
import com.example.charactermatchingapp.domain.auth.service.CurrentUserProvider

class CurrentUserProviderImpl(private val auth: FirebaseAuth) : CurrentUserProvider {
    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
