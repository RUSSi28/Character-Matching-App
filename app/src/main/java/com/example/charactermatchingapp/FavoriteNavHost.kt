package com.example.charactermatchingapp

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.charactermatchingapp.presentation.gallery.GalleryApp
import com.example.charactermatchingapp.presentation.gallery.GalleryViewModel
import com.example.charactermatchingapp.presentation.sns.AccountScreen
import com.example.charactermatchingapp.presentation.sns.TimelineScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteScreenNavHost(
    navController: NavHostController = rememberNavController(),
    windowInsets: WindowInsets = WindowInsets.safeDrawing,
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.GALLERY_ROUTE
    ) {
        composable(AppDestinations.GALLERY_ROUTE) {
            val galleryViewModel: GalleryViewModel = koinViewModel()
            GalleryApp(
                windowInsets = windowInsets,
                galleryViewModel = galleryViewModel,
                onItemClick = { authorId ->
                    val route = AppDestinations.ACCOUNT_ROUTE.replace("{${AppDestinations.ACCOUNT_ARG_AUTHOR_ID}}", authorId)
                    navController.navigate(route)
                },
            )
        }
        // アカウント画面
        composable(
            route = AppDestinations.ACCOUNT_ROUTE,
            arguments = listOf(navArgument(AppDestinations.ACCOUNT_ARG_AUTHOR_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val authorId = backStackEntry.arguments?.getString(AppDestinations.ACCOUNT_ARG_AUTHOR_ID)
            if (authorId != null) {
                AccountScreen(
                    accountId = authorId,
                    onClick = {
                        navController.popBackStack()
                    },
                    onPostClick = { post ->
                        val timelineRoute = AppDestinations.TIMELINE_ROUTE
                            .replace("{${AppDestinations.TIMELINE_ARG_ACCOUNT_ID}}", authorId)
                            .replace("{${AppDestinations.TIMELINE_ARG_POST_ID}}", post.id)
                        navController.navigate(timelineRoute)
                    }
                )
            }
        }
        // タイムライン画面
        composable(
            route = AppDestinations.TIMELINE_ROUTE,
            arguments = listOf(
                navArgument(AppDestinations.TIMELINE_ARG_ACCOUNT_ID) { type = NavType.StringType },
                navArgument(AppDestinations.TIMELINE_ARG_POST_ID) { type = NavType.StringType }
            )
        ) { backStackEntry ->
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