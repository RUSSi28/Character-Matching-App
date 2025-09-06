package com.example.charactermatchingapp.di

import org.koin.core.annotation.KoinInternalApi
import org.koin.test.verify.verify

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.check.checkModules

class CheckModuleTest : KoinTest{
    val testModule = module {
        single<FirebaseApp> { mockk(relaxed = true) }
        single<FirebaseAuth> { mockk(relaxed = true) }
        single<FirebaseFirestore> { mockk(relaxed = true) }
    }
    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(appModule, testModule)
    }
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    @Test
    fun `check all Koin modules`() {
        koinTestRule.koin.checkModules ()
    }
}

// テスト時に使用するDispatcherをMainDispatcherに設定するRule
class MainDispatcherRule @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    // テストが開始される前にMainDispatcherをテスト用のものに差し替える
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    // テストが終了した後にMainDispatcherを元に戻す
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}