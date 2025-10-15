package com.iid.iiflashcards.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iid.iiflashcards.ui.screens.deckreview.DeckReviewScreen
import com.iid.iiflashcards.ui.screens.home.HomeScreen

sealed class NavDestination(val route: String) {
    data object Home : NavDestination(route = "home")
    data object DeckReview : NavDestination(route = "deck_review")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavDestination.Home.route) {
        composable(route = NavDestination.Home.route) {
            HomeScreen(navigateTo = { navController.navigate(it.route) })
        }
        composable(route = NavDestination.DeckReview.route) {
            DeckReviewScreen()
        }
    }
}
