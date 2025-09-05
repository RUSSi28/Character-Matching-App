package com.example.charactermatchingapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.charactermatchingapp.presentation.sns.AccountSettingsScreen
import com.example.charactermatchingapp.presentation.sns.PostCreationScreen
import com.example.charactermatchingapp.presentation.sns.PosterViewAccountScreen
import com.example.charactermatchingapp.presentation.sns.TimelineScreen
import androidx.navigation.navArgument
import androidx.navigation.NavType

@Composable
fun PosterViewScreenNavHost(
    accountId: String,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.POSTER_VIEW_ACCOUNT_ROUTE
    ) {
        // アカウント画面（投稿者用）
        composable(AppDestinations.POSTER_VIEW_ACCOUNT_ROUTE) {
            PosterViewAccountScreen(
                accountId = accountId,
                onPostClick = {
                        post ->
                    // ★★★ クリックされた投稿のIDとアカウントIDを付けてタイムライン画面へ遷移 ★★★
                    navController.navigate("timeline/$accountId/${post.id}")
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
                accountId = accountId,
                onClick = {
                    // 戻る・更新ボタンが押されたら前の画面に戻る
                    navController.popBackStack()
                }
            )
        }
        // --- タイムライン画面の定義 ---
        composable(
            route =AppDestinations.TIMELINE_ROUTE,
            arguments = listOf(
                navArgument(AppDestinations.TIMELINE_ARG_ACCOUNT_ID) { type = NavType.StringType },
                navArgument(AppDestinations.TIMELINE_ARG_POST_ID) { type = NavType.StringType }
            )
        ) {backStackEntry ->
            // ★★★ ナビゲーション引数を取得 ★★★
            val accountId = backStackEntry.arguments?.getString(AppDestinations.TIMELINE_ARG_ACCOUNT_ID)
            val initialPostId = backStackEntry.arguments?.getString(AppDestinations.TIMELINE_ARG_POST_ID)
            if (accountId != null && initialPostId != null) {
                TimelineScreen(
                    accountId = accountId,
                    initialPostId = initialPostId, // ★★★ 取得したIDを渡す ★★★
                    onClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
        // アカウント設定画面
        composable(AppDestinations.POST_EDIT_ROUTE) {
            PostCreationScreen(
                onClick = {
                    // 戻る・更新ボタンが押されたら前の画面に戻る
                    navController.popBackStack()
                }
            )
        }
    }
}