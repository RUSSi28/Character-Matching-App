package com.example.charactermatchingapp.domain.matching.model

import android.net.Uri

// --- データクラスの定義 ---
data class Profile(
    val accountName: String = "",
    val headerImageResId: Uri = Uri.EMPTY,
    val iconImageResId: Uri = Uri.EMPTY,
    val profileText: String = ""
)

