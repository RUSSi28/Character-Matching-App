package com.example.charactermatchingapp.data.post.repository

import android.util.Log
import com.example.charactermatchingapp.data.post.dto.PostDto
import com.example.charactermatchingapp.domain.post.model.PostInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class PostRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : PostRepository {

    override suspend fun savePost(postInfo: PostInfo): Result<Unit> {
        return try {
            var imageUrl: String? = null

            // 1. 画像のアップロード (CharacterPostActivity.txtのuploadCharacterDataの一部)
            if (postInfo.imageUri != null) {
                val fileRef = storage.reference.child("images/${System.currentTimeMillis()}.jpg")
                fileRef.putFile(postInfo.imageUri).await() // アップロード完了を待つ
                imageUrl = fileRef.downloadUrl.await().toString() // ダウンロードURLを取得
                Log.d("PostRepoImpl", "Image uploaded successfully: $imageUrl")
            }

            // 2. PostInfoをPostDtoに変換し、Firestore保存用のMapを作成
            val postDto = PostDto.fromDomain(postInfo, imageUrl)
            val postMap = postDto.toMap()

            // 3. Firestoreにデータを保存 (CharacterPostActivity.txtのsaveCharacterDataの一部)
            firestore.collection("artworks").add(postMap).await() // "Post"コレクション名を"posts"に変更
            Log.d("PostRepoImpl", "Post saved successfully: ${postInfo.name}")

            Result.success(Unit) // 成功
        } catch (e: Exception) {
            Log.e("PostRepoImpl", "Error saving post", e)
            Result.failure(e) // 失敗
        }
    }
}
