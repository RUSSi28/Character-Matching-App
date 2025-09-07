package com.example.charactermatchingapp.di

import com.example.charactermatchingapp.data.auth.repository.AuthRepositoryImpl
import com.example.charactermatchingapp.data.auth.service.CurrentUserProviderImpl
import com.example.charactermatchingapp.data.gallery.repository.GalleryRepositoryImpl
import com.example.charactermatchingapp.data.matching.repository.CharacterMatchingRepositoryImpl
import com.example.charactermatchingapp.data.post.repository.PostRepository
import com.example.charactermatchingapp.data.post.repository.PostRepositoryImpl
import com.example.charactermatchingapp.data.recommendation.repository.RecommendationRepositoryImpl
import com.example.charactermatchingapp.data.user.repository.UserRepositoryImpl
import com.example.charactermatchingapp.domain.auth.repository.AuthRepository
import com.example.charactermatchingapp.domain.auth.service.CurrentUserProvider
import com.example.charactermatchingapp.domain.gallery.repository.GalleryRepository
import com.example.charactermatchingapp.domain.matching.repository.CharacterMatchingRepository
import com.example.charactermatchingapp.domain.recommendation.repository.RecommendationRepository
import com.example.charactermatchingapp.domain.user.repository.UserRepository
import com.example.charactermatchingapp.presentation.auth.AuthViewModel
import com.example.charactermatchingapp.presentation.gallery.GalleryViewModel
import com.example.charactermatchingapp.presentation.post.PostViewModel
import com.example.charactermatchingapp.presentation.recommendation.RecommendationViewModel
import com.example.charactermatchingapp.presentation.SharedViewModel
import com.example.charactermatchingapp.presentation.matching.CharacterMatchingViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    // Firebase関連
    single { FirebaseApp.initializeApp(androidContext()) }
    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<FirebaseFirestore> { FirebaseFirestore.getInstance() }
    single<FirebaseStorage> { FirebaseStorage.getInstance() }
    single<CurrentUserProvider> { CurrentUserProviderImpl(get()) }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    viewModelOf(::AuthViewModel)

    single<GalleryRepository> { GalleryRepositoryImpl(get()) }
    viewModelOf(::GalleryViewModel)

    single<PostRepository> { PostRepositoryImpl(get(),get()) }
    viewModelOf(::PostViewModel)

    // Recommendation機能
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<RecommendationRepository> { RecommendationRepositoryImpl() }
    viewModelOf(::RecommendationViewModel)

    single<CharacterMatchingRepository> { CharacterMatchingRepositoryImpl(get(),get()) }
    viewModelOf(::CharacterMatchingViewModel)

    viewModelOf(::SharedViewModel)
}