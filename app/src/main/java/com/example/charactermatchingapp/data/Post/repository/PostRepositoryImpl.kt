package com.example.charactermatchingapp.data.post.repository

import android.util.Log
import com.example.charactermatchingapp.data.post.dto.PostDto
import com.example.charactermatchingapp.domain.post.model.PostInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class PostRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : PostRepository {

    override suspend fun savePost(postInfo: PostInfo): Result<Unit> {
        return try {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser == null) {
                return Result.failure(Exception("User not logged in"))
            }
            val uid = currentUser.uid

            // Firestoreの'accounts'コレクションからユーザー情報を取得
            val userDocument = firestore.collection("accounts").document(uid).get().await()
            val authorName = userDocument.getString("displayName") ?: ""

            var imageUrl: String? = null

            // 1. 画像のアップロード
            if (postInfo.imageUri != null) {
                val fileRef = storage.reference.child("images/${System.currentTimeMillis()}.jpg")
                fileRef.putFile(postInfo.imageUri).await()
                imageUrl = fileRef.downloadUrl.await().toString()
                Log.d("PostRepoImpl", "Image uploaded successfully: $imageUrl")
            }

            // 2. PostInfoをPostDtoに変換し、Firestore保存用のMapを作成
            val postDto = PostDto.fromDomain(postInfo, imageUrl)
            val postMap = postDto.toMap().toMutableMap()

            // 3. 投稿者情報をMapに追加
            postMap["authorId"] = uid
            postMap["authorName"] = authorName

            // 4. Firestoreにデータを保存
            firestore.collection("artworks").add(postMap).await()
            Log.d("PostRepoImpl", "Post saved successfully: ${postInfo.name}")

            Result.success(Unit) // 成功
        } catch (e: Exception) {
            Log.e("PostRepoImpl", "Error saving post", e)
            Result.failure(e) // 失敗
        }
    }
}
