package com.example.charactermatchingapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.charactermatchingapp.domain.matching.model.Post
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

class PostPagingSource(
    private val query: Query
) : PagingSource<DocumentSnapshot, Post>() {

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, Post> {
        return try {
            var currentQuery = query.limit(10)

            params.key?.let { lastVisibleDocument ->
                currentQuery = currentQuery.startAfter(lastVisibleDocument)
            }

            val documents = currentQuery.get().await()

            val nextKey = documents.documents.lastOrNull()

            val data = documents.map { document ->
                document.toObject(Post::class.java)
            }

            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Post>): DocumentSnapshot? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
                ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }
}