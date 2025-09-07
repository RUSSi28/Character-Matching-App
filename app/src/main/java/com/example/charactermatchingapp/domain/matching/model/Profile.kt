package com.example.charactermatchingapp.domain.matching.model

import com.google.firebase.Timestamp

data class Profile(
    val email: String = "",
    val displayName: String = "",
    val iconImageUrl: String? = null,
    val headerImageUrl: String? = null,
    val bio: String? = null,
    val likedCount: Int = 0,
    val postsCount: Int = 0,
    val createdAt: Timestamp = Timestamp.now(),
    val likesTags: List<String> = emptyList()
)

