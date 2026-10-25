package com.albin.betdaylite.presentation.history

import com.albin.betdaylite.domain.entities.Bet
import com.albin.betdaylite.domain.entities.BetStatus

data class BetsUiState(
    val bets: List<Bet> = emptyList(),
    val selectedFilter: BetStatus? = null,
    val totalWon: Double = 0.0,
    val totalWagered: Double = 0.0,
    val isLoading: Boolean = false
) {
    val profit: Double
        get() = totalWon - totalWagered
}
