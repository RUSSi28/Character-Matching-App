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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.charactermatchingapp.domain.matching.model.CharacterInfo
import com.example.charactermatchingapp.presentation.gallery.GalleryApp
import com.example.charactermatchingapp.data.gallery.repository.GalleryRepositoryImpl
import com.example.charactermatchingapp.domain.gallery.repository.GalleryRepository
import com.example.charactermatchingapp.domain.gallery.usecase.GetGalleryItemsUseCase
import com.example.charactermatchingapp.presentation.gallery.GalleryViewModel
import com.example.charactermatchingapp.presentation.gallery.GalleryViewModelFactory
import com.example.charactermatchingapp.presentation.matching.CharacterMatchingScreen
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import com.example.charactermatchingapp.presentation.auth.AuthViewModel
import com.example.charactermatchingapp.presentation.auth.LoginScreen
import com.example.charactermatchingapp.presentation.auth.SignUpScreen
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

import android.util.Log

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    data object Matching : Screen("matching")

    @Serializable
    data object Gallery : Screen("gallery")

    @Serializable
    data object Home : Screen("home")

    @Serializable
    data object Settings : Screen("settings")

    @Serializable
    data object Login : Screen("login")

    @Serializable
    data object SignUp : Screen("signup")
}

private val bottomNavigationAllowedScreen = setOf(
    Screen.Matching,
    Screen.Gallery,
    Screen.Home,
    Screen.Settings
)

fun NavDestination.toBottomNavigationTab(): BottomNavigationTab {
    return when {
        hasRoute(Screen.Matching::class) -> BottomNavigationTab.Matching
        hasRoute(Screen.Gallery::class) -> BottomNavigationTab.Gallery
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
    val authViewModel: AuthViewModel = viewModel()
    val authUiState by authViewModel.uiState.collectAsState()

    val startDestination = Screen.Login

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
                onSignUp = { email, password ->
                    authViewModel.signUp(email, password)
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
            val galleryDatasource: GalleryRepository = GalleryRepositoryImpl()
            val getGalleryItemsUseCase = GetGalleryItemsUseCase(galleryDatasource)
            val galleryViewModel: GalleryViewModel = viewModel(factory = GalleryViewModelFactory(getGalleryItemsUseCase, com.google.firebase.auth.FirebaseAuth.getInstance()))
            GalleryApp(galleryViewModel = galleryViewModel)
        }
        composable<Screen.Home> {

        }
        composable<Screen.Settings> {

        }
    }
}