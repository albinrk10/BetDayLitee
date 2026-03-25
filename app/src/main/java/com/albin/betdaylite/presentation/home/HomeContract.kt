package com.albin.betdaylite.presentation.home

import com.albin.betdaylite.domain.model.Bet
import com.albin.betdaylite.domain.model.Match


sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val matchesByHour: Map<Int, List<Match>>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}


sealed class HomeEvent {
    data class BetPlacedSuccess(val bet: Bet) : HomeEvent()
    data class BetPlacedError(val message: String) : HomeEvent()
}

