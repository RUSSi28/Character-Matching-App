package com.example.charactermatchingapp.domain.user.repository

interface UserRepository {
    suspend fun getLikedTags(userId: String): Result<List<String>>
}
