package com.example.charactermatchingapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.charactermatchingapp.presentation.SharedViewModel
import com.example.charactermatchingapp.presentation.auth.AuthViewModel
import com.example.charactermatchingapp.presentation.auth.LoginScreen
import com.example.charactermatchingapp.presentation.auth.SignUpScreen
import com.example.charactermatchingapp.presentation.gallery.GalleryApp
import com.example.charactermatchingapp.presentation.gallery.GalleryViewModel
import com.example.charactermatchingapp.presentation.matching.CharacterMatchingScreen
import com.example.charactermatchingapp.presentation.post.CharacterPostScreen
import com.example.charactermatchingapp.presentation.post.PostViewModel
import com.example.charactermatchingapp.presentation.recommendation.RecommendationScreen
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    data object Matching : Screen("matching")

    @Serializable
    data object Gallery : Screen("gallery")

    @Serializable
    data object Home : Screen("home")

    @Serializable
    data object Recommend : Screen("recommend")

    @Serializable
    data object Login : Screen("login")

    @Serializable
    data object SignUp : Screen("signup")
}

private val bottomNavigationAllowedScreen = setOf(
    Screen.Matching,
    Screen.Gallery,
    Screen.Home,
    Screen.Recommend
)

fun NavDestination.toBottomNavigationTab(): BottomNavigationTab {
    return when {
        hasRoute(Screen.Matching::class) -> BottomNavigationTab.Matching
        hasRoute(Screen.Gallery::class) -> BottomNavigationTab.Gallery
        hasRoute(Screen.Home::class) -> BottomNavigationTab.Home
        hasRoute(Screen.Recommend::class) -> BottomNavigationTab.Recommend
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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
            snackbarHostState = snackbarHostState,
            scope = scope
        )
    }
}

@Composable
private fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    val startDestination = Screen.Login
    val scope = rememberCoroutineScope()

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
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<Screen.Login> {
            val authViewModel: AuthViewModel = koinViewModel()
            val authUiState by authViewModel.uiState.collectAsState()

            LaunchedEffect(authUiState.isLoginSuccess) {
                if (authUiState.isLoginSuccess) {
                    navController.navigate(Screen.Matching) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                    authViewModel.resetAuthStates()
                }
            }
            LoginScreen(
                uiState = authUiState,
                onLogin = { email, password ->
                    authViewModel.login(email, password)
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp)
                }
            )
        }
        composable<Screen.SignUp> {
            val authViewModel: AuthViewModel = koinViewModel()
            val authUiState by authViewModel.uiState.collectAsState()

            LaunchedEffect(authUiState.isSignUpSuccess) {
                if (authUiState.isSignUpSuccess) {
                    navController.navigate(Screen.Matching) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                    authViewModel.resetAuthStates()
                }
            }
            SignUpScreen(
                uiState = authUiState,
                onSignUp = { email, password, displayName ->
                    authViewModel.signUp(email, password, displayName)
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login)
                }
            )
        }
        composable<Screen.Matching> {
            CharacterMatchingScreen(
                items = items.toImmutableList(),
                onItemsDrop = {
                    items.removeAt(items.lastIndex)
                }
            )
        }
        composable<Screen.Gallery> {
            val galleryViewModel: GalleryViewModel = koinViewModel()
            GalleryApp(galleryViewModel = galleryViewModel)
        }
        composable<Screen.Home> {
            val sharedViewModel: SharedViewModel = koinViewModel()
            val userId = sharedViewModel.currentUserProvider.getCurrentUserId()
            if (userId != null) {
                PosterViewScreenNavHost(userId)
            }
        }
        composable<Screen.Recommend> {
            RecommendationScreen()
        }

    }
}
