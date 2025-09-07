package com.example.charactermatchingapp.domain.user.repository

import com.example.charactermatchingapp.domain.matching.model.Profile

interface UserRepository {
    suspend fun getLikedTags(userId: String): Result<List<String>>
    suspend fun getUserProfile(userId: String): Result<Profile>
    suspend fun updateUserProfile(userId: String, profile: Profile): Result<Unit>
}
