package com.albin.betdaylite.presentation.profile

import com.albin.betdaylite.domain.entities.UserWallet

data class ProfileUiState(
    val wallet: UserWallet? = null,
    val totalBetsCount: Int = 0,
    val wonBetsCount: Int = 0,
    val depositSuccessMessage: String? = null,
    val isLoading: Boolean = false
) {
    val winRate: Double
        get() = if (totalBetsCount > 0) (wonBetsCount.toDouble() / totalBetsCount.toDouble()) * 100.0 else 0.0
}
