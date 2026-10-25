package com.albin.betdaylite.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
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
import com.albin.betdaylite.core.constants.BetConstants
import com.albin.betdaylite.core.designsystem.AppSpacing
import com.albin.betdaylite.core.designsystem.NeonGreen
import com.albin.betdaylite.core.designsystem.ObsidianBorder
import com.albin.betdaylite.core.designsystem.ObsidianBorderFocused
import com.albin.betdaylite.core.designsystem.OddsButtonBackground
import com.albin.betdaylite.core.designsystem.OddsButtonShape
import com.albin.betdaylite.core.designsystem.TextPrimary
import com.albin.betdaylite.core.designsystem.TextSecondary

@Composable
fun OddsButton(
    label: String,
    odd: Double,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) NeonGreen else OddsButtonBackground,
        animationSpec = tween(durationMillis = 200),
        label = "odds_bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) NeonGreen else ObsidianBorder,
        animationSpec = tween(durationMillis = 200),
        label = "odds_border"
    )
    val labelColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF003815) else TextSecondary,
        label = "odds_label_color"
    )
    val oddColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF003815) else TextPrimary,
        label = "odds_val_color"
    )

    Column(
        modifier = modifier
            .defaultMinSize(minHeight = AppSpacing.oddsButtonHeight)
            .clip(OddsButtonShape)
            .background(bgColor)
            .border(1.dp, borderColor, OddsButtonShape)
            .clickable(onClick = onClick)
            .padding(vertical = AppSpacing.xs, horizontal = AppSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = labelColor
        )
        Text(
            text = BetConstants.formatOdd(odd),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = oddColor
        )
    }
}
