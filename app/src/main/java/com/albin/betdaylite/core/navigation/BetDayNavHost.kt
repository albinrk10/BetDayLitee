package com.albin.betdaylite.core.navigation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.albin.betdaylite.core.designsystem.GoldPrimary
import com.albin.betdaylite.core.designsystem.ObsidianBackground
import com.albin.betdaylite.core.designsystem.ObsidianBorder
import com.albin.betdaylite.core.designsystem.ObsidianSurface
import com.albin.betdaylite.core.designsystem.TextSecondary
import com.albin.betdaylite.presentation.detail.MatchDetailScreen
import com.albin.betdaylite.presentation.history.BetsHistoryScreen
import com.albin.betdaylite.presentation.home.HomeScreen
import com.albin.betdaylite.presentation.profile.ProfileScreen

@Composable
fun BetDayNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isDetailScreen = currentDestination?.hasRoute(BetRoute.MatchDetail::class) == true

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ObsidianBackground,
        bottomBar = {
            if (!isDetailScreen) {
                NavigationBar(
                    containerColor = ObsidianSurface,
                    modifier = Modifier.border(width = 1.dp, color = ObsidianBorder)
                ) {
                    // Tab 1: Partidos (Home)
                    val isHomeSelected = currentDestination?.hierarchy?.any { it.hasRoute(BetRoute.Home::class) } == true
                    NavigationBarItem(
                        selected = isHomeSelected,
                        onClick = {
                            navController.navigate(BetRoute.Home) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.SportsSoccer, contentDescription = "Partidos") },
                        label = { Text("Partidos") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GoldPrimary,
                            selectedTextColor = GoldPrimary,
                            indicatorColor = GoldPrimary.copy(alpha = 0.18f),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )

                    // Tab 2: Mis Apuestas
                    val isBetsSelected = currentDestination?.hierarchy?.any { it.hasRoute(BetRoute.BetsHistory::class) } == true
                    NavigationBarItem(
                        selected = isBetsSelected,
                        onClick = {
                            navController.navigate(BetRoute.BetsHistory) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = "Mis Apuestas") },
                        label = { Text("Mis Apuestas") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GoldPrimary,
                            selectedTextColor = GoldPrimary,
                            indicatorColor = GoldPrimary.copy(alpha = 0.18f),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )

                    // Tab 3: Billetera / Perfil
                    val isProfileSelected = currentDestination?.hierarchy?.any { it.hasRoute(BetRoute.Profile::class) } == true
                    NavigationBarItem(
                        selected = isProfileSelected,
                        onClick = {
                            navController.navigate(BetRoute.Profile) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Billetera") },
                        label = { Text("Billetera") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GoldPrimary,
                            selectedTextColor = GoldPrimary,
                            indicatorColor = GoldPrimary.copy(alpha = 0.18f),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BetRoute.Home,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<BetRoute.Home> {
                HomeScreen(
                    onNavigateToMatchDetail = { matchId ->
                        navController.navigate(BetRoute.MatchDetail(matchId))
                    },
                    onNavigateToBetsHistory = {
                        navController.navigate(BetRoute.BetsHistory)
                    }
                )
            }

            composable<BetRoute.MatchDetail> { backStackEntry ->
                val detailRoute = backStackEntry.toRoute<BetRoute.MatchDetail>()
                MatchDetailScreen(
                    matchId = detailRoute.matchId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<BetRoute.BetsHistory> {
                BetsHistoryScreen()
            }

            composable<BetRoute.Profile> {
                ProfileScreen()
            }
        }
    }
}
