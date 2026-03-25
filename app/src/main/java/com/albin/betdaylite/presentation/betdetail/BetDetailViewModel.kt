package com.albin.betdaylite.presentation.betdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albin.betdaylite.domain.model.Bet
import com.albin.betdaylite.domain.model.BetPick
import com.albin.betdaylite.domain.model.Match
import com.albin.betdaylite.domain.repository.MatchRepository
import com.albin.betdaylite.domain.usecase.GetBetDetailUseCase
import com.albin.betdaylite.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BetDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBetDetailUseCase: GetBetDetailUseCase,
    private val matchRepository: MatchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BetDetailUiState>(BetDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val betId: String = savedStateHandle.get<String>(Route.BetDetail.BET_ID_ARG).orEmpty()

    init {
        loadBetDetail()
    }

    private fun loadBetDetail() {
        if (betId.isBlank()) {
            _uiState.value = BetDetailUiState.Error("ID de apuesta invalido")
            return
        }

        viewModelScope.launch {
            getBetDetailUseCase.invoke(betId)
                .onSuccess { bet ->
                    if (bet == null) {
                        _uiState.value = BetDetailUiState.NotFound
                        return@onSuccess
                    }
                    val match = matchRepository.getMatchById(bet.matchId)
                    _uiState.value = BetDetailUiState.Success(bet.toUiModel(match))
                }
                .onFailure { error ->
                    _uiState.value = BetDetailUiState.Error(error.message ?: "No se pudo cargar la apuesta")
                }
        }
    }
}

private fun Bet.toUiModel(match: Match?): BetDetailUiModel {
    val teams = if (match != null) {
        "${match.homeTeam.name} vs ${match.awayTeam.name}"
    } else {
        "Partido no disponible"
    }

    val leagueName = match?.league?.name ?: "Liga no disponible"
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", Locale("es", "PE"))
    val placedAtText = placedAt.atZone(ZoneId.systemDefault()).format(formatter)

    return BetDetailUiModel(
        betId = id,
        teams = teams,
        leagueName = leagueName,
        pickLabel = pick.toLabel(),
        odd = odd,
        stake = stake,
        potentialReturn = odd * stake,
        status = status,
        placedAtText = placedAtText
    )
}

private fun BetPick.toLabel(): String {
    return when (this) {
        BetPick.HOME -> "1 (Local)"
        BetPick.DRAW -> "X (Empate)"
        BetPick.AWAY -> "2 (Visita)"
    }
}

