package com.example.charactermatchingapp.data.matching.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class CharacterData(
    @DocumentId val id: String = "",
    @get:PropertyName("characterName")
    val name: String = "",
    @get:PropertyName("imageUrl")
    val imageUrl: String? = null,
    @get:PropertyName("characterDescription")
    val description: String = "",
    @get:PropertyName("tags")
    val tags: List<String> = emptyList(),
    @get:PropertyName("authorName")
    val userName: String = "",
    @get:PropertyName("likes")
    val likes: Int = 0,
    @get:PropertyName("createdAt")
    val postedAt: Timestamp = Timestamp.now()
)
