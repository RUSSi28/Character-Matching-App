package com.example.charactermatchingapp.domain.matching.model

import android.net.Uri

// --- データクラスの定義 ---
data class Profile(
    val accountName: String,
    val headerImageResId: String,
    val iconImageResId: String,
    val profileText: String
)

