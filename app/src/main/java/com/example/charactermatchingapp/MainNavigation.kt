package com.example.charactermatchingapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.charactermatchingapp.domain.matching.model.CharacterInfo
import com.example.charactermatchingapp.presentation.matching.CharacterMatchingScreen
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    data object Matching : Screen("matching")

    @Serializable
    data object Add : Screen("add")

    @Serializable
    data object Home : Screen("home")

    @Serializable
    data object Settings : Screen("settings")
}

private val bottomNavigationAllowedScreen = setOf(
    Screen.Matching,
    Screen.Add,
    Screen.Home,
    Screen.Settings
)

fun NavDestination.toBottomNavigationTab(): BottomNavigationTab {
    return when {
        hasRoute(Screen.Matching::class) -> BottomNavigationTab.Matching
        hasRoute(Screen.Add::class) -> BottomNavigationTab.Add
        hasRoute(Screen.Home::class) -> BottomNavigationTab.Home
        hasRoute(Screen.Settings::class) -> BottomNavigationTab.Settings
        else -> BottomNavigationTab.Matching
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = remember(navBackStackEntry) { navBackStackEntry?.destination }
    val isBottomNavigationShown = remember(currentDestination) {
        bottomNavigationAllowedScreen.any {
            currentDestination?.hasRoute(it::class) ?: false
        }
    }

    val hazeState = rememberHazeState()
    Scaffold(
        bottomBar = {
            if (isBottomNavigationShown) {
                GlassmorphicBottomNavigation(
                    currentTab = currentDestination?.toBottomNavigationTab()
                        ?: BottomNavigationTab.Matching,
                    onItemClick = { item ->
                        navController.navigate(item) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    hazeState = hazeState,
                )
            }
        },
        containerColor = Color(0xFF363636),
        modifier = modifier
    ) { innerPadding ->
        NavigationHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    // TODO(): リモートからデータを読み込めるようにして消す
    val items = remember {
        mutableStateListOf(
            CharacterInfo(
                id = "1",
                name = "Character 1",
                image = "https://example.com/image1.jpg",
                description = "Description for Character 1",
                tags = listOf("優しい", "天真爛漫"),
                contributor = "Contributor 1"
            ),
            CharacterInfo(
                id = "2",
                name = "Character 2",
                image = "https://example.com/image2.jpg",
                description = "Description for Character 2",
                tags = listOf("根暗", "リスカ", "眼帯", "デコラ風", "優しい"),
                contributor = "Contributor 1"
            ),
            CharacterInfo(
                id = "3",
                name = "Character 3",
                image = "https://example.com/image3.jpg",
                description = "Description for Character 3",
                tags = listOf("あほ", "明るい"),
                contributor = "Contributor 2"
            )
        )
    }
    NavHost(
        navController = navController,
        startDestination = Screen.Matching,
        modifier = modifier
    ) {
        composable<Screen.Matching> {
            CharacterMatchingScreen(
                items = items.toImmutableList(),
                onItemsDrop = {
                    items.removeAt(items.lastIndex)
                }
            )
        }
        composable<Screen.Add> {
            FavoriteScreenNavHost("NjMe4XK8J4rm9f4ogvEj")
        }
        composable<Screen.Home> {

        }
        composable<Screen.Settings> {
            PosterViewScreenNavHost("NjMe4XK8J4rm9f4ogvEj")
        }
    }
}