package com.example.charactermatchingapp.data.post.repository

import com.example.charactermatchingapp.domain.post.model.PostInfo

interface PostRepository {
    // 投稿を保存する関数。成功/失敗をResultで返す
    suspend fun savePost(postInfo: PostInfo): Result<Unit> // Unitは成功を示す
}
