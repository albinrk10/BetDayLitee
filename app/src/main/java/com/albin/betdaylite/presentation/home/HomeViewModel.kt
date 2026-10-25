package com.albin.betdaylite.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albin.betdaylite.domain.entities.BetPick
import com.albin.betdaylite.domain.entities.BetSlip
import com.albin.betdaylite.domain.entities.Match
import com.albin.betdaylite.domain.usecases.DepositFundsUseCase
import com.albin.betdaylite.domain.usecases.GetFeaturedMatchUseCase
import com.albin.betdaylite.domain.usecases.GetMatchesUseCase
import com.albin.betdaylite.domain.usecases.GetWalletBalanceUseCase
import com.albin.betdaylite.domain.usecases.PlaceBetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val getFeaturedMatchUseCase: GetFeaturedMatchUseCase,
    private val placeBetUseCase: PlaceBetUseCase,
    private val getWalletBalanceUseCase: GetWalletBalanceUseCase,
    private val depositFundsUseCase: DepositFundsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val selectedLeagueFlow = MutableStateFlow("Todas")

    init {
        observeData()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeData() {
        viewModelScope.launch {
            // Observar billetera
            launch {
                getWalletBalanceUseCase().collect { wallet ->
                    _uiState.update { it.copy(wallet = wallet) }
                }
            }

            // Observar partido estelar
            launch {
                getFeaturedMatchUseCase().collect { featured ->
                    _uiState.update { it.copy(featuredMatch = featured) }
                }
            }

            // Observar partidos según liga seleccionada
            launch {
                selectedLeagueFlow.flatMapLatest { league ->
                    getMatchesUseCase(league)
                }.collect { matches ->
                    _uiState.update {
                        it.copy(
                            matches = matches,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onLeagueSelected(league: String) {
        selectedLeagueFlow.value = league
        _uiState.update { it.copy(selectedLeague = league) }
    }

    fun onPickSelected(match: Match, pick: BetPick) {
        val odd = match.market.odds.getOddForPick(pick)
        val slip = BetSlip(
            match = match,
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
                        betSuccessMessage = "¡Apuesta confirmada! ${bet.matchDescription} (${bet.pick.label}) @ ${String.format("%.2f", bet.odd)}"
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(errorMessage = error.message ?: "Error al colocar apuesta")
                }
            }
        }
    }

    fun onDepositFunds(amount: Double) {
        viewModelScope.launch {
            depositFundsUseCase(amount)
        }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(betSuccessMessage = null) }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
