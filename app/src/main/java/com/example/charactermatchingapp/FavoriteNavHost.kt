package com.example.charactermatchingapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.charactermatchingapp.presentation.sns.AccountScreen
import com.example.charactermatchingapp.presentation.sns.FavoritesScreen
import com.example.charactermatchingapp.presentation.sns.TimelineScreen

@Composable
fun FavoriteScreenNavHost(
    accountId: String,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.FAVORITE_ROUTE
    ) {
        // アカウント画面（投稿者用）
        composable(AppDestinations.FAVORITE_ROUTE) {
            FavoritesScreen(
                accountId = accountId,
                onPostClick = {
                    navController.navigate(AppDestinations.ACCOUNT_ROUTE)
                }
            )
        }
        // アカウント画面
        composable(AppDestinations.ACCOUNT_ROUTE) {
            AccountScreen(
                accountId = accountId,
                onClick = {
                    // ★★★ 戻るボタンが押されたら、前の画面（お気に入り画面）に戻る ★★★
                    navController.popBackStack()
                },
                onPostClick = {post ->
                    // ★★★ クリックされた投稿のIDとアカウントIDを付けてタイムライン画面へ遷移 ★★★
                    navController.navigate("timeline/$accountId/${post.id}")
                }
            )
        }
        // タイムライン画面
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
    }
}