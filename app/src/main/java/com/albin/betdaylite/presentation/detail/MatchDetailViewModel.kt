package com.albin.betdaylite.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albin.betdaylite.domain.entities.BetPick
import com.albin.betdaylite.domain.entities.BetSlip
import com.albin.betdaylite.domain.usecases.GetMatchDetailUseCase
import com.albin.betdaylite.domain.usecases.GetWalletBalanceUseCase
import com.albin.betdaylite.domain.usecases.PlaceBetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchDetailViewModel @Inject constructor(
    private val getMatchDetailUseCase: GetMatchDetailUseCase,
    private val placeBetUseCase: PlaceBetUseCase,
    private val getWalletBalanceUseCase: GetWalletBalanceUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchDetailUiState())
    val uiState: StateFlow<MatchDetailUiState> = _uiState.asStateFlow()

    init {
        // En Navigation Compose con @Serializable, el argumento puede estar en savedStateHandle
        val matchIdFromNav: String? = savedStateHandle["matchId"]
        if (matchIdFromNav != null) {
            loadMatch(matchIdFromNav)
        }

        observeWallet()
    }

    private fun observeWallet() {
        viewModelScope.launch {
            getWalletBalanceUseCase().collect { wallet ->
                _uiState.update { it.copy(wallet = wallet) }
            }
        }
    }

    fun loadMatch(matchId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val match = getMatchDetailUseCase(matchId)
            if (match != null) {
                _uiState.update { it.copy(match = match, isLoading = false) }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Partido no encontrado") }
            }
        }
    }

    fun onPickSelected(pick: BetPick) {
        val currentMatch = _uiState.value.match ?: return
        val odd = currentMatch.market.odds.getOddForPick(pick)
        val slip = BetSlip(
            match = currentMatch,
            pick = pick,
            odd = odd,
            stake = 10.0
        )
        _uiState.update { it.copy(activeBetSlip = slip) }
    }

    fun onDismissBetSlip() {
        _uiState.update { it.copy(activeBetSlip = null) }
    }

    fun onConfirmBet(stake: Double) {
        val slip = _uiState.value.activeBetSlip ?: return
        viewModelScope.launch {
            val result = placeBetUseCase(
                match = slip.match,
                pick = slip.pick,
                odd = slip.odd,
                stake = stake
            )
            result.onSuccess { bet ->
                _uiState.update {
                    it.copy(
                        activeBetSlip = null,
                        betSuccessMessage = "¡Apuesta realizada con éxito! (${bet.pick.label}) @ ${String.format("%.2f", bet.odd)}"
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(error = error.message ?: "Error al confirmar apuesta")
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(betSuccessMessage = null, error = null) }
    }
}
