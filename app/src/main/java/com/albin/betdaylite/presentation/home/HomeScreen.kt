package com.albin.betdaylite.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.albin.betdaylite.core.constants.BetConstants
import com.albin.betdaylite.core.designsystem.AppSpacing
import com.albin.betdaylite.core.designsystem.CardShape
import com.albin.betdaylite.core.designsystem.GoldPrimary
import com.albin.betdaylite.core.designsystem.ObsidianBackground
import com.albin.betdaylite.core.designsystem.ObsidianBorder
import com.albin.betdaylite.core.designsystem.ObsidianSurface
import com.albin.betdaylite.core.designsystem.PillShape
import com.albin.betdaylite.core.designsystem.TextPrimary
import com.albin.betdaylite.core.designsystem.TextSecondary
import com.albin.betdaylite.presentation.components.BetSlipBottomSheet
import com.albin.betdaylite.presentation.components.FeaturedMatchBanner
import com.albin.betdaylite.presentation.components.FilterChipsRow
import com.albin.betdaylite.presentation.components.MatchCard
import com.albin.betdaylite.presentation.components.WalletBalanceHeader

@Composable
fun HomeScreen(
    onNavigateToMatchDetail: (String) -> Unit,
    onNavigateToBetsHistory: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDepositDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.betSuccessMessage) {
        uiState.betSuccessMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSuccessMessage()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearErrorMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = ObsidianBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(ObsidianBackground)
        ) {
            if (uiState.isLoading && uiState.matches.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GoldPrimary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = AppSpacing.md,
                        end = AppSpacing.md,
                        top = AppSpacing.md,
                        bottom = AppSpacing.xxl
                    ),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
                ) {
                    // 1. Header de Billetera
                    item {
                        WalletBalanceHeader(
                            balance = uiState.wallet?.balance ?: BetConstants.INITIAL_WALLET_BALANCE,
                            activeBetsCount = uiState.wallet?.activeBetsCount ?: 0,
                            onDepositClick = { showDepositDialog = true },
                            onActiveBetsClick = onNavigateToBetsHistory
                        )
                    }

                    // 2. Banner Destacado Cinematográfico
                    uiState.featuredMatch?.let { featured ->
                        item {
                            FeaturedMatchBanner(
                                match = featured,
                                onMatchClick = { onNavigateToMatchDetail(featured.id) },
                                onQuickBetClick = { pick -> viewModel.onPickSelected(featured, pick) }
                            )
                        }
                    }

                    // 3. Filtro de Ligas
                    item {
                        Column {
                            Text(
                                text = "LIGAS Y COMPETICIONES",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(bottom = AppSpacing.xs)
                            )
                            FilterChipsRow(
                                selectedLeague = uiState.selectedLeague,
                                onLeagueSelected = { viewModel.onLeagueSelected(it) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // 4. Lista de Partidos
                    item {
                        Text(
                            text = "TODOS LOS PARTIDOS (${uiState.matches.size})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(top = AppSpacing.xs)
                        )
                    }

                    if (uiState.matches.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(AppSpacing.xl),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No hay partidos disponibles para esta liga",
                                    color = TextSecondary,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        items(uiState.matches, key = { it.id }) { match ->
                            val currentPick = if (uiState.activeBetSlip?.match?.id == match.id) {
                                uiState.activeBetSlip?.pick
                            } else null

                            MatchCard(
                                match = match,
                                selectedPick = currentPick,
                                onPickSelected = { pick -> viewModel.onPickSelected(match, pick) },
                                onCardClick = { onNavigateToMatchDetail(match.id) }
                            )
                        }
                    }
                }
            }

            // Ticket de Apuesta Modal (BetSlip)
            uiState.activeBetSlip?.let { slip ->
                BetSlipBottomSheet(
                    betSlip = slip,
                    walletBalance = uiState.wallet?.balance ?: 0.0,
                    onDismiss = { viewModel.onDismissBetSlip() },
                    onConfirmBet = { stake -> viewModel.onConfirmBet(stake) }
                )
            }

            // Diálogo de Recarga Rápida de Saldo
            if (showDepositDialog) {
                AlertDialog(
                    onDismissRequest = { showDepositDialog = false },
                    title = {
                        Text(
                            text = "Recarga Rápida de Fondos",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Text(
                            text = "Selecciona una recarga simulada para tu billetera de apuestas:",
                            color = TextSecondary
                        )
                    },
                    containerColor = ObsidianSurface,
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.onDepositFunds(500.0)
                                showDepositDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = PillShape
                        ) {
                            Text("+ S/ 500.00", color = Color(0xFF1B1300), fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = {
                                viewModel.onDepositFunds(1000.0)
                                showDepositDialog = false
                            },
                            shape = PillShape
                        ) {
                            Text("+ S/ 1,000.00", color = GoldPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        }
    }
}
