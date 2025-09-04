package com.example.charactermatchingapp.presentation.sns


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.charactermatchingapp.domain.matching.model.Post
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import com.example.charactermatchingapp.data.PostRepository

// Hiltのアノテーションを削除
class SnsViewModel : ViewModel() {

    // ViewModelの内部で、Repositoryに必要なインスタンスを直接生成します。
    private val firestore = Firebase.firestore
    private val postRepository = PostRepository(firestore)

    // postPagingFlowの定義は変更ありません。
    val postPagingFlow: Flow<PagingData<Post>> =
        postRepository.getPostsPager().cachedIn(viewModelScope)
}