package com.example.charactermatchingapp.di

import com.example.charactermatchingapp.data.auth.repository.AuthRepositoryImpl
import com.example.charactermatchingapp.data.auth.service.CurrentUserProviderImpl
import com.example.charactermatchingapp.data.gallery.repository.GalleryRepositoryImpl
import com.example.charactermatchingapp.domain.auth.repository.AuthRepository
import com.example.charactermatchingapp.domain.auth.service.CurrentUserProvider
import com.example.charactermatchingapp.domain.gallery.repository.GalleryRepository
import com.example.charactermatchingapp.presentation.auth.AuthViewModel
import com.example.charactermatchingapp.presentation.gallery.GalleryViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    // Firebase関連
    single { FirebaseApp.initializeApp(androidContext()) }
    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<FirebaseFirestore> { FirebaseFirestore.getInstance() }
    single<CurrentUserProvider> { CurrentUserProviderImpl(get()) }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    viewModelOf(::AuthViewModel)

    single<GalleryRepository> { GalleryRepositoryImpl(get()) }
    viewModelOf(::GalleryViewModel)
}