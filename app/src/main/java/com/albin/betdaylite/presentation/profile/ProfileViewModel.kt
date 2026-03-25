package com.albin.betdaylite.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albin.betdaylite.domain.model.Bet
import com.albin.betdaylite.domain.model.BetPick
import com.albin.betdaylite.domain.repository.MatchRepository
import com.albin.betdaylite.domain.repository.BetRepository
import com.albin.betdaylite.domain.usecase.GetUserBetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserBetsUseCase: GetUserBetsUseCase,
    private val betRepository: BetRepository,
    private val matchRepository: MatchRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()
    
    init {
        bootstrapBets()
        loadBets()
    }

    private fun bootstrapBets() {
        viewModelScope.launch {
            betRepository.ensureBetsBootstrapped()
        }
    }
    
    private fun loadBets() {
        viewModelScope.launch {
            try {
                getUserBetsUseCase()
                    .collect { bets ->
                        _uiState.value = if (bets.isEmpty()) {
                            ProfileUiState.Empty
                        } else {
                            ProfileUiState.Success(bets.map { it.toUiModel(matchRepository) })
                        }
                    }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

private suspend fun Bet.toUiModel(matchRepository: MatchRepository): ProfileBetUiModel {
    val match = matchRepository.getMatchById(matchId)
    val teams = if (match != null) {
        "${match.homeTeam.name} vs ${match.awayTeam.name}"
    } else {
        "Partido no disponible"
    }

    return ProfileBetUiModel(
        id = id,
        teams = teams,
        pickLabel = pick.toLabel(),
        odd = odd,
        stake = stake,
        status = status,
        payout = payout
    )
}

private fun BetPick.toLabel(): String {
    return when (this) {
        BetPick.HOME -> "1"
        BetPick.DRAW -> "X"
        BetPick.AWAY -> "2"
    }
}

