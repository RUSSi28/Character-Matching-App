package com.example.charactermatchingapp.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.charactermatchingapp.domain.matching.model.Profile

/**
 * 指定されたaccountIdのプロフィール情報をFirestoreから取得する
 *
 * @param accountId 取得したいユーザーのドキュメントID
 * @return 取得したプロフィール情報。見つからない場合やエラーの場合はnullを返す。
 */
suspend fun getProfile(accountId: String): Profile? {
    // 1. Firestoreのインスタンスを取得
    val db = FirebaseFirestore.getInstance()

    return try {
        // 2. "accounts"コレクションから指定されたIDのドキュメントを取得
        val document = db.collection("accounts").document(accountId).get().await()

        // 3. ドキュメントが存在するか確認
        if (document.exists()) {
            // 4. Firestoreのフィールド名を使って各データを取得
            //    ?: "" は、もしデータがnullの場合に空文字を代入する安全対策
            val displayName = document.getString("displayName") ?: ""
            val headerImageUrl = document.getString("headerImageUrl") ?: ""
            val iconImageUrl = document.getString("iconImageUrl") ?: ""
            val bio = document.getString("bio") ?: ""

            // 5. 取得したデータを使ってProfileオブジェクトを生成して返す
            Profile(
                accountName = displayName,
                headerImageResId = headerImageUrl,
                iconImageResId = iconImageUrl,
                profileText = bio
            )
        } else {
            // ドキュメントが見つからなかった場合
            null
        }
    } catch (e: Exception) {
        // ネットワークエラーなどで取得に失敗した場合
        println("Error getting profile: ${e.message}")
        null
    }
}