package com.example.charactermatchingapp

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object CharacterPostActivity {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private const val TAG = "CharacterRepository"

    // Firestore に投稿データを保存
    fun saveCharacterData(
        name: String,
        tags: List<String>,
        description: String,
        imageUrl: String?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val character = hashMapOf(
            "name" to name,
            "tags" to tags,
            "description" to description,
            "imageUrl" to imageUrl
        )

        db.collection("Post")
            .add(character)
            .addOnSuccessListener {
                Log.d(TAG, "Character saved successfully: $name")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error saving character", e)
                onFailure(e)
            }
    }

    // Storageに画像アップロード → URL取得 → Firestoreに保存
    fun uploadCharacterData(
        name: String,
        tags: List<String>,
        description: String,
        imageUri: Uri,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val fileRef = storage.reference.child("images/${System.currentTimeMillis()}.jpg")

        fileRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // アップロード成功 → URL 取得
                taskSnapshot.storage.downloadUrl
                    .addOnSuccessListener { downloadUrl ->
                        // Firestore に保存
                        saveCharacterData(name, tags, description, downloadUrl.toString(), onSuccess, onFailure)
                        Log.d(TAG, "Image uploaded successfully: $downloadUrl")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error getting download URL", e)
                        onFailure(e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error uploading image", e)
                onFailure(e)
            }
    }
}
