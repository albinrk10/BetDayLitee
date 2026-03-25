package com.albin.betdaylite.presentation.profile

import com.albin.betdaylite.domain.model.BetStatus


sealed class ProfileUiState {
    data object Loading : ProfileUiState()
    data class Success(val bets: List<ProfileBetUiModel>) : ProfileUiState()
    data object Empty : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

data class ProfileBetUiModel(
    val id: String,
    val teams: String,
    val pickLabel: String,
    val odd: Double,
    val stake: Double,
    val status: BetStatus,
    val payout: Double?
)

sealed class ProfileEvent {
    data class BetDeleted(val betId: String) : ProfileEvent()
}

