package com.movieai.app.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.movieai.app.presentation.components.BottomNav
import com.movieai.app.presentation.detail.DetailScreen
import com.movieai.app.presentation.favorites.FavoritesScreen
import com.movieai.app.presentation.recommend.RecommendScreen
import com.movieai.app.presentation.search.SearchScreen
import com.movieai.app.presentation.theme.MovieAiColors

@Composable
fun MovieAiNavGraph() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route ?: Screen.Search.route

    Scaffold(
        containerColor = MovieAiColors.bg,
        bottomBar = {
            if (currentRoute in Screen.topLevelRoutes) {
                BottomNav(currentRoute = currentRoute, onSelect = { route ->
                    if (route == currentRoute) return@BottomNav
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Search.route,
            modifier = Modifier.padding(padding),
        ) {
            composable(Screen.Search.route) {
                SearchScreen(onMovieClick = { id ->
                    navController.navigate(Screen.Detail.build(id))
                })
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onMovieClick = { id -> navController.navigate(Screen.Detail.build(id)) },
                    onAiRecClick = { navController.navigate(Screen.Recommend.route) },
                )
            }
            composable(Screen.Recommend.route) {
                RecommendScreen(onMovieClick = { id ->
                    navController.navigate(Screen.Detail.build(id))
                })
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument(Screen.Detail.ARG_ID) { type = NavType.LongType }),
            ) { entry ->
                val id = entry.arguments?.getLong(Screen.Detail.ARG_ID) ?: 0L
                DetailScreen(id = id, onBack = { navController.popBackStack() })
            }
        }
    }
}
