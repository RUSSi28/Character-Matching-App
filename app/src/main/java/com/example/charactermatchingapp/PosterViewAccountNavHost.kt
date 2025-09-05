package com.example.charactermatchingapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.charactermatchingapp.domain.matching.model.Profile
import com.example.charactermatchingapp.presentation.sns.AccountSettingsScreen
import com.example.charactermatchingapp.presentation.sns.PosterViewAccountScreen
import com.example.charactermatchingapp.presentation.sns.SnsViewModel
import com.example.charactermatchingapp.presentation.sns.TimelineScreen
import com.example.charactermatchingapp.presentation.sns.PostCreationScreen

@Composable
fun PosterViewScreenNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.POSTER_VIEW_ACCOUNT_ROUTE
    ) {
        // アカウント画面（投稿者用）
        composable(AppDestinations.POSTER_VIEW_ACCOUNT_ROUTE) {
            PosterViewAccountScreen(
                //ホントはDBから取ってきたのが入る
                accountId = "NjMe4XK8J4rm9f4ogvEj",
                onPostClick = {
                    navController.navigate(AppDestinations.TIMELINE_ROUTE)
                },
                onEditClick = {
                    // 編集ボタンが押されたら設定画面に遷移
                    navController.navigate(AppDestinations.ACCOUNT_SETTINGS_ROUTE)
                },
                onPostFabClick = {
                    navController.navigate(AppDestinations.POST_EDIT_ROUTE)
                }
            )
        }

        // アカウント設定画面
        composable(AppDestinations.ACCOUNT_SETTINGS_ROUTE) {
            AccountSettingsScreen(
                accountId = "NjMe4XK8J4rm9f4ogvEj",
                onClick = {
                    // 戻る・更新ボタンが押されたら前の画面に戻る
                    navController.navigate(AppDestinations.POSTER_VIEW_ACCOUNT_ROUTE)
                }
            )
        }
        // --- タイムライン画面の定義 ---
        composable(AppDestinations.TIMELINE_ROUTE) {
            TimelineScreen(
                accountId = "NjMe4XK8J4rm9f4ogvEj",
                onClick = {
                    // ★★★ 戻るボタンが押されたら、前の画面（アカウント画面）に戻る ★★★
                    navController.navigate(AppDestinations.POSTER_VIEW_ACCOUNT_ROUTE)
                }
            )
        }
        // アカウント設定画面
        composable(AppDestinations.POST_EDIT_ROUTE) {
            PostCreationScreen(
                onClick = {
                    // 戻る・更新ボタンが押されたら前の画面に戻る
                    navController.navigate(AppDestinations.POSTER_VIEW_ACCOUNT_ROUTE)
                }
            )
        }
    }
}