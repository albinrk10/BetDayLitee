package com.albin.betdaylite.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.albin.betdaylite.presentation.betdetail.BetDetailScreen
import com.albin.betdaylite.presentation.home.HomeScreen
import com.albin.betdaylite.presentation.profile.ProfileScreen


@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = Route.Home.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        // Home Screen
        composable(Route.Home.route) {
            HomeScreen()
        }
        
        // Profile Screen
        composable(Route.Profile.route) {
            ProfileScreen(
                onBetClick = { betId ->
                    navController.navigate(Route.BetDetail.createRoute(betId))
                },
                onGoToHome = {
                    navController.navigate(Route.Home.route) {
                        launchSingleTop = true
                        popUpTo(Route.Home.route)
                    }
                }
            )
        }
        
        // Bet Detail Screen (placeholder por ahora)
        composable(
            route = Route.BetDetail("").route,
            arguments = listOf(
                navArgument(Route.BetDetail.BET_ID_ARG) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val betId = backStackEntry.arguments?.getString(Route.BetDetail.BET_ID_ARG)
            if (!betId.isNullOrBlank()) {
                BetDetailScreen()
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se pudo abrir el detalle de la apuesta")
                }
            }
        }
    }
}

