package com.albin.betdaylite.presentation.detail

import com.albin.betdaylite.domain.entities.BetSlip
import com.albin.betdaylite.domain.entities.Match
import com.albin.betdaylite.domain.entities.UserWallet

data class MatchDetailUiState(
    val match: Match? = null,
    val activeBetSlip: BetSlip? = null,
    val wallet: UserWallet? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val betSuccessMessage: String? = null
)
