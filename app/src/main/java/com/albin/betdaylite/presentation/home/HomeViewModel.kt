package com.albin.betdaylite.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albin.betdaylite.domain.model.BetPick
import com.albin.betdaylite.domain.repository.BetRepository
import com.albin.betdaylite.domain.repository.MatchRepository
import com.albin.betdaylite.domain.usecase.GetMatchesGroupedByHourUseCase
import com.albin.betdaylite.domain.usecase.PlaceBetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMatchesGroupedByHourUseCase: GetMatchesGroupedByHourUseCase,
    private val placeBetUseCase: PlaceBetUseCase,
    private val matchRepository: MatchRepository,
    private val betRepository: BetRepository
) : ViewModel() {
    
    // State
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()  // Exposición read-only
    
    // One-time events
    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()
    
    init {
        bootstrapData()
        loadMatches()
    }

    private fun bootstrapData() {
        viewModelScope.launch {
            matchRepository.ensureMatchesBootstrapped()
            betRepository.ensureBetsBootstrapped()
        }
    }
    
    private fun loadMatches() {
        viewModelScope.launch {
            try {
                getMatchesGroupedByHourUseCase()
                    .collect { matchesByHour ->
                        _uiState.value = HomeUiState.Success(matchesByHour)
                    }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    /**
     * Dispara la acción de realizar apuesta
     *
     * Flujo:
     * 1. Muestra loading
     * 2. Ejecuta use case
     * 3. Si OK: muestra éxito y emite evento
     * 4. Si error: muestra error y emite evento
     */
    fun placeBet(
        matchId: String,
        pick: BetPick,
        stake: Double,
        odd: Double
    ) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            
            val result = placeBetUseCase(matchId, pick, stake, odd)
            
            result
                .onSuccess { bet ->
                    _events.send(HomeEvent.BetPlacedSuccess(bet))
                    loadMatches()  // Recarga (actualiza estado)
                }
                .onFailure { error ->
                    val errorMsg = error.message ?: "Unknown error"
                    _events.send(HomeEvent.BetPlacedError(errorMsg))
                    loadMatches()  // Restaura estado
                }
        }
    }
}

