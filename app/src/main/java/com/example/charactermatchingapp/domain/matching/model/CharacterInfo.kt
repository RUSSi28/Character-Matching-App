package com.example.charactermatchingapp.domain.matching.model

import com.google.firebase.Timestamp

data class CharacterInfo(
    val id: String,
    val name: String,
    val image: String,
    val description: String,
    val tags: List<String>,
    val userName: String,
    val likes: Int,
    val postedAt: Timestamp
)