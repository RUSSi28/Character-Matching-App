package com.example.charactermatchingapp.domain.matching.repository

import com.example.charactermatchingapp.domain.matching.model.CharacterInfo

interface CharacterMatchingRepository {
    suspend fun getMatchingCharactersInfo(): List<CharacterInfo>
    suspend fun getMatchingCharacterInfo(documentId: String): CharacterInfo?
    suspend fun likeCharacterInfo(characterInfo: CharacterInfo)
    fun bookmarkCharacterInfo(characterInfo: CharacterInfo)
    fun dislikeCharacterInfo(characterInfo: CharacterInfo)
    suspend fun onCardLiked(userId: String, likedArtworkTags: List<String>): Result<Unit>
}