package com.example.charactermatchingapp.domain.matching.model

data class CharacterInfo(
    val id: String,
    val name: String,
    val image: String,
    val description: String,
    val tags: List<String>,
    val contributor: String,
)