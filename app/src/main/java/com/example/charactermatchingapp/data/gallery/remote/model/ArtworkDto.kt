package com.example.charactermatchingapp.data.gallery.remote.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class ArtworkDto(
    @DocumentId val artworkId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val characterName: String = "",
    val characterDescription: String = "",
    val imageUrl: String = "",
    val thumbUrl: String? = null,
    val tags: List<String> = emptyList(),
    val likeCount: Int = 0,
    val postedAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
    val visibility: String = "public"
)
