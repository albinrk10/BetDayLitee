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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.albin.betdaylite.core.constants.BetConstants
import com.albin.betdaylite.core.designsystem.AppSpacing
import com.albin.betdaylite.core.designsystem.CardShape
import com.albin.betdaylite.core.designsystem.GoldGradientEnd
import com.albin.betdaylite.core.designsystem.GoldPrimary
import com.albin.betdaylite.core.designsystem.ObsidianBackground
import com.albin.betdaylite.core.designsystem.ObsidianBorder
import com.albin.betdaylite.core.designsystem.PillShape
import com.albin.betdaylite.core.designsystem.TextPrimary
import com.albin.betdaylite.core.designsystem.TextSecondary
import com.albin.betdaylite.domain.entities.BetPick
import com.albin.betdaylite.domain.entities.Match

@Composable
fun FeaturedMatchBanner(
    match: Match,
    onMatchClick: () -> Unit,
    onQuickBetClick: (BetPick) -> Unit,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1E2836),
            Color(0xFF151A22),
            ObsidianBackground
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardShape)
            .background(gradient)
            .border(1.dp, GoldPrimary.copy(alpha = 0.35f), CardShape)
            .clickable(onClick = onMatchClick)
            .padding(AppSpacing.md)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Badge Partido de la jornada
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(PillShape)
                        .background(GoldPrimary.copy(alpha = 0.15f))
                        .border(1.dp, GoldPrimary.copy(alpha = 0.5f), PillShape)
                        .padding(horizontal = AppSpacing.sm, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Hot",
                        tint = GoldPrimary,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = "PARTIDO DE LA JORNADA",
                        color = GoldPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = match.league.name,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.md))

            // Enfrentamiento Estelar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${match.homeTeam.name}\nvs ${match.awayTeam.name}",
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                // Botón Acción Rápida
                Button(
                    onClick = onMatchClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = Color(0xFF1B1300)
                    ),
                    shape = PillShape,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = AppSpacing.md,
                        vertical = AppSpacing.xs
                    )
                ) {
                    Text(
                        text = "Ver Partido",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.md))

            // Cuotas Rápidas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
            ) {
                OddsButton(
                    label = "1 (${match.homeTeam.shortName})",
                    odd = match.market.odds.home,
                    isSelected = false,
                    onClick = { onQuickBetClick(BetPick.HOME) },
                    modifier = Modifier.weight(1f)
                )
                OddsButton(
                    label = "X (Empate)",
                    odd = match.market.odds.draw,
                    isSelected = false,
                    onClick = { onQuickBetClick(BetPick.DRAW) },
                    modifier = Modifier.weight(1f)
                )
                OddsButton(
                    label = "2 (${match.awayTeam.shortName})",
                    odd = match.market.odds.away,
                    isSelected = false,
                    onClick = { onQuickBetClick(BetPick.AWAY) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
