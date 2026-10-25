package com.albin.betdaylite.presentation.history

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.albin.betdaylite.core.designsystem.NeonGreen
import com.albin.betdaylite.core.designsystem.ObsidianBackground
import com.albin.betdaylite.core.designsystem.ObsidianBorder
import com.albin.betdaylite.core.designsystem.ObsidianSurface
import com.albin.betdaylite.core.designsystem.ObsidianSurfaceVariant
import com.albin.betdaylite.core.designsystem.PillShape
import com.albin.betdaylite.core.designsystem.TextPrimary
import com.albin.betdaylite.core.designsystem.TextSecondary
import com.albin.betdaylite.core.designsystem.TextTertiary
import com.albin.betdaylite.domain.entities.Bet
import com.albin.betdaylite.domain.entities.BetStatus
import com.albin.betdaylite.presentation.components.StatusBadge
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun BetsHistoryScreen(
    viewModel: BetsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBackground)
            .padding(top = AppSpacing.md)
    ) {
        // Título de la Pantalla
        Text(
            text = "MIS APUESTAS",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary,
            modifier = Modifier.padding(horizontal = AppSpacing.md)
        )

        Spacer(modifier = Modifier.height(AppSpacing.sm))

        // Tabs de Filtro (Todas, Pendientes, Ganadas, Perdidas)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.md),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
        ) {
            val filters = listOf(
                null to "Todas",
                BetStatus.PENDING to "Activas",
                BetStatus.WON to "Ganadas",
                BetStatus.LOST to "Perdidas"
            )

            filters.forEach { (status, label) ->
                val isSelected = uiState.selectedFilter == status
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(PillShape)
                        .background(if (isSelected) GoldPrimary else ObsidianSurfaceVariant)
                        .border(
                            1.dp,
                            if (isSelected) GoldPrimary else ObsidianBorder,
                            PillShape
                        )
                        .clickable { viewModel.onFilterSelected(status) }
                        .padding(vertical = AppSpacing.xs),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) Color(0xFF1B1300) else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.md))

        // Lista de Apuestas
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GoldPrimary)
            }
        } else if (uiState.bets.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppSpacing.xl),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No tienes apuestas en esta categoría",
                        fontSize = 15.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = AppSpacing.md,
                    end = AppSpacing.md,
                    bottom = AppSpacing.xxl
                ),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
            ) {
                items(uiState.bets, key = { it.id }) { bet ->
                    BetTicketCard(bet = bet)
                }
            }
        }
    }
}

@Composable
private fun BetTicketCard(
    bet: Bet,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM • HH:mm", Locale.getDefault())
        .withZone(ZoneId.systemDefault())
    val formattedDate = try {
        dateFormatter.format(bet.placedAt)
    } catch (e: Exception) {
        "12 Feb"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardShape)
            .border(1.dp, ObsidianBorder, CardShape),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
        shape = CardShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.md)
        ) {
            // Cabecera del Ticket: Estado y Fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = bet.status)
                Text(
                    text = formattedDate,
                    fontSize = 12.sp,
                    color = TextTertiary
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.sm))

            // Partido
            Text(
                text = bet.matchDescription,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = bet.leagueName,
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Divider(color = ObsidianBorder.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(AppSpacing.sm))

            // Predicción y Cuota
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Predicción",
                        fontSize = 11.sp,
                        color = TextTertiary
                    )
                    Text(
                        text = "${bet.pick.label} @ ${BetConstants.formatOdd(bet.odd)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Apostado",
                        fontSize = 11.sp,
                        color = TextTertiary
                    )
                    Text(
                        text = BetConstants.formatCurrency(bet.stake),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (bet.status == BetStatus.WON) "Ganancia Pagada" else "Retorno Potencial",
                        fontSize = 11.sp,
                        color = TextTertiary
                    )
                    val displayPayout = when (bet.status) {
                        BetStatus.WON -> bet.payout ?: (bet.stake * bet.odd)
                        BetStatus.LOST -> 0.0
                        BetStatus.PENDING -> bet.potentialPayout
                    }
                    Text(
                        text = BetConstants.formatCurrency(displayPayout),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (bet.status == BetStatus.WON) NeonGreen else TextPrimary
                    )
                }
            }
        }
    }
}
