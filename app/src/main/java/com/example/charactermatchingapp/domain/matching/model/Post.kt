package com.example.charactermatchingapp.domain.matching.model

import android.net.Uri

data class Post(
    val id: String = "",
    val userName: String = "",
    val userIconResId: Uri = Uri.EMPTY,
    val characterName: String = "",
    val postImageResId: Uri = Uri.EMPTY,
    val posttags: List<String> = emptyList()
) {
    // postTextは、リストの各要素の先頭に#を付けて結合する形に変更
    val postText: String
    get() = posttags.joinToString(separator = "　") { "#$it" }
}
