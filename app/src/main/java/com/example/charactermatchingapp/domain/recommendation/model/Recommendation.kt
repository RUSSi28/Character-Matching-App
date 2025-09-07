package com.example.charactermatchingapp.domain.recommendation.model

import kotlinx.serialization.Serializable

@Serializable
data class Recommendation(
    val title: String,
    val type: String, // "manga" or "anime"
    val reason: String,
    val matchedTags: List<String>,
    val sourceUrls: List<String>
)
