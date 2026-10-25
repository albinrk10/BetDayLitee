package com.albin.betdaylite.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albin.betdaylite.domain.entities.BetStatus
import com.albin.betdaylite.domain.usecases.GetBetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BetsViewModel @Inject constructor(
    private val getBetsUseCase: GetBetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BetsUiState(isLoading = true))
    val uiState: StateFlow<BetsUiState> = _uiState.asStateFlow()

    private val filterFlow = MutableStateFlow<BetStatus?>(null)

    init {
        observeBets()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeBets() {
        viewModelScope.launch {
            filterFlow.flatMapLatest { filter ->
                getBetsUseCase(filter)
            }.collect { bets ->
                val wonAmount = bets.filter { it.status == BetStatus.WON }.sumOf { (it.payout ?: 0.0) }
                val wageredAmount = bets.sumOf { it.stake }

                _uiState.update {
                    it.copy(
                        bets = bets,
                        totalWon = wonAmount,
                        totalWagered = wageredAmount,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onFilterSelected(status: BetStatus?) {
        filterFlow.value = status
        _uiState.update { it.copy(selectedFilter = status) }
    }
}
