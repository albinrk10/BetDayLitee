package com.albin.betdaylite.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.albin.betdaylite.core.designsystem.AppSpacing
import com.albin.betdaylite.core.designsystem.CardShape
import com.albin.betdaylite.core.designsystem.LiveBadgeRed
import com.albin.betdaylite.core.designsystem.ObsidianBorder
import com.albin.betdaylite.core.designsystem.ObsidianSurface
import com.albin.betdaylite.core.designsystem.ObsidianSurfaceVariant
import com.albin.betdaylite.core.designsystem.PillShape
import com.albin.betdaylite.core.designsystem.TextPrimary
import com.albin.betdaylite.core.designsystem.TextSecondary
import com.albin.betdaylite.core.designsystem.TextTertiary
import com.albin.betdaylite.domain.entities.BetPick
import com.albin.betdaylite.domain.entities.Match
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun MatchCard(
    match: Match,
    selectedPick: BetPick?,
    onPickSelected: (BetPick) -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
        .withZone(ZoneId.systemDefault())
    val formattedTime = try {
        timeFormatter.format(match.startTime)
    } catch (e: Exception) {
        "20:00"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardShape)
            .border(1.dp, ObsidianBorder, CardShape)
            .clickable(onClick = onCardClick),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
        shape = CardShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.md)
        ) {
            // Cabecera: Liga y Hora / Live
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${match.league.name.uppercase()} • ${match.league.country.uppercase()}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextTertiary,
                    letterSpacing = 0.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                if (match.isLive) {
                    Row(
                        modifier = Modifier
                            .clip(PillShape)
                            .background(LiveBadgeRed.copy(alpha = 0.15f))
                            .padding(horizontal = AppSpacing.xs, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FiberManualRecord,
                            contentDescription = "Live",
                            tint = LiveBadgeRed,
                            modifier = Modifier.size(8.dp)
                        )
                        Spacer(modifier = Modifier.width(AppSpacing.xxs))
                        Text(
                            text = "EN VIVO",
                            color = LiveBadgeRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(
                        text = "Hoy $formattedTime",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.sm))

            // Equipos y Enfrentamiento
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Local
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = match.homeTeam.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = match.homeTeam.shortName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }

                // VS o Marcador
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(ObsidianSurfaceVariant)
                        .padding(horizontal = AppSpacing.sm, vertical = AppSpacing.xxs),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (match.isLive) "${match.homeScore ?: 0} - ${match.awayScore ?: 0}" else "VS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (match.isLive) LiveBadgeRed else TextTertiary
                    )
                }

                // Visitante
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = match.awayTeam.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = match.awayTeam.shortName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.md))

            // Botones de Cuotas 1X2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
            ) {
                OddsButton(
                    label = "1 (${match.homeTeam.shortName})",
                    odd = match.market.odds.home,
                    isSelected = selectedPick == BetPick.HOME,
                    onClick = { onPickSelected(BetPick.HOME) },
                    modifier = Modifier.weight(1f)
                )

                OddsButton(
                    label = "X (Empate)",
                    odd = match.market.odds.draw,
                    isSelected = selectedPick == BetPick.DRAW,
                    onClick = { onPickSelected(BetPick.DRAW) },
                    modifier = Modifier.weight(1f)
                )

                OddsButton(
                    label = "2 (${match.awayTeam.shortName})",
                    odd = match.market.odds.away,
                    isSelected = selectedPick == BetPick.AWAY,
                    onClick = { onPickSelected(BetPick.AWAY) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
