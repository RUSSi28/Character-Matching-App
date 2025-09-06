package com.example.charactermatchingapp.domain.gallery.model

data class PagedResult<T>(
    val items: List<T>,
    val hasMoreItems: Boolean
)
