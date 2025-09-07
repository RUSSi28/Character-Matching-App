package com.example.charactermatchingapp.data.recommendation.repository

import com.example.charactermatchingapp.domain.recommendation.model.Recommendation
import com.example.charactermatchingapp.domain.recommendation.repository.RecommendationRepository
import com.example.charactermatchingapp.domain.recommendation.repository.RecommendationResult
import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Tool
import kotlinx.serialization.json.Json

class RecommendationRepositoryImpl : RecommendationRepository {

    // ユーザーの指示とエラー内容に基づき、初期化方法を修正
    private val model: GenerativeModel = Firebase.ai.generativeModel(
        modelName = "gemini-2.5-flash", // ご要望のモデル名に変更
        tools = listOf(Tool.googleSearch())
    )

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    override suspend fun getRecommendations(tags: List<String>): Result<RecommendationResult> {
        if (tags.isEmpty()) {
            return Result.success(RecommendationResult(emptyList(), null))
        }

        // ユーザー提案のマーカー方式を採用
        val prompt = """
あなたは漫画・アニメの推薦キュレーターです。
Google検索を利用して、ユーザーのタグに基づき 5 件おすすめしてください。
出力は必ず以下の形式に従ってください。
JSON_START
[
  { "title": string, "type": "manga"|"anime", "reason": string, "matchedTags": string[], "sourceUrls": string[] }
]
JSON_END
ユーザーのタグ: ${tags.joinToString()}
""".trimIndent()

        return try {
            val response = model.generateContent(prompt)

            val rawText = response.text ?: ""
            // マーカーを使ってJSON部分を安全に抽出する
            val jsonText = rawText.substringAfter("JSON_START", "").substringBeforeLast("JSON_END", "")

            if (jsonText.isBlank()) {
                return Result.failure(Exception("モデルの応答からJSONコンテンツが見つかりませんでした。"))
            }

            val recommendations = json.decodeFromString<List<Recommendation>>(jsonText.trim())

            // ユーザーの調査結果に基づき、正しいパスから検索サジェストを取得する
            val searchSuggestions = response.candidates.firstOrNull()?.groundingMetadata?.searchEntryPoint?.renderedContent

            val result = RecommendationResult(
                recommendations = recommendations,
                searchSuggestions = searchSuggestions
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}