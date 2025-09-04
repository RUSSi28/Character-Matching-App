package com.example.charactermatchingapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.charactermatchingapp.domain.matching.model.Profile
import com.example.charactermatchingapp.presentation.sns.AccountScreen
import com.example.charactermatchingapp.presentation.sns.FavoritesScreen
import com.example.charactermatchingapp.presentation.sns.SnsViewModel
import com.example.charactermatchingapp.presentation.sns.TimelineScreen

@Composable
fun FavoriteScreenNavHost(
    navController: NavHostController = rememberNavController(),
    snsViewModel: SnsViewModel = viewModel() // ViewModelを共有
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.FAVORITE_ROUTE
    ) {
        // アカウント画面（投稿者用）
        composable(AppDestinations.FAVORITE_ROUTE) {
            FavoritesScreen(
                viewModel = snsViewModel,
                onPostClick = {
                    navController.navigate(AppDestinations.ACCOUNT_ROUTE)
                }
            )
        }
        // アカウント画面
        composable(AppDestinations.ACCOUNT_ROUTE) {
            AccountScreen(
                profile = Profile(
                    accountName = "User Name",
                    headerImageResId = "",
                    iconImageResId = "",
                    profileText = "ここにプロフィール文が入ります。この文章はサンプルです。"
                ),
                viewModel = snsViewModel,
                onClick = {
                    // ★★★ 戻るボタンが押されたら、前の画面（お気に入り画面）に戻る ★★★
                    navController.navigate(AppDestinations.FAVORITE_ROUTE)
                },
                onPostClick = {
                    navController.navigate(AppDestinations.TIMELINE_ROUTE)
                }
            )
        }
        // タイムライン画面
        composable(AppDestinations.TIMELINE_ROUTE) {
            TimelineScreen(
                onClick = {
                    // ★★★ 戻るボタンが押されたら、前の画面（アカウント画面）に戻る ★★★
                    navController.navigate(AppDestinations.ACCOUNT_ROUTE)
                }
            )
        }
    }
}