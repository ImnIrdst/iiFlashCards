package com.iid.iiflashcards.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iid.iiflashcards.ui.screens.addcard.AddCardScreen
import com.iid.iiflashcards.ui.screens.deckreview.DeckReviewScreen
import com.iid.iiflashcards.ui.screens.profile.ProfileScreen
import com.iid.iiflashcards.ui.screens.signin.SignInScreen
import com.iid.iiflashcards.ui.screens.signin.SignInViewModel

sealed class NavEvent(val route: String) {
    data object Home : NavEvent("home")
    data object DeckReview : NavEvent("deck_review")
    data object AddCard : NavEvent("add_card")
    data object SignIn : NavEvent("login")
    data object Profile : NavEvent("profile")
    data object PopBackStack : NavEvent("pop_back_stack")
}

fun doNavigation(navController: NavController, navEvent: NavEvent) {
    when (navEvent) {
        NavEvent.SignIn,
        NavEvent.Home -> {
            navController.navigate(route = navEvent.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }

        NavEvent.Profile,
        NavEvent.AddCard,
        NavEvent.DeckReview -> {
            navController.navigate(navEvent.route)
        }

        NavEvent.PopBackStack -> {
            navController.popBackStack()
        }
    }
}

@Composable
fun AppNavigation(signInViewModel: SignInViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavEvent.SignIn.route) {
        composable(NavEvent.Home.route) {
            // HomeScreen { doNavigation(navController, navEvent = it) } // Its empty for now
            DeckReviewScreen { doNavigation(navController, navEvent = it) }
        }
        composable(NavEvent.DeckReview.route) {
            DeckReviewScreen { doNavigation(navController, navEvent = it) }
        }
        composable(NavEvent.AddCard.route) {
            AddCardScreen { doNavigation(navController, navEvent = it) }
        }
        composable(NavEvent.SignIn.route) {
            SignInScreen(signInViewModel) { doNavigation(navController, navEvent = it) }
        }
        composable(NavEvent.Profile.route) {
            ProfileScreen(signInViewModel) { doNavigation(navController, navEvent = it) }
        }
    }
}
