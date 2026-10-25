package com.albin.betdaylite.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albin.betdaylite.domain.entities.BetStatus
import com.albin.betdaylite.domain.usecases.DepositFundsUseCase
import com.albin.betdaylite.domain.usecases.GetBetsUseCase
import com.albin.betdaylite.domain.usecases.GetWalletBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getWalletBalanceUseCase: GetWalletBalanceUseCase,
    private val getBetsUseCase: GetBetsUseCase,
    private val depositFundsUseCase: DepositFundsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            launch {
                getWalletBalanceUseCase().collect { wallet ->
                    _uiState.update { it.copy(wallet = wallet, isLoading = false) }
                }
            }

            launch {
                getBetsUseCase().collect { bets ->
                    val wonCount = bets.count { it.status == BetStatus.WON }
                    _uiState.update {
                        it.copy(
                            totalBetsCount = bets.size,
                            wonBetsCount = wonCount
                        )
                    }
                }
            }
        }
    }

    fun depositFunds(amount: Double) {
        viewModelScope.launch {
            depositFundsUseCase(amount)
            _uiState.update {
                it.copy(depositSuccessMessage = "¡Depósito exitoso de +S/ $amount a tu saldo!")
            }
        }
    }

    fun clearDepositMessage() {
        _uiState.update { it.copy(depositSuccessMessage = null) }
    }
}
