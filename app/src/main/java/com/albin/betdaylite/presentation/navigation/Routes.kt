package com.albin.betdaylite.presentation.navigation


sealed class Route(val route: String) {
    data object Home : Route("home")
    data object Profile : Route("profile")
    data class BetDetail(val betId: String) : Route("bet_detail/{betId}") {
        companion object {
            const val BET_ID_ARG = "betId"
            fun createRoute(betId: String) = "bet_detail/$betId"
        }
    }
}

