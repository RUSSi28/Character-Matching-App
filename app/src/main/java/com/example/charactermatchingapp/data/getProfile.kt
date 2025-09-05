package com.example.charactermatchingapp.data // or repository package

import com.example.charactermatchingapp.domain.matching.model.Profile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getProfile(accountId: String): Profile? {
        return try {
            val document = db.collection("accounts").document(accountId).get().await()
            if (document.exists()) {
                // ここでFirestoreのデータをProfileオブジェクトに変換する
                Profile(
                    accountName = document.getString("displayName") ?: "",
                    headerImageResId = document.getString("headerImageUrl") ?: "",
                    iconImageResId = document.getString("iconImageUrl") ?: "",
                    profileText = document.getString("bio") ?: ""
                )
            } else {
                null
            }
        } catch (e: Exception) {
            // エラー処理
            null
        }
    }
}