package com.example.charactermatchingapp

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage

// グローバルな依存関係を提供するインターフェース
interface AppContainer {
    val firebaseAuth: FirebaseAuth
    val firebaseFirestore: FirebaseFirestore
    val firebaseStorage: FirebaseStorage // 追加
}

// AppContainerの実装
class AppContainerImpl : AppContainer {
    override val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    override val firebaseFirestore: FirebaseFirestore by lazy { Firebase.firestore }
    override val firebaseStorage: FirebaseStorage by lazy { Firebase.storage } // 追加
}

class MyApplication : Application() {
    // アプリケーション全体でアクセス可能なAppContainerのインスタンス
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainerImpl()
    }
}