package com.example.charactermatchingapp.domain.recommendation.repository

import com.example.charactermatchingapp.domain.recommendation.model.Recommendation

data class RecommendationResult(
    val recommendations: List<Recommendation>,
    val searchSuggestions: String? // WebViewで表示するHTMLコンテンツ
)

interface RecommendationRepository {
    suspend fun getRecommendations(tags: List<String>): Result<RecommendationResult>
}
