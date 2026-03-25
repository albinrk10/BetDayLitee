package com.albin.betdaylite.presentation.betdetail

import com.albin.betdaylite.domain.model.BetStatus

data class BetDetailUiModel(
    val betId: String,
    val teams: String,
    val leagueName: String,
    val pickLabel: String,
    val odd: Double,
    val stake: Double,
    val potentialReturn: Double,
    val status: BetStatus,
    val placedAtText: String
)

sealed class BetDetailUiState {
    data object Loading : BetDetailUiState()
    data class Success(val detail: BetDetailUiModel) : BetDetailUiState()
    data object NotFound : BetDetailUiState()
    data class Error(val message: String) : BetDetailUiState()
}

