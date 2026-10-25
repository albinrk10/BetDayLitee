package com.albin.betdaylite.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.albin.betdaylite.core.designsystem.AppSpacing
import com.albin.betdaylite.core.designsystem.CardShape
import com.albin.betdaylite.core.designsystem.GoldPrimary
import com.albin.betdaylite.core.designsystem.ObsidianBackground
import com.albin.betdaylite.core.designsystem.ObsidianBorder
import com.albin.betdaylite.core.designsystem.ObsidianSurface
import com.albin.betdaylite.core.designsystem.ObsidianSurfaceVariant
import com.albin.betdaylite.core.designsystem.PillShape
import com.albin.betdaylite.core.designsystem.TextPrimary
import com.albin.betdaylite.core.designsystem.TextSecondary
import com.albin.betdaylite.core.designsystem.TextTertiary
import com.albin.betdaylite.domain.entities.BetPick
import com.albin.betdaylite.presentation.components.BetSlipBottomSheet
import com.albin.betdaylite.presentation.components.GlassmorphicIconButton
import com.albin.betdaylite.presentation.components.OddsButton

@Composable
fun MatchDetailScreen(
    matchId: String,
    onNavigateBack: () -> Unit,
    viewModel: MatchDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(matchId) {
        viewModel.loadMatch(matchId)
    }

    LaunchedEffect(uiState.betSuccessMessage) {
        uiState.betSuccessMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { err ->
            snackbarHostState.showSnackbar(err)
            viewModel.clearMessages()
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
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = GoldPrimary)
                }
            } else if (uiState.match == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Partido no encontrado", color = TextSecondary)
                }
            } else {
                val match = uiState.match!!
                val backdropGradient = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF223040),
                        Color(0xFF141920),
                        ObsidianBackground
                    )
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = AppSpacing.xxl)
                ) {
                    // Header Inmersivo con Backdrop y Equipos
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(backdropGradient)
                                .statusBarsPadding()
                                .padding(AppSpacing.md)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                // Barra superior: Botón atrás y Liga
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    GlassmorphicIconButton(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Atrás",
                                        onClick = onNavigateBack
                                    )

                                    Spacer(modifier = Modifier.weight(1f))

                                    Box(
                                        modifier = Modifier
                                            .clip(PillShape)
                                            .background(GoldPrimary.copy(alpha = 0.15f))
                                            .border(1.dp, GoldPrimary.copy(alpha = 0.4f), PillShape)
                                            .padding(horizontal = AppSpacing.sm, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "${match.league.name} • ${match.league.country}",
                                            color = GoldPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(AppSpacing.lg))

                                // Equipos y Marcador H2H
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Equipo Local
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(56.dp)
                                                .clip(CircleShape)
                                                .background(ObsidianSurfaceVariant)
                                                .border(1.dp, ObsidianBorder, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = match.homeTeam.shortName,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = GoldPrimary
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(AppSpacing.xs))
                                        Text(
                                            text = match.homeTeam.name,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "Local",
                                            fontSize = 11.sp,
                                            color = TextTertiary
                                        )
                                    }

                                    // VS Badge
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(ObsidianSurface)
                                            .border(1.dp, GoldPrimary.copy(alpha = 0.4f), CircleShape)
                                            .padding(horizontal = AppSpacing.md, vertical = AppSpacing.xs),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "VS",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Black,
                                            color = GoldPrimary
                                        )
                                    }

                                    // Equipo Visitante
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(56.dp)
                                                .clip(CircleShape)
                                                .background(ObsidianSurfaceVariant)
                                                .border(1.dp, ObsidianBorder, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = match.awayTeam.shortName,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = GoldPrimary
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(AppSpacing.xs))
                                        Text(
                                            text = match.awayTeam.name,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "Visitante",
                                            fontSize = 11.sp,
                                            color = TextTertiary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(AppSpacing.md))
                            }
                        }
                    }

                    // Mercado Principal: 1X2
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = AppSpacing.md, vertical = AppSpacing.xs)
                        ) {
                            Text(
                                text = "MERCADO PRINCIPAL (1X2 - GANADOR DEL PARTIDO)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(bottom = AppSpacing.xs)
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                                shape = CardShape
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(AppSpacing.md),
                                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
                                ) {
                                    OddsButton(
                                        label = "1 (${match.homeTeam.shortName})",
                                        odd = match.market.odds.home,
                                        isSelected = uiState.activeBetSlip?.pick == BetPick.HOME,
                                        onClick = { viewModel.onPickSelected(BetPick.HOME) },
                                        modifier = Modifier.weight(1f)
                                    )

                                    OddsButton(
                                        label = "X (Empate)",
                                        odd = match.market.odds.draw,
                                        isSelected = uiState.activeBetSlip?.pick == BetPick.DRAW,
                                        onClick = { viewModel.onPickSelected(BetPick.DRAW) },
                                        modifier = Modifier.weight(1f)
                                    )

                                    OddsButton(
                                        label = "2 (${match.awayTeam.shortName})",
                                        odd = match.market.odds.away,
                                        isSelected = uiState.activeBetSlip?.pick == BetPick.AWAY,
                                        onClick = { viewModel.onPickSelected(BetPick.AWAY) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    // Mercados Adicionales
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm)
                        ) {
                            Text(
                                text = "TOTAL DE GOLES (MÁS / MENOS 2.5)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(bottom = AppSpacing.xs)
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                                shape = CardShape
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(AppSpacing.md),
                                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                                ) {
                                    OddsButton(
                                        label = "Más de 2.5",
                                        odd = 1.85,
                                        isSelected = false,
                                        onClick = { viewModel.onPickSelected(BetPick.HOME) },
                                        modifier = Modifier.weight(1f)
                                    )
                                    OddsButton(
                                        label = "Menos de 2.5",
                                        odd = 1.95,
                                        isSelected = false,
                                        onClick = { viewModel.onPickSelected(BetPick.AWAY) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    // Tarjetas de Información del Encuentro
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm)
                        ) {
                            Text(
                                text = "INFORMACIÓN DEL ENCUENTRO",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(bottom = AppSpacing.xs)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
                            ) {
                                InfoMetricCard(
                                    icon = Icons.Default.LocationOn,
                                    title = "Sede",
                                    value = match.league.country,
                                    modifier = Modifier.weight(1f)
                                )
                                InfoMetricCard(
                                    icon = Icons.Default.SportsSoccer,
                                    title = "Competición",
                                    value = match.league.name,
                                    modifier = Modifier.weight(1f)
                                )
                                InfoMetricCard(
                                    icon = Icons.Default.Info,
                                    title = "Mercado",
                                    value = match.market.type,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // Ticket de Apuesta Modal
            uiState.activeBetSlip?.let { slip ->
                BetSlipBottomSheet(
                    betSlip = slip,
                    walletBalance = uiState.wallet?.balance ?: 0.0,
                    onDismiss = { viewModel.onDismissBetSlip() },
                    onConfirmBet = { stake -> viewModel.onConfirmBet(stake) }
                )
            }
        }
    }
}

@Composable
private fun InfoMetricCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(CardShape)
            .border(1.dp, ObsidianBorder, CardShape),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
        shape = CardShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.sm),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, fontSize = 10.sp, color = TextTertiary)
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1
            )
        }
    }
}
