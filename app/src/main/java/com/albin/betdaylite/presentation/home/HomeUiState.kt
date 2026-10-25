package com.albin.betdaylite.presentation.home

import com.albin.betdaylite.domain.entities.BetSlip
import com.albin.betdaylite.domain.entities.Match
import com.albin.betdaylite.domain.entities.UserWallet

data class HomeUiState(
    val featuredMatch: Match? = null,
    val matches: List<Match> = emptyList(),
    val selectedLeague: String = "Todas",
    val activeBetSlip: BetSlip? = null,
    val wallet: UserWallet? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val betSuccessMessage: String? = null
)
