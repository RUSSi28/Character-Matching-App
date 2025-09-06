package com.example.charactermatchingapp.domain.post.model

import android.net.Uri
import com.google.firebase.Timestamp

data class PostInfo(
    val name: String,
    val tags: List<String>,
    val description: String,
    val imageUri: Uri?,
    val postedAt: Timestamp = Timestamp.now()
)