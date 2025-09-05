package com.example.charactermatchingapp

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

// グローバルな依存関係を提供するインターフェース
interface AppContainer {
    val firebaseAuth: FirebaseAuth
    val firebaseFirestore: FirebaseFirestore
}

// AppContainerの実装
class AppContainerImpl : AppContainer {
    override val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    override val firebaseFirestore: FirebaseFirestore by lazy { Firebase.firestore }
}

class MyApplication : Application() {
    // アプリケーション全体でアクセス可能なAppContainerのインスタンス
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainerImpl()
    }
}