package com.albin.betdaylite.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.albin.betdaylite.domain.model.BetPick
import com.albin.betdaylite.domain.model.Match
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    
    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeEvent.BetPlacedSuccess -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Apuesta creada: ${event.bet.pick} @ ${String.format("%.2f", event.bet.odd)}"
                        )
                    }
                }
                is HomeEvent.BetPlacedError -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "No se pudo apostar: ${event.message}"
                        )
                    }
                }
            }
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is HomeUiState.Loading -> {
                LoadingState()
            }
            is HomeUiState.Success -> {
                if (state.matchesByHour.isEmpty()) {
                    EmptyHomeState()
                } else {
                    MatchesTimeline(
                        matchesByHour = state.matchesByHour,
                        onBetClick = { matchId, pick, stake, odd ->
                            viewModel.placeBet(matchId, pick, stake, odd)
                        }
                    )
                }
            }
            is HomeUiState.Error -> {
                ErrorState(message = state.message)
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp)
        )
    }
}

@Composable
private fun BettingHint() {
    Text(
        text = "Tip: toca 1, X o 2 para crear una apuesta simulada (stake fijo: S/10).",
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray,
        modifier = Modifier.padding(horizontal = 12.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun EmptyHomeState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sin eventos por mostrar",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Cargando datos iniciales...",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
private fun LoadingState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text("Cargando partidos...")
    }
}

@Composable
private fun ErrorState(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
private fun MatchesTimeline(
    matchesByHour: Map<Int, List<Match>>,
    onBetClick: (matchId: String, pick: BetPick, stake: Double, odd: Double) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 12.dp,
            vertical = 8.dp
        )
    ) {
        item {
            BettingHint()
        }
        matchesByHour.toSortedMap().forEach { (hour, matches) ->
            stickyHeader {
                HourHeader(hour = hour)
            }
            
            items(
                items = matches,
                key = { it.id }
            ) { match ->
                MatchCard(
                    match = match,
                    onBetClick = { pick ->
                        onBetClick(match.id, pick, 10.0, 
                            when (pick) {
                                BetPick.HOME -> match.market.odds.home
                                BetPick.DRAW -> match.market.odds.draw
                                BetPick.AWAY -> match.market.odds.away
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun HourHeader(hour: Int) {
    Text(
        text = "$hour:00",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(12.dp)
    )
}

@Composable
private fun MatchCard(
    match: Match,
    onBetClick: (pick: BetPick) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = match.league.name,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.homeTeam.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "VS",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Text(
                    text = match.awayTeam.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Mercado 1X2",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf(
                    Triple("1", BetPick.HOME, match.market.odds.home),
                    Triple("X", BetPick.DRAW, match.market.odds.draw),
                    Triple("2", BetPick.AWAY, match.market.odds.away)
                ).forEach { (label, pick, odd) ->
                    BetButton(
                        label = label,
                        odd = odd,
                        modifier = Modifier.weight(1f),
                        onClick = { onBetClick(pick) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BetButton(
    label: String,
    odd: Double,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val buttonColor by animateColorAsState(
        targetValue = if (isPressed) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
        label = "betButtonColor"
    )
    val coroutineScope = rememberCoroutineScope()
    
    Button(
        onClick = {
            isPressed = true
            onClick()
            coroutineScope.launch {
                delay(220)
                isPressed = false
            }
        },
        modifier = modifier.padding(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelLarge.fontSize)
            Text(
                text = String.format("%.2f", odd),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

