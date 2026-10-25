package com.albin.betdaylite.core.navigation

import kotlinx.serialization.Serializable

sealed interface BetRoute {
    @Serializable
    data object Home : BetRoute

    @Serializable
    data class MatchDetail(val matchId: String) : BetRoute

    @Serializable
    data object BetsHistory : BetRoute

    @Serializable
    data object Profile : BetRoute
}
