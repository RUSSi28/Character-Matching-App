package com.example.charactermatchingapp.data.post.dto

import com.example.charactermatchingapp.domain.post.model.PostInfo
import com.google.firebase.Timestamp // FirebaseのTimestampを使う場合

data class PostDto(
    val name: String = "",
    val tags: List<String> = emptyList(),
    val description: String = "",
    val imageUrl: String? = null, // Firebase Storageにアップロード後のURL
    val postedAt: Timestamp = Timestamp.now()
) {
    // PostInfoからPostDtoへの変換関数
    companion object {
        fun fromDomain(postInfo: PostInfo, imageUrl: String?): PostDto {
            return PostDto(
                name = postInfo.name,
                tags = postInfo.tags,
                description = postInfo.description,
                imageUrl = imageUrl, // アップロード後のURLを受け取る
                postedAt = postInfo.postedAt
            )
        }
    }

    // PostDtoからMapへの変換関数 (Firestore保存用)
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "tags" to tags,
            "description" to description,
            "imageUrl" to imageUrl,
            "postedAt" to postedAt
        )
    }
}
