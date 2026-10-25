package com.albin.betdaylite.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.albin.betdaylite.core.constants.BetConstants
import com.albin.betdaylite.core.designsystem.AppSpacing
import com.albin.betdaylite.core.designsystem.GoldPrimary
import com.albin.betdaylite.core.designsystem.ObsidianBorder
import com.albin.betdaylite.core.designsystem.ObsidianSurfaceVariant
import com.albin.betdaylite.core.designsystem.PillShape
import com.albin.betdaylite.core.designsystem.TextPrimary
import com.albin.betdaylite.core.designsystem.TextSecondary

@Composable
fun FilterChipsRow(
    selectedLeague: String,
    onLeagueSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    leagues: List<String> = BetConstants.POPULAR_LEAGUES
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(vertical = AppSpacing.xs),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
    ) {
        leagues.forEach { league ->
            val isSelected = league == selectedLeague

            val bgColor by animateColorAsState(
                targetValue = if (isSelected) GoldPrimary else ObsidianSurfaceVariant,
                label = "chip_bg"
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) Color(0xFF1B1300) else TextSecondary,
                label = "chip_text"
            )
            val borderColor by animateColorAsState(
                targetValue = if (isSelected) GoldPrimary else ObsidianBorder,
                label = "chip_border"
            )

            Box(
                modifier = Modifier
                    .clip(PillShape)
                    .background(bgColor)
                    .border(1.dp, borderColor, PillShape)
                    .clickable { onLeagueSelected(league) }
                    .padding(horizontal = AppSpacing.md, vertical = AppSpacing.xs)
            ) {
                Text(
                    text = league,
                    color = textColor,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}
